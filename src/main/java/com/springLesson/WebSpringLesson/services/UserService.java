package com.springLesson.WebSpringLesson.services;

import com.springLesson.WebSpringLesson.models.User;
import com.springLesson.WebSpringLesson.models.enums.Role;
import com.springLesson.WebSpringLesson.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean creatUser(User user) {
        String email = user.getEmail();
        if (userRepository.findByEmail(email) != null) return false;
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
        log.info("Saving new User with email {}", email);
        userRepository.save(user);
        return true;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
