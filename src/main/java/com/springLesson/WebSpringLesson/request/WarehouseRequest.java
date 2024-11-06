package com.springLesson.WebSpringLesson.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class WarehouseRequest {

    private Long id;

    private List<String> product;

    private List<Integer> quantity;
}