package pl.neumann.dawid.task.services;

import pl.neumann.dawid.task.entities.Price;

import java.util.Optional;

public interface PricingService {

    Optional<Price> getPricing() throws InterruptedException;
}
