package com.easymenu.order;

import com.easymenu.utils.PageResultDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Tag(name = "Order", description = "Operations related to customer orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Operation(summary = "Create Order", description = "Creates a new order and returns it with a self-link")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Order data to be created",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = OrderRecordDTO.class),
                    examples = @ExampleObject(value = """
                    {
                      "userId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
                      "products": [
                        {
                          "productId": "f47ac10b-58cc-4372-a567-0e02b2c3d470",
                          "quantity": 2
                        }
                      ],
                      "observation": "Deliver in the morning"
                    }
                    """)
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order successfully created"),
            @ApiResponse(responseCode = "403", description = "Validation error")
    })
    @PostMapping("/orders")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody @Valid OrderRecordDTO order){
        OrderResponseDTO newOrder = orderService.createOrder(order);
        newOrder.add(linkTo(methodOn(OrderController.class).getOneOrder(newOrder.getOrderId())).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }

    @Operation(summary = "List All Orders", description = "Returns all orders with HATEOAS links")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders found"),
            @ApiResponse(responseCode = "204", description = "No orders found")
    })
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

    @Operation(summary = "Get Order by ID", description = "Returns a single order by its ID")
    @Parameter(name = "id", description = "Order ID", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponseDTO> getOneOrder(@PathVariable(value="id") UUID orderId) {
        OrderResponseDTO order = orderService.getOrderById(orderId);
        order.add(linkTo(methodOn(OrderController.class).getOneOrder(orderId)).withSelfRel());
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Search Orders by Criteria", description = "Search orders by multiple optional filters with pagination")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Order search criteria",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = OrderSearchDTO.class),
                    examples = @ExampleObject(value = """
                    {
                      "orderId": null,
                      "orderNumber": null,
                      "userId": null,
                      "status": "DELIVERED",
                      "minAmount": 10.00,
                      "maxAmount": 100.00,
                      "observation": "urgent",
                      "startDate": "2025-01-01",
                      "endDate": "2025-12-31"
                    }
                    """)
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Filtered orders returned successfully")
    })
    @GetMapping("/orders/search")
    public ResponseEntity<PageResultDTO<OrderResponseDTO>> findOrderByCriteria(@RequestBody OrderSearchDTO orderSearchDTO, Pageable pageable){
        Page<OrderResponseDTO> orders = orderService.findByCriteria(orderSearchDTO, pageable);
        orders.forEach(order ->
                order.add(linkTo(methodOn(OrderController.class).getOneOrder(order.getOrderId())).withSelfRel()));
        return ResponseEntity.ok(PageResultDTO.result(orders));
    }

    @Operation(summary = "Update Order", description = "Updates an existing order by its ID")
    @Parameters({
            @Parameter(name = "id", description = "Order ID to update", required = true)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Updated order data",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = OrderUpdateDTO.class),
                    examples = @ExampleObject(value = """
                    {
                      "status": "CANCELLED",
                      "observation": "Customer requested cancellation",
                      "updatedOn": "2025-06-01T08:00:00Z"
                    }
                    """)
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "403", description = "Validation error")
    })
    @PutMapping("/orders/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(@RequestBody @Valid OrderUpdateDTO orderUpdateDto,
                                                        @PathVariable(value="id") UUID orderId){
        OrderResponseDTO updateOrder = orderService.updateOrder(orderUpdateDto, orderId);
        return ResponseEntity.ok(updateOrder);
    }
}
