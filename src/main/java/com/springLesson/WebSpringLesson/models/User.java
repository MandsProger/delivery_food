package com.springLesson.WebSpringLesson.models;

import com.springLesson.WebSpringLesson.models.enums.Gender;
import com.springLesson.WebSpringLesson.models.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", schema = "food")
@Data
public class User implements UserDetails {

    @Id
    @Column(name = "number_phone")
    private Long numberPhone;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "email")
    private String email;

    @Column(name = "bonus")
    private int bonus;

    @Column(name = "active")
    private boolean active;

    @Column(name = "password")
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
        return email;
    }

    public User(Builder builder) {
        this.numberPhone = builder.numberPhone;
        this.name = builder.name;
        this.gender = builder.gender;
        this.bonus = builder.bonus;
        this.email = builder.email;
        this.password = builder.password;
        this.roles = builder.roles;
        this.active = builder.active;
    }

    public static class Builder {
        private Long numberPhone;
        private String name;
        private Gender gender;
        private int bonus;
        private String email;
        private String password;
        private Set<Role> roles;
        private boolean active;


        public User.Builder withNumberPhone(Long numberPhone) {
            this.numberPhone = numberPhone;
            return this;
        }

        public User.Builder withName(String name) {
            this.name = name;
            return this;
        }

        public User.Builder withGender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public User.Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public User.Builder withBonus(int bonus) {
            this.bonus = bonus;
            return this;
        }

        public User.Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public User.Builder withRole(Set<Role> roles) {
            this.roles = roles;
            return this;
        }

        public User.Builder withActive(boolean active) {
            this.active = active;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
