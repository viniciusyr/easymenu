package com.easymenu.repositories;

import com.easymenu.order.OrderModel;
import com.easymenu.order.OrderRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OrderRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void getLastOrderNumber_ReturnsMaxOrderNumber() {

        // Arrange
        OrderModel order1 = createOrder(100L);
        OrderModel order2 = createOrder(200L);

        // Act
        Optional<Long> lastOrderNumber = orderRepository.getLastOrderNumber();

        // Assert
        assertThat(lastOrderNumber).isPresent();
        assertThat(lastOrderNumber.get()).isEqualTo(200L);
    }

    private OrderModel createOrder(Long orderNumber) {
        OrderModel newOrder = new OrderModel();
        newOrder.setOrderNumber(orderNumber);
        this.entityManager.persist(newOrder);
        return newOrder;
    }
}