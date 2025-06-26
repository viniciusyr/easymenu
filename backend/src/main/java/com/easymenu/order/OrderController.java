package com.easymenu.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
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

    @GetMapping("/orders/search")
    public ResponseEntity<Page<OrderResponseDTO>> getOrderByCriteria(@RequestBody OrderSearchDTO orderSearchDTO, Pageable pageable) throws JsonProcessingException {
        Page<OrderResponseDTO> orders = orderService.findByCriteria(orderSearchDTO, pageable);
        orders.forEach(order ->
                order.add(linkTo(methodOn(OrderController.class).getOneOrder(order.getOrderId())).withSelfRel()));
        OrderResponseDTO first = orders.getContent().get(0);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(first);
        System.out.println(json);
        return ResponseEntity.ok(new PageImpl<>(List.of(first)));
    }


    @PutMapping("/orders/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(@RequestBody @Valid OrderUpdateDTO orderUpdateDto, @PathVariable(value="id") UUID orderId){
        OrderResponseDTO updateOrder = orderService.updateOrder(orderUpdateDto, orderId);
        return ResponseEntity.ok(updateOrder);
    }

}
