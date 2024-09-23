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

    public Menu(Builder builder) {
        this.foodId = builder.foodId;
        this.name = builder.name;
        this.price = builder.price;
        this.category = builder.category;
        this.remainder = builder.remainder;
        this.description = builder.description;
        this.volume = builder.volume;
    }

    public static class Builder {
        private int foodId;
        private String name;
        private int price;
        private String category;
        private int remainder;
        private String description;
        private String volume;

        public Builder withFoodId(int foodId) {
            this.foodId = foodId;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withPrice(int price) {
            this.price = price;
            return this;
        }

        public Builder withCategory(String category) {
            this.category = category;
            return this;
        }

        public Builder withRemainder(int remainder) {
            this.remainder = remainder;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withVolume(String volume) {
            this.volume = volume;
            return this;
        }

        public Menu build() {
            return new Menu(this);
        }
    }

}
