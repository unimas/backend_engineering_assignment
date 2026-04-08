package pl.neumann.dawid.task.services.implementations;

import org.springframework.stereotype.Service;
import pl.neumann.dawid.task.entities.Price;
import pl.neumann.dawid.task.services.PricingService;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PricingNetherlandsMockService implements PricingService{
    @Override
    public Optional<Price> getPricing() throws InterruptedException {
        Price price = new Price();
        price.setBasePrice(generateRandomPrice());
        price.setDiscount(generateRandomDiscount());
        price.setFinalPrice(calculateFinalPrice(price.getBasePrice(), price.getDiscount()));
        price.setCurrency("€");

        Thread.sleep(80);
        boolean failure = Math.random() * 1000 < 5;
        if (failure) {
            return Optional.empty();
        }

        return Optional.of(price);
    }

    private BigDecimal calculateFinalPrice(BigDecimal basePrice, int discount) {
        return basePrice.multiply(BigDecimal.valueOf(100-discount).divide(BigDecimal.valueOf(100)));
    }

    private BigDecimal generateRandomPrice() {
        return BigDecimal.valueOf(Math.random() * 1000 + 100);
    }

    private int generateRandomDiscount() {
        return (int) (Math.random() * 100);
    }
}
