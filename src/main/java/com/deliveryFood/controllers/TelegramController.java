package com.deliveryFood.controllers;

import com.deliveryFood.services.TelegramBotService;
import com.deliveryFood.services.TelegramCommandService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/telegram")
public class TelegramController {

    private final TelegramBotService telegramBotService;
    private final TelegramCommandService telegramCommandService;

    public TelegramController(TelegramBotService telegramBotService, TelegramCommandService telegramCommandService) {
        this.telegramBotService = telegramBotService;
        this.telegramCommandService = telegramCommandService;
    }

    @PostMapping("/start")
    public void handleStart(@RequestParam String chatId) {
        long chatIdLong = Long.parseLong(chatId);
        String response = telegramCommandService.processCommand("/start", "Пользователь", chatIdLong, null, null);
        telegramBotService.sendTelegramMessage(chatIdLong, response, false); // Если вам не нужны кнопки
    }

    @PostMapping("/help")
    public void handleHelp(@RequestParam String chatId) {
        long chatIdLong = Long.parseLong(chatId);
        String response = telegramCommandService.processCommand("/help", "Пользователь", chatIdLong, null, null);
        telegramBotService.sendTelegramMessage(chatIdLong, response, false); // Если вам не нужны кнопки
    }

    @PostMapping("/login")
    public void handleLogin(@RequestParam String chatId, @RequestParam(required = false) String email, @RequestParam(required = false) String password) {
        long chatIdLong = Long.parseLong(chatId);
        String response;

        if (email == null) {
            // Если email не передан, значит, мы ожидаем его ввода
            response = telegramCommandService.processCommand("/login", "Пользователь", chatIdLong, null, null);
        } else {
            // В этом случае вводится пароль, который был получен ранее
            response = telegramCommandService.processCommand("", "Пользователь", chatIdLong, email, password);
        }

        telegramBotService.sendTelegramMessage(chatIdLong, response, false); // Если вам не нужны кнопки
    }
}