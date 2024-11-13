package com.deliveryFood.services;

import com.deliveryFood.models.User;
import com.deliveryFood.models.enums.Role;
import com.deliveryFood.repository.UserRepository;
import com.deliveryFood.request.UserEditRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User saveUser(User user){return userRepository.save(user);}

    public List<User> list() {
        return userRepository.findAll();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByNumberPhone(Long numberPhone){return userRepository.findByNumberPhone(numberPhone);}

    @Transactional
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public void banUser(Long numberPhone) {
        User user = getUserByNumberPhone(numberPhone);
        if (user != null) {
            if (user.isActive()) {
                user.setActive(false);
                log.info("Ban user with id = {}", user.getNumberPhone());
            } else {
                user.setActive(true);
                log.info("UnBan user with id = {}", user.getNumberPhone());
            }
        }
        saveUser(user);
    }

    @Transactional
    public void createUser(User user) {
        String email = user.getEmail();
        Long numberPhone = user.getNumberPhone();
        if (userRepository.findByNumberPhone(numberPhone) != null) {
            throw new IllegalArgumentException("Пользователь с таким номером уже существует");
        }
        if (userRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
        user.setNumberPhoneException(numberPhone);
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
        log.info("Saving new User with email {}", email);
        saveUser(user);
    }

    @Transactional
    public void userUpdate(Long numberPhone, UserEditRequest userEditRequest) {
        User user = getUserByNumberPhone(numberPhone);
        if (user != null) {
            user.setName(userEditRequest.getName());
            user.setEmail(userEditRequest.getEmail());
            user.setBonus(userEditRequest.getBonus());
            user.setGender(userEditRequest.getGender());
            user.setRoles(userEditRequest.getRoles());
            if (!userEditRequest.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userEditRequest.getPassword()));
            }
            saveUser(user);
        }
    }

    public boolean authenticateTelegramBot(String email, String password) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getEmail().equals(email) && passwordEncoder.matches(password, user.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public List<User> getUsersByRole(Role role) {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .filter(user -> user.getRoles().contains(role))
                .collect(Collectors.toList());
    }
}