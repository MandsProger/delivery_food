package com.springLesson.WebSpringLesson.services;


import com.springLesson.WebSpringLesson.models.Menu;
import com.springLesson.WebSpringLesson.repo.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    public Menu saveMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    public List<Menu> findAllMenu() {return menuRepository.findAll();}

    public void delete(Menu menu) {
        menuRepository.delete(menu);
    }

    public Optional<Menu> findOptionalByMenuId(Integer foodId) {return menuRepository.findById(foodId);}

    public Menu findByMenuId(Integer foodId) {return menuRepository.findById(foodId).orElseThrow();}

    public boolean existsMenuById(Integer foodId) {return menuRepository.existsById(foodId);}

    public void menuAddBuild(String name, int price, String category, int remainder,
                                String description, String volume) {
        Menu.Builder builder = new Menu.Builder();
        builder.withName(name)
                .withPrice(price)
                .withCategory(category)
                .withRemainder(remainder)
                .withDescription(description)
                .withVolume(volume);
        Menu menu = builder.build();
        saveMenu(menu);
    }

    public void menuEditBuild(Integer foodId, String name, int price, String category, int remainder,
                             String description, String volume) {
        Menu.Builder builder = new Menu.Builder();
        builder.withFoodId(foodId)
                .withName(name)
                .withPrice(price)
                .withCategory(category)
                .withRemainder(remainder)
                .withDescription(description)
                .withVolume(volume);
        Menu menu = builder.build();
        saveMenu(menu);
    }
}
