package br.com.avaliacao.backend.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Coupon {

    private String code;
    private BigDecimal discountPercent;
    private LocalDate expiresAt;
    private boolean active;

    public Coupon(String code, BigDecimal discountPercent, LocalDate expiresAt, boolean active) {
        this.code = code;
        this.discountPercent = discountPercent;
        this.expiresAt = expiresAt;
        this.active = active;
    }

    public String getCode() {
        return code;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public LocalDate getExpiresAt() {
        return expiresAt;
    }

    public boolean isActive() {
        return active;
    }
}
