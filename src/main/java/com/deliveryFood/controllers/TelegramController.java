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
        String response = telegramCommandService.processCommand("/start", "Пользователь", Long.parseLong(chatId), null, null);
        telegramBotService.sendTelegramMessage(Long.parseLong(chatId), response, null);
    }

    @PostMapping("/help")
    public void handleHelp(@RequestParam String chatId) {
        String response = telegramCommandService.processCommand("/help", "Пользователь", Long.parseLong(chatId), null, null);
        telegramBotService.sendTelegramMessage(Long.parseLong(chatId), response, null);
    }

    @PostMapping("/login")
    public void handleLogin(@RequestParam String chatId, @RequestParam(required = false) String email, @RequestParam(required = false) String password) {
        if (email == null) {
            // Если email не передан, значит, мы ожидаем его ввода
            String response = telegramCommandService.processCommand("/login", "Пользователь", Long.parseLong(chatId), null, null);
            telegramBotService.sendTelegramMessage(Long.parseLong(chatId), response, null);
        } else {
            // В этом случае вводится пароль, который был получен ранее
            String response = telegramCommandService.processCommand("", "Пользователь", Long.parseLong(chatId), email, password);
            telegramBotService.sendTelegramMessage(Long.parseLong(chatId), response, null);
        }
    }
}