package com.deliveryFood.bot;

import com.deliveryFood.services.TelegramBotService;
import com.deliveryFood.services.TelegramCommandService;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MyTelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.name}")
    private String botName;

    private final TelegramBotService telegramBotService;
    private final TelegramCommandService telegramCommandService;

    // Хранилище для отслеживания состояния ввода
    private final Map<Long, String> waitingForInput = new HashMap<>();

    public MyTelegramBot(TelegramBotService telegramBotService, TelegramCommandService telegramCommandService) {
        this.telegramBotService = telegramBotService;
        this.telegramCommandService = telegramCommandService;
    }


    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            // Обрабатываем нажатие на кнопку
            String command = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            String userName = update.getCallbackQuery().getFrom().getFirstName();

            String response = telegramCommandService.processCommand(command, userName, chatId, null, null);
            telegramBotService.sendTelegramMessage(chatId, response, this);

        } else if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String userName = update.getMessage().getFrom().getFirstName();

            String response = telegramCommandService.processCommand(messageText, userName, chatId, null, null);
            telegramBotService.sendTelegramMessage(chatId, response, this);
        }
    }
}