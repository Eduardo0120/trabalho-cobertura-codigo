package br.com.avaliacao.backend.domain;

import java.math.BigDecimal;

public class Customer {

    private Long id;
    private String name;
    private String email;
    private BigDecimal balance;
    private boolean active;

    public Customer(Long id, String name, String email, BigDecimal balance, boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.balance = balance;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
