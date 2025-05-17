package com.easymenu.dtos;

import com.easymenu.enums.OrderStatus;
import com.easymenu.models.ProductModel;

import java.util.List;

public record OrderUpdateDto(List<ProductModel> products,
                             String observation,
                             OrderStatus status) {
}
