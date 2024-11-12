package com.deliveryFood.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Gender implements GrantedAuthority {
    MALE, FEMALE;

    @Override
    public String getAuthority() {
        return name();
    }
}
