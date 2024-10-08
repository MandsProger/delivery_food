package com.springLesson.WebSpringLesson.models;

import com.springLesson.WebSpringLesson.models.enums.Gender;
import com.springLesson.WebSpringLesson.models.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", schema = "food")
@Setter @Getter
public class User implements UserDetails{

    @Id
    @Column(name = "number_phone")
    @NotNull
    private Long numberPhone;

    @Column(name = "name")
    @NotNull
    @Pattern(regexp = "^[a-zA-Zа-яА-Я\s]{1,50}$", message = "Имя должно быть только на англ. и русском языке и без цифр")
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

    @Override
    public String getUsername() {
        return name;
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

    public void setNumberPhoneException(Long numberPhone) {
        String phoneStr = String.valueOf(numberPhone);
        if (phoneStr.length() == 11 && phoneStr.startsWith("7")) {
            this.numberPhone = numberPhone;
        } else {
            throw new IllegalArgumentException("Номер телефона должен начинаться с 7 и содержать 11 цифр.");
        }
    }
}
