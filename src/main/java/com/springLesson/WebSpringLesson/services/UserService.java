package com.springLesson.WebSpringLesson.services;

import com.springLesson.WebSpringLesson.models.User;
import com.springLesson.WebSpringLesson.models.enums.Role;
import com.springLesson.WebSpringLesson.repo.UserRepository;
import com.springLesson.WebSpringLesson.request.UserEditRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User saveUser(User user){return userRepository.save(user);}

    public List<User> list() {
        return userRepository.findAll();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByNumberPhone(Long numberPhone){return userRepository.findByNumberPhone(numberPhone);}

    public void banUser(Long numberPhone) {
        User user = getUserByNumberPhone(numberPhone);
        if (user != null) {
            if (user.isActive()) {
                user.setActive(false);
                log.info("Ban user with id = {}", user.getNumberPhone());
            } else  {
                user.setActive(true);
                log.info("UnBan user with id = {}", user.getNumberPhone());
            }

        }
        saveUser(user);
    }

    public void createUser(User user) {
        String email = user.getEmail();
        Long numberPhone = user.getNumberPhone();
        if (userRepository.findByNumberPhone(numberPhone) != null) {
            throw new IllegalArgumentException("Пользователь с таким номером уже существует");
        }
        if (userRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }

        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
        log.info("Saving new User with email {}", email);
        saveUser(user);
    }

    private Long cleanPhoneNumber(String phone) {
        String phoneStr = phone.replaceAll("[^\\d]", "");
        phoneStr = phoneStr.replaceAll("[()]|-", "");
        return Long.parseLong(phoneStr);
    }

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
}