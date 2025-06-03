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
    public ResponseEntity<OrderResponseDTO> addOrder(@RequestBody @Valid OrderRecordDTO order){
        OrderResponseDTO newOrder = orderService.createOrder(order);
        newOrder.add(linkTo(methodOn(OrderController.class).getOneOrder(newOrder.getOrderId())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders(){
        List<OrderResponseDTO> ordersList = orderService.getAllOrders();
        if(ordersList.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        ordersList.forEach(order ->
                order.add(linkTo(methodOn(OrderController.class)
                        .getOneOrder(order.getOrderId())).withSelfRel()));

        return ResponseEntity.ok(ordersList);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponseDTO> getOneOrder(@PathVariable(value="id") UUID orderId) {
        OrderResponseDTO order = orderService.getOrderById(orderId);
        order.add(linkTo(methodOn(OrderController.class).getOneOrder(orderId)).withSelfRel());
        return ResponseEntity.ok(order);
    }

    @PutMapping("/orders/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(@RequestBody @Valid OrderUpdateDTO orderUpdateDto, @PathVariable(value="id") UUID orderId){
        OrderResponseDTO updateOrder = orderService.updateOrder(orderUpdateDto, orderId);
        return ResponseEntity.ok(updateOrder);
    }

}
