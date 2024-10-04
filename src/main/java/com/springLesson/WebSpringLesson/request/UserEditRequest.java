package com.springLesson.WebSpringLesson.request;

import com.springLesson.WebSpringLesson.models.enums.Gender;
import com.springLesson.WebSpringLesson.models.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter @Getter
public class UserEditRequest {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]{1,50}$")
    private String name;

    @Email
    @NotNull
    private String email;

    @NotNull
    private Integer bonus;

    @NotNull
    private Gender gender;

    @NotNull
    private Set<Role> roles;

    @NotNull
    private String password;

    @NotNull
    private Boolean active;
}
