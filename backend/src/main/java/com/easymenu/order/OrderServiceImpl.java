package com.easymenu.order;

import com.easymenu.order.exceptions.OrderException;
import com.easymenu.product.ProductModel;
import com.easymenu.user.UserModel;
import com.easymenu.product.ProductRepository;
import com.easymenu.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderFactory orderFactory;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            ProductRepository productRepository,
                            OrderFactory orderFactory) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderFactory = orderFactory;
    }


    @Override
    public OrderResponseDTO createOrder(OrderRecordDTO orderDto) {

        if(orderDto == null){
            throw new OrderException.OrderNotFoundException("OrderDto is null or not found");
        }

        UserModel user = userRepository.findById(orderDto.userId())
                .orElseThrow(() -> new OrderException.UserNotFoundException("The user not found"));

        List<ProductModel> products = productRepository.findAllById(orderDto.productsId());
        if(products.size() != orderDto.productsId().size()){
            throw new OrderException.ProductNotFoundException("One or more products not found");
        }

        long lastOrderNumber = orderRepository.getLastOrderNumber().orElse(0L);
        long newOrderNumber = lastOrderNumber + 1;

        BigDecimal totalAmount = products.stream()
                .map(ProductModel::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderModel newOrder = orderFactory.createOrder(newOrderNumber,
                user,
                products,
                totalAmount,
                orderDto.observation());

        OrderModel savedOrder = orderRepository.save(newOrder);

        log.info("Order created ID: {}", newOrder.getOrderId());

        return orderFactory.toResponseDto(savedOrder);
    }

    @Override
    public OrderResponseDTO updateOrder(OrderUpdateDTO orderDto, UUID orderId) {
        OrderModel existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException.UpdateNotFoundException("Order not found!"));

        orderFactory.updateOrder(orderDto, existingOrder);

        OrderModel savedOrder = orderRepository.save(existingOrder);

        log.info("Order updated successfully: {}", savedOrder.getOrderId());

        return orderFactory.toResponseDto(savedOrder);
    }

    @Override
    public OrderResponseDTO getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    log.info("Order found by ID: {}", order.getOrderNumber());
                    return orderFactory.toResponseDto(order);
                })
                .orElseThrow(() -> new OrderException.OrderNotFoundException("Order not found by ID: " + orderId));
    }


    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<OrderModel> orders = orderRepository.findAll();
        log.info("Total Orders found: {}", orders.size());
        return orders.stream()
                .map(orderFactory::toResponseDto)
                .toList();
    }

    @Override
    public Page<OrderResponseDTO> findByCriteria(OrderSearchDTO searchDto, Pageable pageable) {
        if(searchDto == null) {
            throw new OrderException.FilterNotFoundException("SearchDTO is null");
        }

        Specification<OrderModel> spec = Specification.where(null);

        if(searchDto.orderId() != null) {
            spec = spec.and(OrderSpecs.hasId(searchDto.orderId()));
        }

        if(searchDto.orderNumber() != null) {
            spec = spec.and(OrderSpecs.hasOrderNumber(searchDto.orderNumber()));
        }

        if(searchDto.userId() != null) {
           spec = spec.and(OrderSpecs.hasUserId(searchDto.userId()));
        }

        if(searchDto.status() != null){
            spec = spec.and(OrderSpecs.inStatus(searchDto.status()));
        }

        if(searchDto.minAmount() != null && searchDto.maxAmount() != null){
            spec = spec.and(OrderSpecs.betweenAmount(searchDto.minAmount(), searchDto.maxAmount()));
        }

        if(searchDto.observation() != null){
            spec = spec.and(OrderSpecs.containsObservation(searchDto.observation()));
        }

        if(searchDto.startDate() != null && searchDto.endDate() != null) {
            spec = spec.and(OrderSpecs.betweenDates(searchDto.startDate(), searchDto.endDate()));
        }

        List<OrderModel> result = orderRepository.findAll(spec);

        return new PageImpl<>(new ArrayList<>(result.stream().map(orderFactory::toResponseDto).toList()));
    }

}
