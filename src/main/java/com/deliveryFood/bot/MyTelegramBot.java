package com.deliveryFood.bot;

import com.deliveryFood.models.User;
import com.deliveryFood.services.TelegramBotService;
import com.deliveryFood.services.TelegramCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
@RequiredArgsConstructor
public class MyTelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.name}")
    private String botName;

    private final TelegramBotService telegramBotService;
    private final TelegramCommandService telegramCommandService;

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
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleTextMessage(update);
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update);
        }
    }

    private void handleTextMessage(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();

        // Обработать текстовые сообщения
        String response = telegramCommandService.processCommand(text, update.getMessage().getFrom().getUserName(), chatId, null, null);

        // Передайте пользователя, если он аутентифицирован
        User user = telegramCommandService.getUserByChatId(chatId);
        sendResponse(chatId, response, user);
    }

    private void handleCallbackQuery(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String callbackData = update.getCallbackQuery().getData();

        // Обработать обратный вызов
        String response = telegramCommandService.processCommand(callbackData, update.getCallbackQuery().getFrom().getUserName(), chatId, null, null);

        // Передайте пользователя
        User user = telegramCommandService.getUserByChatId(chatId);
        sendResponse(chatId, response, user);

        // Ответ на callbackQuery
        try {
            AnswerCallbackQuery answerCallbackQuery = AnswerCallbackQuery.builder()
                    .callbackQueryId(update.getCallbackQuery().getId())
                    .text("Вы выбрали: " + callbackData)
                    .showAlert(false)
                    .cacheTime(0)
                    .build();

            execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            log.error("Ошибка при ответе на callback query: {}", e.getMessage());
        }
    }

    private void sendResponse(Long chatId, String response, User user) {
        telegramBotService.sendTelegramMessage(chatId, response, user);
    }
}