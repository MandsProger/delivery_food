package com.springLesson.WebSpringLesson.services;


import com.springLesson.WebSpringLesson.models.Menu;
import com.springLesson.WebSpringLesson.repo.MenuRepository;
import com.springLesson.WebSpringLesson.request.MenuEditRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    @Transactional
    public Menu saveMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    public List<Menu> findAllMenu() {return menuRepository.findAll();}

    @Transactional
    public void delete(Long foodID) {
        menuRepository.deleteById(Math.toIntExact(foodID));
    }

    public Optional<Menu> findOptionalByMenuId(Long foodId) {return menuRepository.findOptionalByFoodId(foodId);}

    public Menu findByMenuId(Long foodId) {return menuRepository.findByFoodId(foodId);}

    public boolean existsMenuById(Integer foodId) {return menuRepository.existsById(foodId);}

    @Transactional
    public void menuEdit(Long foodId, MenuEditRequest menuEditRequest) {
        Menu menu = findByMenuId(foodId);
        if (menu == null) {
            menu = new Menu();
        }
        menu.setName(menuEditRequest.getName());
        menu.setCategory(menuEditRequest.getCategory());
        menu.setDescription(menuEditRequest.getDescription());
        menu.setPrice(menuEditRequest.getPrice());
        menu.setRemainder(menuEditRequest.getRemainder());
        menu.setVolume(menuEditRequest.getVolume());
        saveMenu(menu);
    }
}
