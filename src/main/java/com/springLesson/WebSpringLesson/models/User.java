package com.springLesson.WebSpringLesson.models;

import com.springLesson.WebSpringLesson.models.enums.Gender;
import com.springLesson.WebSpringLesson.models.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", schema = "food")
public class User implements UserDetails{

    @Id
    @Column(name = "number_phone")
    @NotNull
    private Long numberPhone;

    @Column(name = "name")
    @NotNull
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]{1,50}$")
    private String name;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    @NotNull
    private Gender gender;

    @Column(name = "email")
    @Email
    @NotNull
    private String email;

    @Column(name = "bonus")
    @NotNull
    private int bonus;

    @Column(name = "active")
    @NotNull
    private boolean active;

    @Column(name = "password")
    @NotNull
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "number_phone"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false, updatable = false, name = "date_of_registration")
    private LocalDateTime date_of_registration;

    public User() {
    }

    @PrePersist
    private void init() {
       this.date_of_registration = LocalDateTime.now();
    }

    public boolean isAdmin(){return roles.contains(Role.ROLE_ADMIN);}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    public Long getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(Long numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
