package com.springLesson.WebSpringLesson.models;

import com.springLesson.WebSpringLesson.models.enums.PaymentMethod;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "`order`", schema = "food")
@Setter
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @NotNull
    private Long id;

    @Column(name = "user_id")
    @NotNull
    private Long userId;

    @Column(nullable = false, updatable = false, name = "date_order")
    @NotNull
    private LocalDateTime dateOrder;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Set<ContentOrder> contentOrders = new HashSet<>();

    @Column(name = "result_price")
    @NotNull
    private float resultPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "is_paid")
    @NotNull
    private boolean isPaid;

    @Column(name = "order_completion_time")
    private LocalDateTime orderCompletionTime;

    @Column(name = "cost_delivery")
    private float costDelivery;

    @Column(name = "feedback")
    private String feedback;

    @Column(name = "order_address")
    @NotNull
    private String orderAddress;

    @PrePersist
    private void init() {
        this.dateOrder = LocalDateTime.now();
    }
}
