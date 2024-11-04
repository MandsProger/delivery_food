package com.springLesson.WebSpringLesson.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {
    @NotNull
    private String street;
    @NotNull
    private String house;
    @NotNull
    private String apartment;
    private String intercom;
    private String porch;
    private String floor;
}