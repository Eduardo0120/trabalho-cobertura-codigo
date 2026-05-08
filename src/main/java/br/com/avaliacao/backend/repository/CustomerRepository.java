package br.com.avaliacao.backend.repository;

import br.com.avaliacao.backend.domain.Customer;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class CustomerRepository {

    private final List<Customer> customers = new CopyOnWriteArrayList<>();

    public CustomerRepository() {
        customers.add(new Customer(1L, "Ana Silva", "ana@email.com", new BigDecimal("250.00"), true));
        customers.add(new Customer(2L, "Bruno Lima", "bruno@email.com", new BigDecimal("50.00"), true));
        customers.add(new Customer(3L, "Cliente Inativo", "inativo@email.com", new BigDecimal("100.00"), false));
    }

    public List<Customer> findAll() {
        return new ArrayList<>(customers);
    }

    public Optional<Customer> findById(Long id) {
        return customers.stream().filter(customer -> customer.getId().equals(id)).findFirst();
    }

    public Optional<Customer> findByEmail(String email) {
        return customers.stream().filter(customer -> customer.getEmail().equals(email)).findFirst();
    }

    public Customer save(Customer customer) {
        if (customer.getId() == null) {
            Long nextId = customers.stream()
                    .map(Customer::getId)
                    .max(Comparator.naturalOrder())
                    .orElse(0L) + 1;
            Customer newCustomer = new Customer(nextId, customer.getName(), customer.getEmail(), customer.getBalance(), customer.isActive());
            customers.add(newCustomer);
            return newCustomer;
        }

        customers.removeIf(existing -> existing.getId().equals(customer.getId()));
        customers.add(customer);
        return customer;
    }
}
