package pl.neumann.dawid.task.services;

import pl.neumann.dawid.task.entities.CustomerDetails;

import java.util.Optional;

public interface CustomerContextService {
    Optional<CustomerDetails> getCustomerDetails(int customerId) throws InterruptedException;
}
