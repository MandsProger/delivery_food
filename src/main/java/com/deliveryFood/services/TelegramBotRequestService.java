package com.deliveryFood.services;

import com.deliveryFood.models.TelegramBotRequest;
import com.deliveryFood.models.User;
import com.deliveryFood.repository.TelegramBotRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramBotRequestService {

    private final TelegramBotRequestRepository telegramBotRequestRepository;

    @Transactional
    public TelegramBotRequest createRequest(User user, String message) {
        TelegramBotRequest request = new TelegramBotRequest();
        request.setUserId(user.getNumberPhone());
        request.setMessage(message);
        return telegramBotRequestRepository.save(request);
    }

    public List<TelegramBotRequest> getRequestsByUser(User user) {
        return telegramBotRequestRepository.findByUserId(user.getNumberPhone());
    }

    public List<TelegramBotRequest> getAllRequests() {
        return telegramBotRequestRepository.findAll();
    }
}