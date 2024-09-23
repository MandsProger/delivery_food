package com.springLesson.WebSpringLesson.services;

import com.springLesson.WebSpringLesson.models.User;
import com.springLesson.WebSpringLesson.models.enums.Gender;
import com.springLesson.WebSpringLesson.models.enums.Role;
import com.springLesson.WebSpringLesson.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public String createUser(User user) {
        String email = user.getEmail();
        Long numberPhone = user.getNumberPhone();
        if (userRepository.findByNumberPhone(numberPhone) != null) {
            return "ErrorNumberPhone";
        }
        if (userRepository.findByEmail(email) != null) {
            return "ErrorEmail";
        }
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
        log.info("Saving new User with email {}", email);
        saveUser(user);
        return "done";
    }

    public void userUpdate(Long numberPhone, String name, String email, Gender gender,
                     int bonus, Set<Role> roles, String password) {

        User user = userRepository.findByNumberPhone(numberPhone);
        User.Builder builder = new User.Builder();
        builder.withNumberPhone(numberPhone)
                .withEmail(email)
                .withName(name)
                .withGender(gender)
                .withBonus(bonus)
                .withRole(roles);

        if (!password.isEmpty()) {
            builder.withPassword(passwordEncoder.encode(password));
        } else {
            String storedPassword = user.getPassword();
            builder.withPassword(storedPassword);
        }

        user = builder.build();
        saveUser(user);
    }
}
