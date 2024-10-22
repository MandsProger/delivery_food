package com.springLesson.WebSpringLesson.request;

import com.springLesson.WebSpringLesson.models.ContentOrder;
import com.springLesson.WebSpringLesson.models.enums.PaymentMethod;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class OrderPayRequest {

    @NotNull
    private Long id;

    @NotNull
    private Long userId;

    private Set<ContentOrder> contentOrders = new HashSet<>();

    @NotNull
    private float resultPrice;

    private PaymentMethod paymentMethod;

    @NotNull
    private boolean isPaid;

    private LocalDateTime orderCompletionTime;

    private float costDelivery;

    private String feedback;

    @NotNull
    private String orderAddress;
    private AddressRequest address;
}
