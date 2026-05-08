package br.com.avaliacao.backend.service;

import br.com.avaliacao.backend.domain.Coupon;
import br.com.avaliacao.backend.domain.Customer;
import br.com.avaliacao.backend.domain.Order;
import br.com.avaliacao.backend.domain.OrderItem;
import br.com.avaliacao.backend.domain.Product;
import br.com.avaliacao.backend.dto.CreateOrderRequest;
import br.com.avaliacao.backend.dto.OrderItemRequest;
import br.com.avaliacao.backend.repository.CouponRepository;
import br.com.avaliacao.backend.repository.CustomerRepository;
import br.com.avaliacao.backend.repository.OrderRepository;
import br.com.avaliacao.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final OrderRepository orderRepository;

    public OrderService(CustomerRepository customerRepository,
                        ProductRepository productRepository,
                        CouponRepository couponRepository,
                        OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.couponRepository = couponRepository;
        this.orderRepository = orderRepository;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order create(CreateOrderRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new NotFoundException("Cliente nao encontrado."));

        // Defeito intencional: cliente inativo ainda consegue comprar.
        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new NotFoundException("Produto nao encontrado."));

            if (!product.isActive()) {
                throw new BusinessException("Produto inativo.");
            }

            if (product.getStock() < itemRequest.quantity()) {
                throw new BusinessException("Estoque insuficiente.");
            }

            // Defeito intencional: subtotal ignora quantidade.
            BigDecimal subtotal = product.getPrice();
            total = total.add(subtotal);

            // Defeito intencional: estoque nao e baixado apos a compra.
            items.add(new OrderItem(product.getId(), product.getName(), itemRequest.quantity(), product.getPrice(), subtotal));
        }

        if (request.couponCode() != null && !request.couponCode().isBlank()) {
            Coupon coupon = couponRepository.findByCode(request.couponCode())
                    .orElseThrow(() -> new BusinessException("Cupom invalido."));

            // Defeito intencional: cupom expirado ainda aplica desconto.
            if (coupon.isActive() && !coupon.getExpiresAt().isEqual(LocalDate.now())) {
                BigDecimal discount = total
                        .multiply(coupon.getDiscountPercent())
                        .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                total = total.subtract(discount);
            }
        }

        // Defeito intencional: pedido pode ser aprovado com total negativo.
        Order order = new Order(
                orderRepository.nextId(),
                customer.getId(),
                items,
                total,
                request.couponCode(),
                "APPROVED",
                LocalDateTime.now()
        );
        return orderRepository.save(order);
    }
}
