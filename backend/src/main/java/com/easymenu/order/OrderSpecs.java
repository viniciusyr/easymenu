package com.easymenu.order;

import com.easymenu.order.enums.OrderStatus;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class OrderSpecs {
    public static Specification<OrderModel> hasId(UUID providedId){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("orderId"), providedId);
    }

    public static Specification<OrderModel> hasOrderNumber(Long providedOrder){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("orderNumber"), providedOrder);
    }

    public static Specification<OrderModel> hasUserId(UUID providedUserId){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("id"), providedUserId);
    }

    public static Specification<OrderModel> inStatus(OrderStatus providedStatus){
       return (root, query, criteriaBuilder) ->
               criteriaBuilder.equal(root.get("orderStatus"), providedStatus);
    }

    public static Specification<OrderModel> betweenAmount(BigDecimal start, BigDecimal end){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("totalAmount"), start, end);
    }

    public static Specification<OrderModel> containsObservation(String providedObservation){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("observation")), "%" + providedObservation.toLowerCase() + "%");
    }

    public static Specification<OrderModel> betweenDates(LocalDate start,LocalDate end){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("createdOn"), start.atStartOfDay(), end.atTime(23,59, 59));
    }

}
