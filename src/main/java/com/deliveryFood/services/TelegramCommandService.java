package com.deliveryFood.services;

import org.springframework.stereotype.Service;

@Service
public class TelegramCommandService {

    public String processCommand(String command, String userName) {
        switch (command) {
            case "/start":
                return "Добро пожаловать, " + userName + "! Чем я могу помочь?";
            case "/help":
                return "Доступные команды:\n/start - Начать взаимодействие с ботом\n/help - Получить справку по доступным командам";
            default:
                return "Неизвестная команда. Попробуйте /help для получения списка доступных команд.";
        }
    }
}