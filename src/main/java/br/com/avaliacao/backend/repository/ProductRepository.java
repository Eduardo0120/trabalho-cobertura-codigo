package br.com.avaliacao.backend.repository;

import br.com.avaliacao.backend.domain.Product;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class ProductRepository {

    private final List<Product> products = new CopyOnWriteArrayList<>();

    public ProductRepository() {
        products.add(new Product(1L, "Mouse QA", new BigDecimal("99.90"), 10, true));
        products.add(new Product(2L, "Teclado QA", new BigDecimal("199.90"), 5, true));
        products.add(new Product(3L, "Monitor QA", new BigDecimal("899.90"), 0, true));
        products.add(new Product(4L, "Produto Desativado", new BigDecimal("39.90"), 20, false));
    }

    public List<Product> findAll() {
        return new ArrayList<>(products);
    }

    public Optional<Product> findById(Long id) {
        return products.stream().filter(product -> product.getId().equals(id)).findFirst();
    }

    public Product save(Product product) {
        products.removeIf(existing -> existing.getId().equals(product.getId()));
        products.add(product);
        return product;
    }
}
