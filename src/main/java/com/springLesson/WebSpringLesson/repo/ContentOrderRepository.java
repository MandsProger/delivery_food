package com.springLesson.WebSpringLesson.repo;

import com.springLesson.WebSpringLesson.models.ContentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentOrderRepository extends JpaRepository<ContentOrder, Long> {
    List<ContentOrder> findAllByUserId(Long userId);
    ContentOrder findByUserId(Long id);
}
