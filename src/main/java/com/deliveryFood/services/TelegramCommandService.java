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

    private final Map<Long, String> emailMap = new HashMap<>();
    private final Map<Long, String> waitingForInput = new HashMap<>();
    private final Map<Long, User> userMap = new HashMap<>();

    private final String EMAIL = "EMAIL";
    private final String PASSWORD = "PASSWORD";
    private final String REQUEST = "REQUEST";

    public String processCommand(String command, String userName, Long chatId, String email, String password) {
        User user = userMap.get(chatId); // Получаем пользователя по chatId

        switch (command) {
            case "/start":
                return "Добро пожаловать, " + userName + "! Введите /login для начала.";
            case "/login":
                return startLogin(chatId);
            case "Оставить обращение":
                return handleLeaveRequest(chatId);
            case "Мои обращения":
                return showUserRequests(chatId);
            case "/logout":
            case "Выйти":
                return logoutUser(chatId);
            case "Посмотреть оставленные обращения":
                return handleViewRequests(chatId);
            default:
                return handleUserInput(command, chatId);
        }
    }

    public User getUserByChatId(Long chatId) {
        return userMap.get(chatId);
    }

    private String startLogin(Long chatId) {
        if (userMap.containsKey(chatId)) {
            return "Вы уже вошли в систему!";
        }
        emailMap.put(chatId, null);
        waitingForInput.put(chatId, EMAIL);
        return "Введите вашу почту:";
    }

    private String handleLeaveRequest(Long chatId) {
        User user = userMap.get(chatId);
        if (user != null) {
            waitingForInput.put(chatId, REQUEST);
            return "Введите ваше обращение:";
        } else {
            return "Вы должны войти в систему, чтобы оставить обращение. \nПожалуйста, введите /login.";
        }
    }

    private String handleViewRequests(Long chatId) {
        if (isUserAdmin(chatId)) {
            List<TelegramBotRequest> requests = telegramBotRequestService.getAllRequests();
            telegramBotService.sendTelegramBotRequestListToManager(chatId, requests, userMap.get(chatId));
            return "Список обращений отправлен.";
        } else {
            return "Вы не имеете прав для просмотра обращений.";
        }
    }

    private String handleUserInput(String input, Long chatId) {
        String state = waitingForInput.get(chatId);

        if (state == null) {
            return "Неправильный ввод";
        }

        if (state.equals(EMAIL)) {
            emailMap.put(chatId, input);
            waitingForInput.put(chatId, PASSWORD);
            return "Введите ваш пароль:";
        } else if (state.equals(PASSWORD)) {
            return handlePasswordInput(chatId, input);
        } else if (state.equals(REQUEST)) {
            return handleRequestInput(chatId, input);
        }

        return "Неверный ввод. \nВведите /login для начала.";
    }

    private String handlePasswordInput(Long chatId, String input) {
        String email = emailMap.get(chatId);
        User user = userService.getUserByEmail(email);
        if (user != null && userService.authenticateTelegramBot(email, input)) {
            userMap.put(chatId, user);
            waitingForInput.remove(chatId);
            return "Успешный вход! \nЧтобы оставить обращение, используйте кнопку 'Оставить обращение'.";
        } else {
            cleanupUserSession(chatId);
            return "Неверный email или пароль.";
        }
    }

    private String handleRequestInput(Long chatId, String input) {
        User user = userMap.get(chatId);
        if (user != null) {
            telegramBotRequestService.createRequest(user, input);
            waitingForInput.remove(chatId);
            return "Ваше обращение принято: " + input + "\nСпасибо за ваше сообщение!";
        }
        return "Произошла ошибка, пользователь не найден.";
    }

    private void cleanupUserSession(Long chatId) {
        emailMap.remove(chatId);
        waitingForInput.remove(chatId);
    }

    private String showUserRequests(Long chatId) {
        User user = userMap.get(chatId);
        if (user != null) {
            List<TelegramBotRequest> requests = telegramBotRequestService.getRequestsByUser(user);
            StringBuilder response = new StringBuilder("Ваши обращения:\n");
            for (TelegramBotRequest request : requests) {
                response.append("ID: ").append(request.getId())
                        .append(", Сообщение: ").append(request.getMessage())
                        .append(", Статус: ").append(request.getStatus()).append("\n");
            }
            waitingForInput.remove(chatId);
            return response.toString();
        }
        return "Вы должны войти в систему, чтобы посмотреть свои обращения.";
    }

    private String logoutUser(Long chatId) {
        if (userMap.containsKey(chatId)) {
            userMap.remove(chatId);
            waitingForInput.remove(chatId);
            return "Вы вышли из аккаунта.";
        } else {
            return "Вы не вошли в аккаунт.";
        }
    }

    private boolean isUserAdmin(Long chatId) {
        User user = userMap.get(chatId);
        return user != null && user.getRoles().contains(Role.ROLE_ADMIN);
    }
}