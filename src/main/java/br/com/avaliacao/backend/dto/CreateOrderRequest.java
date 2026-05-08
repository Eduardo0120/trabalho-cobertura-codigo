package br.com.avaliacao.backend.dto;

import java.util.List;

public record CreateOrderRequest(Long customerId, List<OrderItemRequest> items, String couponCode) {
}
