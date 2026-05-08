package br.com.avaliacao.backend.controller;

import br.com.avaliacao.backend.domain.Customer;
import br.com.avaliacao.backend.dto.BalanceOperationRequest;
import br.com.avaliacao.backend.dto.CreateCustomerRequest;
import br.com.avaliacao.backend.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> findAll() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public Customer findById(@PathVariable Long id) {
        return customerService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer create(@RequestBody CreateCustomerRequest request) {
        return customerService.create(request);
    }

    @PostMapping("/{id}/deposit")
    public Customer deposit(@PathVariable Long id, @RequestBody BalanceOperationRequest request) {
        return customerService.deposit(id, request.amount());
    }

    @PostMapping("/{id}/withdraw")
    public Customer withdraw(@PathVariable Long id, @RequestBody BalanceOperationRequest request) {
        return customerService.withdraw(id, request.amount());
    }
}
