package br.com.avaliacao.backend.repository;

import br.com.avaliacao.backend.domain.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class OrderRepository {

    private final List<Order> orders = new CopyOnWriteArrayList<>();

    public List<Order> findAll() {
        return new ArrayList<>(orders);
    }

    public Order save(Order order) {
        orders.add(order);
        return order;
    }

    public Long nextId() {
        return orders.stream()
                .map(Order::getId)
                .max(Comparator.naturalOrder())
                .orElse(0L) + 1;
    }
}
