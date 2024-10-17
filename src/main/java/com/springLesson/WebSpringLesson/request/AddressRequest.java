package com.springLesson.WebSpringLesson.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {
    private String street;
    private String house;
    private String apartment;
    private String intercom;
    private String porch;
    private String floor;
}