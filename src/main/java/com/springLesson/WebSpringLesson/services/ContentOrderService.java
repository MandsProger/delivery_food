package com.springLesson.WebSpringLesson.services;

import com.springLesson.WebSpringLesson.models.ContentOrder;
import com.springLesson.WebSpringLesson.models.Menu;
import com.springLesson.WebSpringLesson.repository.ContentOrderRepository;
import com.springLesson.WebSpringLesson.repository.MenuRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ContentOrderService {

    private final MenuRepository menuRepository;
    private final ContentOrderRepository contentOrderRepository;

    @Transactional
    public ContentOrder saveContentOrder(ContentOrder contentOrder){
        return contentOrderRepository.save(contentOrder);
    }

    @Transactional
    public void addProductToCart(Long foodId, int count, Long numberPhone) {
        Menu menu = menuRepository.findByFoodId(foodId);

        ContentOrder contentOrder = contentOrderRepository.findByFoodNameAndOrderIdIsNullAndUserId(menu.getName(), numberPhone);
        if (contentOrder == null) {
            ContentOrder newContentOrder = new ContentOrder();
            newContentOrder.setFoodName(menu.getName());
            newContentOrder.setCount(count);
            newContentOrder.setPrice(menu.getPrice() * count);
            newContentOrder.setUserId(numberPhone);
            saveContentOrder(newContentOrder);
        } else {
            if (menu.getRemainder() < contentOrder.getCount() + count) {
                throw new IllegalArgumentException("Недостаточно товара на складе.");
            }
            contentOrder.setCount(contentOrder.getCount() + count);
            contentOrder.setPrice(menu.getPrice() * contentOrder.getCount());
            saveContentOrder(contentOrder);
        }
    }

    public Set<ContentOrder> getAllUserCartByNumberPhone(Long numberPhone) {
        Set<ContentOrder> contentOrders = contentOrderRepository.findAllByUserIdAndOrderIdIsNull(numberPhone);
        return contentOrders;
    }

    public ContentOrder getUserById(Long id) {
        return contentOrderRepository.findByUserId(id);
    }


    public Set<ContentOrder> getAllItemsByOrderId(Long orderId) {
        return contentOrderRepository.findAllByOrderId(orderId);
    }

    @Transactional
    public void contentOrderDelete(Long id) {
        contentOrderRepository.deleteById(id);
    }

    @Transactional
    public void contentOrderMinus(Long id) {
        ContentOrder contentOrder = contentOrderRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Заказ с ID " + id + " не найден"));
        Menu menu = menuRepository.findByName(contentOrder.getFoodName());
        if (contentOrder.getCount() > 1) {
            contentOrder.setCount(contentOrder.getCount()-1);
            contentOrder.setPrice(contentOrder.getCount() * menu.getPrice());
            saveContentOrder(contentOrder);
        } else {
            contentOrderRepository.deleteById(id);
        }

    }

    @Transactional
    public void contentOrderPlus(Long id) {
        ContentOrder contentOrder = contentOrderRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Заказ с ID " + id + " не найден"));
        Menu menu = menuRepository.findByName(contentOrder.getFoodName());
        if (contentOrder.getCount() <= menu.getRemainder()-1) {
            contentOrder.setCount(contentOrder.getCount() + 1);
            contentOrder.setPrice(contentOrder.getCount() * menu.getPrice());
            saveContentOrder(contentOrder);
        }
    }
}
