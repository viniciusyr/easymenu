package com.easymenu.order;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<OrderResponseDto> addOrder(@RequestBody @Valid OrderRecordDto order){
        OrderResponseDto newOrder = orderService.createOrder(order);
        newOrder.add(linkTo(methodOn(OrderController.class).getOneOrder(newOrder.getOrderId())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(){
        List<OrderResponseDto> ordersList = orderService.getAllOrders();
        if(ordersList.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        ordersList.forEach(order ->
                order.add(linkTo(methodOn(OrderController.class)
                        .getOneOrder(order.getOrderId())).withSelfRel()));

        return ResponseEntity.ok(ordersList);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponseDto> getOneOrder(@PathVariable(value="id") UUID orderId) {
        OrderResponseDto order = orderService.getOrderById(orderId);
        order.add(linkTo(methodOn(OrderController.class).getOneOrder(orderId)).withSelfRel());
        return ResponseEntity.ok(order);
    }

    @PutMapping("/orders/{id}")
    public ResponseEntity<OrderResponseDto> updateOrder(@RequestBody @Valid OrderUpdateDto orderUpdateDto, @PathVariable(value="id") UUID orderId){
        OrderResponseDto updateOrder = orderService.updateOrder(orderUpdateDto, orderId);
        return ResponseEntity.ok(updateOrder);
    }

}
