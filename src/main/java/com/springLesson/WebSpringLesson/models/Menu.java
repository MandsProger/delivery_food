package com.springLesson.WebSpringLesson.models;

import jakarta.persistence.*;

@Entity
@Table(name = "menu", schema = "food")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "food_id")
    private long foodId;

    private int price,
            remainder;

    private String name,
            category,
            description,
            volume;

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

    public Menu(int price, int remainder, String name, String category, String description, String volume) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.remainder = remainder;
        this.description = description;
        this.volume = volume;
    }

    public Menu() {
    }
}
