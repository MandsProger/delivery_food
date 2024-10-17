package com.springLesson.WebSpringLesson.repo;

import com.springLesson.WebSpringLesson.models.ContentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ContentOrderRepository extends JpaRepository<ContentOrder, Long> {
    Set<ContentOrder> findAllByUserId(Long userId);
    ContentOrder findByUserId(Long userId);
    Optional<ContentOrder> findById(Long id);
    Set<ContentOrder> findAllByUserIdAndOrderIdIsNull(Long userId);
}
