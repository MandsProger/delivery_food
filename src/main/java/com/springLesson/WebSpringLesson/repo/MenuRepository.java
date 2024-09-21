package com.springLesson.WebSpringLesson.repo;

import com.springLesson.WebSpringLesson.models.Menu;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends CrudRepository<Menu, Integer> {

}
