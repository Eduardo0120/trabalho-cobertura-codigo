package br.com.avaliacao.backend.dto;

public record OrderItemRequest(Long productId, int quantity) {
}
