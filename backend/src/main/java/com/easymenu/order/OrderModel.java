package com.easymenu.order;

import com.easymenu.order.enums.OrderStatus;
import com.easymenu.product.ProductModel;
import com.easymenu.user.UserModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "TB_ORDER")
public class OrderModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID orderId;

    @Column(unique = true, nullable = false)
    private Long orderNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )

    private List<ProductModel> products = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private BigDecimal totalAmount;
    private String observation;

    @CreationTimestamp
    private Instant createdOn;
    @UpdateTimestamp
    private Instant updatedOn;

    public OrderModel() {}

    public OrderModel(Long orderNumber, UserModel user, List<ProductModel> products, OrderStatus status, BigDecimal totalAmount, String observation) {
        this.orderNumber = orderNumber;
        this.user = user;
        this.products = products;
        this.status = status;
        this.totalAmount = totalAmount;
        this.observation = observation;

    }

}
