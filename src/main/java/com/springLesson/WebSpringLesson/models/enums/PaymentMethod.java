package com.springLesson.WebSpringLesson.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum PaymentMethod implements GrantedAuthority {
    CARD, CASH;

    @Override
    public String getAuthority() {
        return name();
    }
}
