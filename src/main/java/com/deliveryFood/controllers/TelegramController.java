package com.deliveryFood.controllers;


import com.deliveryFood.services.TelegramCommandService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/telegram")
public class TelegramController {

    private final TelegramCommandService telegramCommandService;

    public TelegramController(TelegramCommandService telegramCommandService) {
        this.telegramCommandService = telegramCommandService;
    }

    @PostMapping("/send-command")
    public String sendCommand(@RequestBody CommandRequest commandRequest) {
        // Здесь вы можете передать команду боту и получить ответ
        return telegramCommandService.processCommand(
                commandRequest.getCommand(),
                commandRequest.getUserName(),
                commandRequest.getChatId(),
                null, // email
                null  // password
        );
    }
}


class CommandRequest {
    private String command;
    private String userName;
    private Long chatId;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
}