package com.deliveryFood.services;

import com.deliveryFood.bot.MyTelegramBot;
import com.deliveryFood.models.TelegramBotRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TelegramBotService {

    private final MyTelegramBot bot; // Ваш бот

    public TelegramBotService(@Lazy MyTelegramBot bot) {
        this.bot = bot;
    }

    public void sendTelegramMessage(long chatId, String text, boolean addButtons) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        // Если нужно добавить кнопки
        if (addButtons) {
            addMenuButtons(message);
        }

        log.info("Отправка сообщения в чат с ID: {}", chatId);
        log.info("Сообщение: {}", message.getText());
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения в Telegram: {}", e.getMessage());
        }
    }

    private void addMenuButtons(SendMessage message) {
        // Создание кнопок
        KeyboardButton buttonLeaveRequest = new KeyboardButton("Оставить обращение");
        KeyboardButton buttonMyRequests = new KeyboardButton("Мои обращения");
        KeyboardButton buttonLogout = new KeyboardButton("Выйти");

        // Создание клавиатуры
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false); // Клавиатура останется открытой после использования

        List<KeyboardRow> keyboard = new ArrayList<>();

        // Добавление кнопок в ряд
        KeyboardRow row1 = new KeyboardRow();
        row1.add(buttonLeaveRequest);
        row1.add(buttonMyRequests);

        // Добавление кнопки "Выйти" на новый ряд
        KeyboardRow row2 = new KeyboardRow();
        row2.add(buttonLogout);

        // Добавление рядов в клавиатуру
        keyboard.add(row1);
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);
    }

    public void sendTelegramBotRequestListToManager(long chatId, List<TelegramBotRequest> requests) {
        StringBuilder message = new StringBuilder("Список обращений:\n");

        if (requests == null || requests.isEmpty()) {
            message.append("Нет обращений для отображения.");
        } else {
            for (TelegramBotRequest request : requests) {
                message.append("ID: ").append(request.getId())
                        .append(", Сообщение: ").append(request.getMessage())
                        .append(", Статус: ").append(request.getStatus()).append("\n");
            }
        }

        sendTelegramMessage(chatId, message.toString(), true);
    }
}