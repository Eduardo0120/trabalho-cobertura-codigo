package br.com.avaliacao.backend.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private Long id;
    private Long customerId;
    private List<OrderItem> items;
    private BigDecimal total;
    private String couponCode;
    private String status;
    private LocalDateTime createdAt;

    public Order(Long id, Long customerId, List<OrderItem> items, BigDecimal total, String couponCode, String status, LocalDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.total = total;
        this.couponCode = couponCode;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
