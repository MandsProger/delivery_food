package com.deliveryFood.services;

import com.deliveryFood.models.TelegramBotRequest;
import com.deliveryFood.models.User;
import com.deliveryFood.models.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramCommandService {

    private final UserService userService;
    private final TelegramBotRequestService telegramBotRequestService;
    private final TelegramBotService telegramBotService;

    private final Map<Long, String> emailMap = new HashMap<>();       // Хранит email текущего пользователя
    private final Map<Long, String> waitingForInput = new HashMap<>(); // Состояние ожидания ввода
    private final Map<Long, Boolean> loggedInUsers = new HashMap<>();   // Залогиненные пользователи
    private final Map<Long, User> userMap = new HashMap<>();            // Хранилище пользователей по chatId

    // Сотояния waitingForInput
    private final String EMAIL = "EMAIL";
    private final String PASSWORD = "PASSWORD";
    private final String REQUEST = "REQUEST";
    private final String WAIT = "WAIT";

    public String processCommand(String command, String userName, long chatId, String email, String password) {
        switch (command) {
            case "/start":
                return "Добро пожаловать, " + userName + "! Введите /login для начала.";
            case "/login":
                return startLogin(chatId);
            case "/leave_request":
            case "Оставить обращение":
                return handleLeaveRequest(chatId);
            case "/my_requests":
            case "Мои обращения":
                return showUserRequests(chatId);
            case "/logout":
            case "Выйти":
                return logoutUser(chatId);
            case "/view_requests":
                return handleViewRequests(chatId);
            default:
                return handleUserInput(command, chatId);
        }
    }

    private String startLogin(long chatId) {
        if (loggedInUsers.getOrDefault(chatId, false)) {
            return "Вы уже вошли в систему! Чтобы оставить обращение, используйте команду /leave_request. Чтобы выйти, используйте команду /logout.";
        }
        emailMap.put(chatId, null);
        waitingForInput.put(chatId, EMAIL);
        return "Введите вашу почту:";
    }

    private String handleLeaveRequest(long chatId) {
        if (loggedInUsers.getOrDefault(chatId, false)) {
            waitingForInput.put(chatId, REQUEST);
            return "Введите ваше обращение:";
        } else {
            return "Вы должны войти в систему, чтобы оставить обращение. \nПожалуйста, введите /login.";
        }
    }

    private String handleViewRequests(long chatId) {
        if (isUserAdmin(chatId)) {
            List<TelegramBotRequest> requests = telegramBotRequestService.getAllRequests();
            telegramBotService.sendTelegramBotRequestListToManager(chatId, requests);
            return "Список обращений отправлен.";
        } else {
            return "Вы не имеете прав для просмотра обращений.";
        }
    }

    private String handleUserInput(String input, long chatId) {
        String state = waitingForInput.get(chatId);

        if (state == null) {
            return "Введите /login для начала.";
        }

        if (state.equals("EMAIL")) {
            emailMap.put(chatId, input);
            waitingForInput.put(chatId, PASSWORD);
            return "Введите ваш пароль:";
        } else if (state.equals(PASSWORD)) {
            return handlePasswordInput(chatId, input);
        } else if (state.equals(REQUEST)) {
            return handleRequestInput(chatId, input);
        } else if (state.equals(WAIT)) {
            return "Неверный ввод. \nВведите /help для вызова навигаицонного меню.";
        }

        return "Неверный ввод. \nВведите /login для начала.";
    }

    private String handlePasswordInput(long chatId, String input) {
        String email = emailMap.get(chatId);
        User user = userService.getUserByEmail(email);
        if (user != null && userService.authenticateTelegramBot(email, input)) {
            loggedInUsers.put(chatId, true);
            userMap.put(chatId, user);
            waitingForInput.put(chatId, WAIT);
            return "Успешный вход! \nЧтобы оставить обращение, используйте команду /leave_request.";
        } else {
            cleanupUserSession(chatId);
            return "Неверный email или пароль.";
        }
    }

    private String handleRequestInput(long chatId, String input) {
        User user = userMap.get(chatId);
        if (user != null) {
            telegramBotRequestService.createRequest(user, input);
            waitingForInput.put(chatId, WAIT);
            return "Ваше обращение принято: " + input + "\nСпасибо за ваше сообщение!";
        }
        return "Произошла ошибка, пользователь не найден.";
    }

    private void cleanupUserSession(long chatId) {
        waitingForInput.remove(chatId);
        emailMap.remove(chatId);
    }

    private String showUserRequests(long chatId) {
        User user = userMap.get(chatId);
        if (user != null) {
            List<TelegramBotRequest> requests = telegramBotRequestService.getRequestsByUser(user);
            StringBuilder response = new StringBuilder("Ваши обращения:\n");
            for (TelegramBotRequest request : requests) {
                response.append("ID: ").append(request.getId())
                        .append(", Сообщение: ").append(request.getMessage())
                        .append(", Статус: ").append(request.getStatus()).append("\n");
            }
            return response.toString();
        }
        return "Вы должны войти в систему, чтобы поссмотреть свои обращения. \nПожалуйста, введите /login.";
    }

    private String logoutUser(long chatId) {
        if (loggedInUsers.get(chatId) != null) {
            loggedInUsers.remove(chatId);
            emailMap.remove(chatId);
            waitingForInput.remove(chatId);
            userMap.remove(chatId);
            return "Вы вышли из аккаунта.";
        } else {
            return "Вы не вошли в аккаунт, чтобы из него выходить";
        }
    }

    private boolean isUserAdmin(Long chatId) {
        User user = userMap.get(chatId);
        return user != null && user.getRoles().contains(Role.ROLE_ADMIN);
    }
}