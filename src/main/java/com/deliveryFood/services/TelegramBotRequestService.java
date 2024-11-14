package com.deliveryFood.services;

import com.deliveryFood.models.TelegramBotRequest;
import com.deliveryFood.models.User;
import com.deliveryFood.models.enums.Role;
import com.deliveryFood.repository.TelegramBotRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramBotRequestService {

    private final TelegramBotRequestRepository telegramBotRequestRepository;
    private final TelegramBotService telegramBotService;
    private final UserService userService;

    @Transactional
    public TelegramBotRequest createRequest(User user, String message) {
        TelegramBotRequest request = new TelegramBotRequest();
        request.setUserId(user.getNumberPhone());
        request.setMessage(message);

        TelegramBotRequest savedRequest = telegramBotRequestRepository.save(request);

        List<User> managers = userService.getUsersByRole(Role.ROLE_ADMIN);
        for (User manager : managers) {
            telegramBotService.sendTelegramBotRequestListToManager(manager.getNumberPhone(), List.of(savedRequest), manager);
        }

        return savedRequest;
    }

    public List<TelegramBotRequest> getRequestsByUser(User user) {
        return telegramBotRequestRepository.findByUserId(user.getNumberPhone());
    }

    public List<TelegramBotRequest> getAllRequests() {
        return telegramBotRequestRepository.findAll();
    }
}