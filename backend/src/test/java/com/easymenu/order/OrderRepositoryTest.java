package com.easymenu.order;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OrderRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void getLastOrderNumber() {
        OrderModel order1 = createOrder(100L);
        OrderModel order2 = createOrder(200L);

        Optional<Long> lastOrderNumber = orderRepository.getLastOrderNumber();

        assertThat(lastOrderNumber).isPresent();
        assertThat(lastOrderNumber.get()).isEqualTo(200L);
    }

    private OrderModel createOrder(Long orderNumber) {
        OrderModel newOrder = new OrderModel();
        newOrder.setOrderNumber(orderNumber);
        this.entityManager.persist(newOrder);
        this.entityManager.flush();
        return newOrder;
    }
}