package com.deliveryFood.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TelegramBotService {

    public void sendTelegramMessage(Long chatId, String text, AbsSender bot) {
        SendMessage message = new SendMessage(String.valueOf(chatId), text);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendHelpMessage(Long chatId, AbsSender bot) {
        String helpText = "Доступные команды:\n" +
                "/start - Запуск бота\n" +
                "/help - Список команд\n" +
                "/order - Оформить заказ\n" +
                "/status - Проверить статус заказа\n" +
                "/cancel - Отменить заказ";

        sendTelegramMessage(chatId, helpText, bot);
    }
}