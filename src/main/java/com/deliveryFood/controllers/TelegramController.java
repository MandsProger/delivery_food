package com.deliveryFood.controller;

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
        String response = telegramCommandService.processCommand("/start", "Пользователь"); // Передайте имя пользователя
        telegramBotService.sendTelegramMessage(Long.parseLong(chatId), response, null); // Передайте ваш бот
    }

    @PostMapping("/help")
    public void handleHelp(@RequestParam String chatId) {
        String response = telegramCommandService.processCommand("/help", "Пользователь");
        telegramBotService.sendTelegramMessage(Long.parseLong(chatId), response, null); // Передайте ваш бот
    }
}