package com.easymenu.order;

import com.easymenu.order.enums.OrderStatus;
import com.easymenu.product.ProductModel;

import java.util.List;

public record OrderUpdateDTO(List<ProductModel> products,
                             String observation,
                             OrderStatus status) {
}
