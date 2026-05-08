package br.com.avaliacao.backend.repository;

import br.com.avaliacao.backend.domain.Coupon;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class CouponRepository {

    private final List<Coupon> coupons = new CopyOnWriteArrayList<>();

    public CouponRepository() {
        coupons.add(new Coupon("QA10", new BigDecimal("10"), LocalDate.now().plusDays(10), true));
        coupons.add(new Coupon("EXPIRADO50", new BigDecimal("50"), LocalDate.now().minusDays(2), true));
        coupons.add(new Coupon("INATIVO20", new BigDecimal("20"), LocalDate.now().plusDays(5), false));
        coupons.add(new Coupon("SUPER120", new BigDecimal("120"), LocalDate.now().plusDays(5), true));
    }

    public Optional<Coupon> findByCode(String code) {
        return coupons.stream().filter(coupon -> coupon.getCode().equalsIgnoreCase(code)).findFirst();
    }
}
