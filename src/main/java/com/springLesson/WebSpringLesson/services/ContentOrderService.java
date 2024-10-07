package com.springLesson.WebSpringLesson.services;

import com.springLesson.WebSpringLesson.models.ContentOrder;
import com.springLesson.WebSpringLesson.models.Menu;
import com.springLesson.WebSpringLesson.repo.ContentOrderRepository;
import com.springLesson.WebSpringLesson.repo.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentOrderService {

    private final MenuRepository menuRepository;
    private final ContentOrderRepository contentOrderRepository;

    @Transactional
    public void addProductToCart(Long foodId, int count, Long numberPhone) {
        Menu menu = menuRepository.findByFoodId(foodId);
        if (menu.getRemainder() < count) {
            throw new IllegalArgumentException("Недостаточно товара на складе.");
        }

        ContentOrder contentOrder = new ContentOrder();
        contentOrder.setFoodName(menu.getName());
        contentOrder.setCount(count);
        contentOrder.setPrice(menu.getPrice() * count);
        contentOrder.setUserId(numberPhone);

        contentOrderRepository.save(contentOrder);
        menu.setRemainder(menu.getRemainder() - count);
        menuRepository.save(menu);
    }

    public List<ContentOrder> getUserCart(Long userId) {
        return contentOrderRepository.findAllByUserId(userId);
    }
}
