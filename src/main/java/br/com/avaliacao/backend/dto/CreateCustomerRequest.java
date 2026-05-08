package br.com.avaliacao.backend.dto;

import java.math.BigDecimal;

public record CreateCustomerRequest(String name, String email, BigDecimal initialBalance) {
}
