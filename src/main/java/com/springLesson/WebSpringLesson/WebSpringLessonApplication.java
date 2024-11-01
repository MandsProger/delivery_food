package com.springLesson.WebSpringLesson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WebSpringLessonApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebSpringLessonApplication.class, args);
	}

}
