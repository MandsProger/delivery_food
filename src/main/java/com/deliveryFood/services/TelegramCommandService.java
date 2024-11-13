package com.deliveryFood.services;

import com.deliveryFood.models.TelegramBotRequest;
import com.deliveryFood.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramCommandService {

    private final UserService userService;
    private final TelegramBotRequestService telegramBotRequestService;

    // Для хранения email пользователей
    private final Map<Long, String> emailMap = new HashMap<>();

    // Для хранения статусов ввода
    private final Map<Long, String> waitingForInput = new HashMap<>();

    // Для отслеживания авторизованных пользователей
    private final Map<Long, Boolean> loggedInUsers = new HashMap<>();

    public String processCommand(String command, String userName, long chatId, String email, String password) {
        switch (command) {
            case "/start":
                return "Добро пожаловать, " + userName + "! Введите /login для начала.";
            case "/login":
                emailMap.put(chatId, null); // Начинаем с null для email
                waitingForInput.put(chatId, "EMAIL"); // Устанавливаем состояние ожидания ввода
                return "Введите вашу почту:";
            case "/leave_request":
                if (loggedInUsers.getOrDefault(chatId, false)) {
                    waitingForInput.put(chatId, "REQUEST");
                    return "Введите ваше обращение:";
                } else {
                    return "Вы должны войти в систему, чтобы оставить обращение.";
                }
            default:
                return handleUserInput(command, chatId);
        }
    }

    private String handleUserInput(String input, long chatId) {
        String state = waitingForInput.get(chatId);

        if (state == null) {
            return "Введите /login для начала.";
        }

        if (state.equals("EMAIL")) {
            emailMap.put(chatId, input);
            waitingForInput.put(chatId, "PASSWORD"); // Переход к следующему состоянию
            return "Введите ваш пароль:";
        } else if (state.equals("PASSWORD")) {
            String email = emailMap.get(chatId);
            User user = userService.getUserByEmail(email);
            if (user != null && userService.authenticateTelegramBot(email, input)) {
                loggedInUsers.put(chatId, true); // Устанавливаем пользователя как авторизованного
                waitingForInput.put(chatId, "REQUEST"); // Устанавливаем состояние для следующего ввода
                return "Успешный вход! Теперь вы можете отправить запрос.";
            } else {
                waitingForInput.remove(chatId); // Удаляем состояние входа
                emailMap.remove(chatId); // Удаляем email пользователя
                return "Неверный email или пароль.";
            }
        } else if (state.equals("REQUEST")) {
            String email = emailMap.get(chatId);
            User user = userService.getUserByEmail(email);
            telegramBotRequestService.createRequest(user, input); // Создаем обращение в БД
            waitingForInput.remove(chatId); // Удаляем состояние
            return "Ваше обращение принято: " + input + "\nСпасибо за ваше сообщение!";
        }

        return "Неверный ввод. Введите /login для начала.";
    }
}