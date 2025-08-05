package com.easymenu.order;

import com.easymenu.order.exceptions.OrderException;
import com.easymenu.product.ProductModel;
import com.easymenu.redis.RedisService;
import com.easymenu.user.UserModel;
import com.easymenu.product.ProductRepository;
import com.easymenu.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.time.Duration;
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
    private final RedisService redisService;
    private final String ORDER_CACHE_KEY = "ORDER_CACHE::";

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            ProductRepository productRepository,
                            OrderFactory orderFactory, RedisService redisService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderFactory = orderFactory;
        this.redisService = redisService;
    }


    @Override
    @CachePut(value = "ORDER_PRODUCT", key="#result.id")
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
    @CachePut(value="ORDER_CACHE", key="#result.id")
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
        String cacheKey = ORDER_CACHE_KEY + orderId;
        return redisService.get(cacheKey, OrderResponseDTO.class)
                .orElseGet(() -> {
                    OrderResponseDTO order = orderRepository.findById(orderId)
                            .map(orderFactory::toResponseDto)
                            .orElseThrow(() -> new OrderException.OrderNotFoundException("Order not found: " + orderId));
                    redisService.set(cacheKey, order, Duration.ofMinutes(10));
                    return order;

                });
    }


    @Override
    public List<OrderResponseDTO> getAllOrders() {
        return redisService.getList(ORDER_CACHE_KEY, OrderResponseDTO.class)
                .orElseGet(() -> {
                    List<OrderResponseDTO> orders = orderRepository.findAll()
                            .stream().map(orderFactory::toResponseDto).toList();

                    redisService.set(ORDER_CACHE_KEY, orders, Duration.ofMinutes(10));
                    log.info("Total Orders found: {}", orders.size());
                    return orders;
                });
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

        if (searchDto.startDate() != null && searchDto.endDate() != null) {
            spec = spec.and(OrderSpecs.betweenDates(searchDto.startDate(), searchDto.endDate()));
        } else if (searchDto.startDate() != null) {
            spec = spec.and(OrderSpecs.createdAfter(searchDto.startDate()));
        } else if (searchDto.endDate() != null) {
            spec = spec.and(OrderSpecs.createdBefore(searchDto.endDate()));
        }

        if (searchDto.updatedOn() != null) {
            spec = spec.and(OrderSpecs.updatedOn(searchDto.updatedOn()));
        }

        List<OrderModel> result = orderRepository.findAll(spec);

        return new PageImpl<>(new ArrayList<>(result.stream().map(orderFactory::toResponseDto).toList()));
    }

}
