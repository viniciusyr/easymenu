package com.easymenu.repositories;

import com.easymenu.models.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, UUID> {
    @Query("SELECT MAX(o.orderNumber) FROM OrderModel o")
    Optional<Long> getLastOrderNumber();
}
