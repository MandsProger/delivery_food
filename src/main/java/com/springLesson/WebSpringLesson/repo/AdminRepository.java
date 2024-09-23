package com.springLesson.WebSpringLesson.repo;

import com.springLesson.WebSpringLesson.models.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Menu, Integer> {

}
