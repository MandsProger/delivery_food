package com.springLesson.WebSpringLesson.request;

import com.springLesson.WebSpringLesson.models.enums.Gender;
import com.springLesson.WebSpringLesson.models.enums.Role;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public class UserEditRequest {
    @NotBlank
    private String name;
    private String email;
    private Integer bonus;
    private Gender gender;
    private Set<Role> roles;
    private String password;
    private Boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getBonus() {
        return bonus;
    }

    public void setBonus(Integer bonus) {
        this.bonus = bonus;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
