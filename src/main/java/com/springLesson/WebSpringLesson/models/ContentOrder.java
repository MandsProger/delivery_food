package com.springLesson.WebSpringLesson.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "content_order", schema = "food")
@Setter
@Getter
public class ContentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "food_name")
    private String foodName;

    @NotNull
    @Column(name = "count")
    private int count;

    @NotNull
    @Column(name = "price")
    private float price;

    @NotNull
    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
