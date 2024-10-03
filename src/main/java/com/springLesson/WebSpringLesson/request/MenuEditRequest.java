package com.springLesson.WebSpringLesson.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class MenuEditRequest {

    @NotNull
    private long foodId;

    @NotNull
    private int price;

    @NotNull
    private int remainder;

    @NotNull
    @Pattern(regexp = "^[а-яА-Я]{1,50}$")
    private String name;

    @NotNull
    @Pattern(regexp = "^[а-яА-Я]{1,50}$")
    private String category;

    @Pattern(regexp = "^[а-яА-Я]{1,50}$")
    private String description;

    @NotNull
    private String volume;
}
