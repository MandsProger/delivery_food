package com.springLesson.WebSpringLesson.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter @Getter
public class MenuEditRequest {

    @NotNull
    private Long foodId;

    @NotNull
    private float price;

    @NotNull
    private int remainder;

    @NotNull
    @Pattern(regexp = "^[a-zA-Zа-яА-Я\s]{1,100}$")
    private String name;

    @NotNull
    @Pattern(regexp = "^[a-zA-Zа-яА-Я\s]{1,100}$")
    private String category;

    @Pattern(regexp = "^[a-zA-Zа-яА-Я\s]{0,250}$")
    private String description;

    @NotNull
    private String volume;

    @NotNull
    private MultipartFile image;
}
