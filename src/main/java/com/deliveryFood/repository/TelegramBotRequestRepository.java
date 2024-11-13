package com.deliveryFood.repository;

import com.deliveryFood.models.TelegramBotRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelegramBotRequestRepository extends JpaRepository<TelegramBotRequest, Long> {
    List<TelegramBotRequest> findByUserId(Long userId);
}