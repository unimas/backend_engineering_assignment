package pl.neumann.dawid.task.entities;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Price {
    BigDecimal basePrice;
    int discount;
    BigDecimal finalPrice;
    String currency;
}
