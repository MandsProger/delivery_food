package com.springLesson.WebSpringLesson.services;

import com.springLesson.WebSpringLesson.models.Menu;
import com.springLesson.WebSpringLesson.repository.MenuRepository;
import com.springLesson.WebSpringLesson.request.MenuEditRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    @Transactional
    public void saveMenus(List<Menu> menus) {
        menuRepository.saveAll(menus);
    }

    public List<Menu> findAllMenu() {return menuRepository.findAll();}

    public Menu findMenuByName(String name) {
        return menuRepository.findByName(name);
    }

    @Transactional
    public void delete(Long foodID) {
        menuRepository.deleteById(Math.toIntExact(foodID));
    }

    public Optional<Menu> findOptionalByMenuId(Long foodId) {return menuRepository.findOptionalByFoodId(foodId);}

    public Menu findByMenuId(Long foodId) {return menuRepository.findByFoodId(foodId);}

    public boolean existsMenuById(Integer foodId) {return menuRepository.existsById(foodId);}

    @Transactional
    public void menuEdit(Long foodId, MenuEditRequest menuEditRequest, MultipartFile image) throws IOException {
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
        if (image != null && !image.isEmpty()) {
            if (menu.getImagePath() != null && !menu.getImagePath().isEmpty()) {
                deleteImage(menu.getImagePath());
            }
            String imagePath = saveImage(image);
            menu.setImagePath(imagePath);
        }
        saveMenu(menu);
    }

    @Transactional
    private String saveImage(MultipartFile file) throws IOException {
        String uploadDir = "uploads";
        String absolutePath = new File("src/main/resources/static/" + uploadDir).getAbsolutePath();

        File dir = new File(absolutePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File destinationFile = new File(dir, filename);
        file.transferTo(destinationFile);
        return filename;
    }

    @Transactional
    private void deleteImage(String imagePath) {
        String uploadDir = "uploads";
        String absolutePath = new File("src/main/resources/static/" + uploadDir).getAbsolutePath();

        File fileToDelete = new File(absolutePath, imagePath);
        if (fileToDelete.exists()) {
            boolean deleted = fileToDelete.delete();
            if (!deleted) {
                System.err.println("Failed to delete old image: " + fileToDelete.getAbsolutePath());
            }
        }
    }
}
