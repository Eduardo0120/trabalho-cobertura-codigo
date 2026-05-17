package br.com.avaliacao.backend.service;

import br.com.avaliacao.backend.domain.Customer;
import br.com.avaliacao.backend.domain.Order;
import br.com.avaliacao.backend.repository.CustomerRepository;
import br.com.avaliacao.backend.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class ReportServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderRepository orderRepository;

    private ReportService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new ReportService(customerRepository, orderRepository);
    }

    @Test
    void deveGerarResumoAdministrativoComClientesPedidosEReceita() {

        Customer customer1 = new Customer(
                1L, "Eduardo", "edu@gmail.com", BigDecimal.ZERO, true
        );

        Customer customer2 = new Customer(
                2L, "Maria", "maria@gmail.com", BigDecimal.ZERO, true
        );

        Order order1 = new Order(
                1L,
                1L,
                List.of(),
                BigDecimal.valueOf(100),
                "APPROVED",
                null,
                LocalDateTime.now()
        );

        Order order2 = new Order(
                2L,
                2L,
                List.of(),
                BigDecimal.valueOf(50),
                "APPROVED",
                null,
                LocalDateTime.now()
        );

        when(customerRepository.findAll())
                .thenReturn(List.of(customer1, customer2));

        when(orderRepository.findAll())
                .thenReturn(List.of(order1, order2));

        Map<String, Object> report = service.summary();

        assertEquals(2, report.get("customers"));
        assertEquals(2, report.get("orders"));
        assertEquals(BigDecimal.valueOf(150), report.get("revenue"));

        assertTrue(report.containsKey("internalNote"));
    }
}