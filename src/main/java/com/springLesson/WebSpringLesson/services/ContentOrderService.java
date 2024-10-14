package com.springLesson.WebSpringLesson.services;

import com.springLesson.WebSpringLesson.models.ContentOrder;
import com.springLesson.WebSpringLesson.models.Menu;
import com.springLesson.WebSpringLesson.repo.ContentOrderRepository;
import com.springLesson.WebSpringLesson.repo.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public List<ContentOrder> getUserCart(Long numberPhone) {
        return contentOrderRepository.findAllByUserId(numberPhone);
    }

    public ContentOrder getUserById(Long id) {
        return contentOrderRepository.findByUserId(id);
    }


    public void contentOrderDelete(Long id) {
        Optional<ContentOrder> contentOrder = contentOrderRepository.findById(id);
        ContentOrder contentOrder1 = contentOrder.get();
        Menu menu = menuRepository.findByName(contentOrder1.getFoodName());
        menu.setRemainder(menu.getRemainder() + contentOrder1.getCount());
        contentOrderRepository.deleteById(id);
    }

}
