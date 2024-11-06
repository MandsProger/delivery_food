package com.springLesson.WebSpringLesson.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "warehouse", schema = "food")
@Setter
@Getter
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Column(name = "warehouse_id", nullable = false, unique = true)
    private Long warehouseId;

    @NotNull(message = "Food name cannot be null")
    @Column(name = "food_name")
    private String foodName;

    @NotNull(message = "Count cannot be null")
    @Column(name = "count")
    private int count;

    @Column(nullable = false, updatable = false, name = "delivery_date")
    @NotNull
    private LocalDateTime delivery_date;

    @PrePersist
    private void init() {
        this.delivery_date = LocalDateTime.now();
    }
}