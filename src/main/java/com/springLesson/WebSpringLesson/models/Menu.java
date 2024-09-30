package com.springLesson.WebSpringLesson.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "menu", schema = "food")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "food_id")
    @NotNull
    private Long foodId;

    @Column(name = "price")
    @NotNull
    private int price;

    @Column(name = "remainder")
    @NotNull
    private int remainder;

    @Column(name = "name")
    @NotNull
    @Pattern(regexp = "^[а-яА-Я]{1,50}$")
    private String name;

    @Column(name = "category")
    @NotNull
    @Pattern(regexp = "^[а-яА-Я]{1,50}$")
    private String category;

    @Column(name = "description")
    @Pattern(regexp = "^[а-яА-Я]{1,50}$")
    private String description;

    @Column(name = "volume")
    @NotNull
    private String volume;

    public Menu() {
    }

    public long getFoodId() {
        return foodId;
    }

    public void setFoodId(long foodId) {
        this.foodId = foodId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRemainder() {
        return remainder;
    }

    public void setRemainder(int remainder) {
        this.remainder = remainder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

}
