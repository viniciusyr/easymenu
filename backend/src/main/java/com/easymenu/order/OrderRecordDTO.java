package com.easymenu.order;


import java.util.List;
import java.util.UUID;

public record OrderRecordDTO(UUID userId,
                             List<UUID> productsId,
                             String observation) {
}
