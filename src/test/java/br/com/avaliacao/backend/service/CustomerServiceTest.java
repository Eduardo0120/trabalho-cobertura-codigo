package br.com.avaliacao.backend.service;

import br.com.avaliacao.backend.dto.CreateCustomerRequest;
import br.com.avaliacao.backend.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import br.com.avaliacao.backend.domain.Customer;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

class CustomerServiceTest {

    @Mock
    private CustomerRepository repository;

    private CustomerService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new CustomerService(repository);
    }
    @Test
    void deveRetornarEmailInvalido() {
        CreateCustomerRequest request =
         new CreateCustomerRequest(
                "Eduardo", "abc", BigDecimal.TEN
         );
    assertThrows(
            BusinessException.class,
            () -> service.create(request)
    );
    }

    @Test
    void deveRejeitarSaqueMaiorQueSaldo() {
        Customer customer = new Customer(
                1L, "Eduardo", "edu@gmail", BigDecimal.valueOf(100), true
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(customer));

        assertThrows(
                BusinessException.class,
                () -> service.withdraw(1L, BigDecimal.valueOf(500))
        );
    }
    @Test
    void deveRejeitarDepositoNegativo() {
        Customer customer = new Customer(
                1L, "Eduardo", "edu@gmail", BigDecimal.valueOf(100), true
        );
        when(repository.findById(1L))
                .thenReturn(Optional.of(customer));
        assertThrows(
                BusinessException.class,
                () -> service.deposit(1L, BigDecimal.valueOf(-50))
        );
    }
    @Test
    void deveBuscarClientePorIdComSucesso() {
        Customer customer = new Customer(
                1L, "Eduardo", "edu@gmail.com", BigDecimal.ZERO, true
        );
        when(repository.findById(1L))
                .thenReturn(Optional.of(customer));

        Customer result = service.findById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Eduardo", result.getName());
        assertEquals("edu@gmail.com", result.getEmail());
        assertEquals(BigDecimal.ZERO, result.getBalance());
        assertTrue(result.isActive());

        verify(repository).findById(1L);
    }
    @Test
    void deveLancarExcecaoQuandoClienteNaoExistir() {
        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> service.findById(99L)
        );
    }
    @Test
    void deveCriarClienteComSaldoZeroQuandoSaldoInicialForNulo() {
        CreateCustomerRequest request = new CreateCustomerRequest(
                "Eduardo", "edu@gmail.com", null
        );
        Customer customerSalvo = new Customer(
                1L, "Eduardo", "edu@gmail.com", BigDecimal.ZERO, true
        );

        when(repository.save(any(Customer.class)))
                .thenReturn(customerSalvo);

        Customer result = service.create(request);

        assertEquals("Eduardo", result.getName());
        assertEquals("edu@gmail.com", result.getEmail());
        assertEquals(BigDecimal.ZERO, result.getBalance());

        verify(repository).save(any(Customer.class));
    }
    @Test
    void deveCriarClienteComSucesso() {
        CreateCustomerRequest customer = new CreateCustomerRequest(
                "Eduardo", "edu@gmail.com", BigDecimal.ZERO
        );
        Customer customerSaved = new Customer(
                1L, "Eduardo", "edu@gmail.com", BigDecimal.ZERO, true
        );

        when(repository.save(any(Customer.class)))
                .thenReturn(customerSaved);

        Customer createdCustomer = service.create(customer);

        assertNotNull(createdCustomer);
        assertEquals(1L, createdCustomer.getId());
        assertEquals("Eduardo", createdCustomer.getName());
        assertEquals("edu@gmail.com", createdCustomer.getEmail());
        assertEquals(BigDecimal.ZERO, createdCustomer.getBalance());
        assertTrue(createdCustomer.isActive());

        verify(repository, times(1)).save(any(Customer.class));
    }
    @Test
    void deveRejeitarEmailDuplicado() {
        CreateCustomerRequest request = new CreateCustomerRequest(
                "Eduardo", "eduardo@gmail.com", BigDecimal.ZERO
        );
        Customer existingCustomer = new Customer(
                1L, "Eduardo", "eduardo@gmail.com", BigDecimal.ZERO, true
        );

        when(repository.findByEmail("eduardo@gmail.com"))
                .thenReturn(Optional.of(existingCustomer));

        assertThrows(
                BusinessException.class,
                () -> service.create(request)
        );

        verify(repository, never()).save(any(Customer.class));
    }
    @Test
    void deveRealizarDepositoComSucesso() {
        Customer customer = new Customer(
                1L, "Eduardo", "edu@gmail.com", BigDecimal.valueOf(100), true
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(repository.save(any(Customer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Customer result = service.deposit(1L, BigDecimal.valueOf(50));

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(150), result.getBalance());

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Customer.class));
    }
    @Test
    void deveRealizarSaqueComSucesso() {
        Customer customer = new Customer(
                1L, "Eduardo", "edu@gmail.com", BigDecimal.valueOf(100), true
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(customer));

        when(repository.save(any(Customer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Customer result = service.withdraw(1L, BigDecimal.valueOf(50));

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(50), result.getBalance());

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Customer.class));
    }
}