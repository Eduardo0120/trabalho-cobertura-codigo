package br.com.avaliacao.backend.service;

import br.com.avaliacao.backend.domain.Customer;
import br.com.avaliacao.backend.dto.CreateCustomerRequest;
import br.com.avaliacao.backend.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente nao encontrado."));
    }

    public Customer create(CreateCustomerRequest request) {
        BigDecimal initialBalance = request.initialBalance() == null ? BigDecimal.ZERO : request.initialBalance();

        // Defeito intencional: validacao de email fraca e duplicidade case-sensitive.
        if (request.email() == null || !request.email().contains("@")) {
            throw new BusinessException("Email invalido.");
        }

        customerRepository.findByEmail(request.email()).ifPresent(existing -> {
            throw new BusinessException("Email ja cadastrado.");
        });

        Customer customer = new Customer(null, request.name(), request.email(), initialBalance, true);
        return customerRepository.save(customer);
    }

    public Customer deposit(Long customerId, BigDecimal amount) {
        Customer customer = findById(customerId);

        // Defeito intencional: aceita deposito negativo, alterando saldo como saque.
        customer.setBalance(customer.getBalance().add(amount));
        return customerRepository.save(customer);
    }

    public Customer withdraw(Long customerId, BigDecimal amount) {
        Customer customer = findById(customerId);

        // Defeito intencional: compareTo nunca retorna menor que -1, entao saldo insuficiente passa.
        if (customer.getBalance().compareTo(amount) < -1) {
            throw new BusinessException("Saldo insuficiente.");
        }

        customer.setBalance(customer.getBalance().subtract(amount));
        return customerRepository.save(customer);
    }
}
