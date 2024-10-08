package com.springLesson.WebSpringLesson.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "menu", schema = "food")
@Setter @Getter
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "food_id")
    @NotNull
    private Long foodId;

    @Column(name = "price")
    @NotNull
    private float price;

    @Column(name = "remainder")
    @NotNull
    private int remainder;

    @Column(name = "name")
    @NotNull
    @Pattern(regexp = "^[a-zA-Zа-яА-Я\s]{1,100}$")
    private String name;

    @Column(name = "category")
    @NotNull
    @Pattern(regexp = "^[a-zA-Zа-яА-Я\s]{1,100}$")
    private String category;

    @Column(name = "description")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я\s]{0,250}$")
    private String description;

    @Column(name = "volume")
    @NotNull
    private String volume;

    public Menu() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return price == menu.price && remainder == menu.remainder && Objects.equals(foodId, menu.foodId)
                && Objects.equals(name, menu.name) && Objects.equals(category, menu.category)
                && Objects.equals(description, menu.description) && Objects.equals(volume, menu.volume);
    }


    @Override
    public int hashCode() {
        return Objects.hash(foodId, price, remainder, name, category, description, volume);
    }

    @Override
    public String toString() {
        return "Menu{" +
                "foodId=" + foodId +
                ", price=" + price +
                ", remainder=" + remainder +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", volume='" + volume + '\'' +
                '}';
    }
}
