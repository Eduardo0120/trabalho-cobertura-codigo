package br.com.avaliacao.backend.dto;

import java.math.BigDecimal;

public record BalanceOperationRequest(BigDecimal amount) {
}
