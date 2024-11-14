package com.deliveryFood.services;

import com.deliveryFood.bot.MyTelegramBot;
import com.deliveryFood.models.TelegramBotRequest;
import com.deliveryFood.models.User;
import com.deliveryFood.models.enums.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TelegramBotService {

    private final MyTelegramBot bot;

    public TelegramBotService(@Lazy MyTelegramBot bot) {
        this.bot = bot;
    }

    public void sendTelegramMessage(long chatId, String text, User user) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        addMenuButtons(message, user); // Добавляем кнопки в сообщение

        log.info("Отправка сообщения в чат с ID: {}", chatId);
        log.info("Сообщение: {}", message.getText());
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения в Telegram: {}", e.getMessage());
        }
    }

    private void addMenuButtons(SendMessage message, User user) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        if (user != null) { // Проверяем, если пользователь аутентифицирован
            List<InlineKeyboardButton> actionRow = new ArrayList<>();
            List<InlineKeyboardButton> logoutRow = new ArrayList<>();

            if (user.getRoles().contains(Role.ROLE_ADMIN)) {
                actionRow.add(InlineKeyboardButton.builder()
                        .text("Посмотреть оставленные обращения")
                        .callbackData("Посмотреть оставленные обращения")
                        .build());
            }

            if (user.getRoles().contains(Role.ROLE_USER)) {
                actionRow.add(InlineKeyboardButton.builder()
                        .text("Оставить обращение")
                        .callbackData("Оставить обращение")
                        .build());
                actionRow.add(InlineKeyboardButton.builder()
                        .text("Мои обращения")
                        .callbackData("Мои обращения")
                        .build());
            }

            // Кнопка выхода
            logoutRow.add(InlineKeyboardButton.builder()
                    .text("Выйти")
                    .callbackData("/logout")
                    .build());

            if (!actionRow.isEmpty()) {
                rows.add(actionRow);
            }
            rows.add(logoutRow);
        } else {
            // Если пользователь не вошел, добавляем кнопку входа
            List<InlineKeyboardButton> loginRow = new ArrayList<>();
            loginRow.add(InlineKeyboardButton.builder()
                    .text("Войти в систему")
                    .callbackData("/login")
                    .build());
            rows.add(loginRow);
        }

        // Устанавливаем клавиатуру
        keyboardMarkup.setKeyboard(rows);
        message.setReplyMarkup(keyboardMarkup);
    }

    public void sendTelegramBotRequestListToManager(long chatId, List<TelegramBotRequest> requests, User user) {
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

        sendTelegramMessage(chatId, message.toString(), user);
    }
}