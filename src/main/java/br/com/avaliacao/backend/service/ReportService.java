package br.com.avaliacao.backend.service;

import br.com.avaliacao.backend.domain.Order;
import br.com.avaliacao.backend.repository.CustomerRepository;
import br.com.avaliacao.backend.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public ReportService(CustomerRepository customerRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    public Map<String, Object> summary() {
        Map<String, Object> report = new HashMap<>();
        report.put("customers", customerRepository.findAll().size());
        report.put("orders", orderRepository.findAll().size());
        report.put("revenue", orderRepository.findAll().stream()
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        report.put("internalNote", "Relatorio administrativo sem autenticacao.");
        return report;
    }
}
