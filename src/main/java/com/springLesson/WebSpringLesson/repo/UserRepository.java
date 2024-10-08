package com.springLesson.WebSpringLesson.repo;

import com.springLesson.WebSpringLesson.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByNumberPhone(Long number_phone);
}
