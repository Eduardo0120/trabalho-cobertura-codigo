package br.com.avaliacao.backend.service;

import br.com.avaliacao.backend.domain.Coupon;
import br.com.avaliacao.backend.domain.Customer;
import br.com.avaliacao.backend.domain.Order;
import br.com.avaliacao.backend.domain.Product;
import br.com.avaliacao.backend.dto.CreateOrderRequest;
import br.com.avaliacao.backend.dto.OrderItemRequest;
import br.com.avaliacao.backend.repository.CouponRepository;
import br.com.avaliacao.backend.repository.CustomerRepository;
import br.com.avaliacao.backend.repository.OrderRepository;
import br.com.avaliacao.backend.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private OrderRepository orderRepository;

    private OrderService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new OrderService(
                customerRepository,
                productRepository,
                couponRepository,
                orderRepository
        );
    }

    @Test
    void deveCriarPedidoComSucessoSemCupom() {
        Customer customer = new Customer(
                1L, "Eduardo", "edu@gmail.com", BigDecimal.valueOf(100), true
        );

        Product product = new Product(
                1L, "Mouse QA", BigDecimal.valueOf(100), 10, true
        );

        CreateOrderRequest request = new CreateOrderRequest(
                1L,
                List.of(new OrderItemRequest(1L, 1)),
                null
        );

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.nextId()).thenReturn(1L);
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order result = service.create(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getCustomerId());
        assertEquals(BigDecimal.valueOf(100), result.getTotal());
        assertEquals("APPROVED", result.getStatus());
        assertEquals(1, result.getItems().size());

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void deveRejeitarPedidoParaClienteInativo() {
        Customer inactiveCustomer = new Customer(
                1L, "Cliente Inativo", "inativo@gmail.com", BigDecimal.valueOf(100), false
        );

        Product product = new Product(
                1L, "Mouse QA", BigDecimal.valueOf(100), 10, true
        );

        CreateOrderRequest request = new CreateOrderRequest(
                1L,
                List.of(new OrderItemRequest(1L, 1)),
                null
        );

        when(customerRepository.findById(1L)).thenReturn(Optional.of(inactiveCustomer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(
                BusinessException.class,
                () -> service.create(request)
        );

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void deveCalcularTotalConsiderandoQuantidade() {
        Customer customer = new Customer(
                1L, "Eduardo", "edu@gmail.com", BigDecimal.valueOf(100), true
        );

        Product product = new Product(
                1L, "Mouse QA", BigDecimal.valueOf(100), 10, true
        );

        CreateOrderRequest request = new CreateOrderRequest(
                1L,
                List.of(new OrderItemRequest(1L, 2)),
                null
        );

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.nextId()).thenReturn(1L);
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order result = service.create(request);

        assertEquals(BigDecimal.valueOf(200), result.getTotal());
        assertEquals(BigDecimal.valueOf(200), result.getItems().get(0).getSubtotal());
    }

    @Test
    void deveBaixarEstoqueAposCriarPedido() {
        Customer customer = new Customer(
                1L, "Eduardo", "edu@gmail.com", BigDecimal.valueOf(100), true
        );

        Product product = new Product(
                1L, "Mouse QA", BigDecimal.valueOf(100), 10, true
        );

        CreateOrderRequest request = new CreateOrderRequest(
                1L,
                List.of(new OrderItemRequest(1L, 2)),
                null
        );

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.nextId()).thenReturn(1L);
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        service.create(request);

        assertEquals(8, product.getStock());
    }

    @Test
    void deveRejeitarCupomExpirado() {
        Customer customer = new Customer(
                1L, "Eduardo", "edu@gmail.com", BigDecimal.valueOf(100), true
        );

        Product product = new Product(
                1L, "Mouse QA", BigDecimal.valueOf(100), 10, true
        );

        Coupon expiredCoupon = new Coupon(
                "EXPIRADO50",
                BigDecimal.valueOf(50),
                LocalDate.now().minusDays(2),
                true
        );

        CreateOrderRequest request = new CreateOrderRequest(
                1L,
                List.of(new OrderItemRequest(1L, 1)),
                "EXPIRADO50"
        );

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(couponRepository.findByCode("EXPIRADO50")).thenReturn(Optional.of(expiredCoupon));

        assertThrows(
                BusinessException.class,
                () -> service.create(request)
        );

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void deveRejeitarPedidoComTotalNegativoPorCupomAcimaDeCemPorCento() {
        Customer customer = new Customer(
                1L, "Eduardo", "edu@gmail.com", BigDecimal.valueOf(100), true
        );

        Product product = new Product(
                1L, "Mouse QA", BigDecimal.valueOf(100), 10, true
        );

        Coupon superCoupon = new Coupon(
                "SUPER120",
                BigDecimal.valueOf(120),
                LocalDate.now().plusDays(5),
                true
        );

        CreateOrderRequest request = new CreateOrderRequest(
                1L,
                List.of(new OrderItemRequest(1L, 1)),
                "SUPER120"
        );

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(couponRepository.findByCode("SUPER120")).thenReturn(Optional.of(superCoupon));

        assertThrows(
                BusinessException.class,
                () -> service.create(request)
        );

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void deveRejeitarProdutoInativo() {
        Customer customer = new Customer(
                1L, "Eduardo", "edu@gmail.com", BigDecimal.valueOf(100), true
        );

        Product inactiveProduct = new Product(
                4L, "Produto Desativado", BigDecimal.valueOf(39.90), 20, false
        );

        CreateOrderRequest request = new CreateOrderRequest(
                1L,
                List.of(new OrderItemRequest(4L, 1)),
                null
        );

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(4L)).thenReturn(Optional.of(inactiveProduct));

        assertThrows(
                BusinessException.class,
                () -> service.create(request)
        );

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void deveRejeitarEstoqueInsuficiente() {
        Customer customer = new Customer(
                1L, "Eduardo", "edu@gmail.com", BigDecimal.valueOf(100), true
        );

        Product product = new Product(
                1L, "Mouse QA", BigDecimal.valueOf(100), 1, true
        );

        CreateOrderRequest request = new CreateOrderRequest(
                1L,
                List.of(new OrderItemRequest(1L, 2)),
                null
        );

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(
                BusinessException.class,
                () -> service.create(request)
        );

        verify(orderRepository, never()).save(any(Order.class));
    }
}