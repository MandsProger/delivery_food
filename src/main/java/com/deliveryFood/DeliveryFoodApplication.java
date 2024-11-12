package com.deliveryFood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DeliveryFoodApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryFoodApplication.class, args);
	}

}
