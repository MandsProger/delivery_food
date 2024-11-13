package com.deliveryFood.services;

import com.deliveryFood.models.TelegramBotRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramBotService {

    // Метод для отправки сообщения в Telegram
    public void sendTelegramMessage(long chatId, String text, TelegramLongPollingBot bot) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId)); // Приводим chatId к строке
        message.setText(text);

        // Проверяем, чтобы добавить кнопку только для успешных входов
        if ("Успешный вход!".equals(text)) {
            // Инициализируем InlineKeyboardMarkup и создаем кнопку
            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows = new ArrayList<>(); // Инициализируем список строк

            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText("Оставить обращение");
            button.setCallbackData("/leave_request"); // Установка команды для кнопки

            // Добавляем кнопку в строку и строку в список
            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(button);
            rows.add(row);

            keyboardMarkup.setKeyboard(rows); // Устанавливаем клавиатуру
            message.setReplyMarkup(keyboardMarkup); // Устанавливаем клавиатуру в сообщение
        }

        // Попытка отправить сообщение
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения в Telegram: {}", e.getMessage());
        }
    }

    // Метод для отправки списка обращений менеджеру
    public void sendTelegramBotRequestListToManager(long chatId, List<TelegramBotRequest> requests) {
        StringBuilder message = new StringBuilder("Список обращений:\n");

        // Проверка на пустой список обращений
        if (requests == null || requests.isEmpty()) {
            message.append("Нет обращений для отображения.");
        } else {
            for (TelegramBotRequest request : requests) {
                message.append("ID: ").append(request.getId())
                        .append(", Сообщение: ").append(request.getMessage())
                        .append(", Статус: ").append(request.getStatus()).append("\n");
            }
        }

        // Отправка сообщения администратору
        sendTelegramMessage(chatId, message.toString(), null);
    }
}