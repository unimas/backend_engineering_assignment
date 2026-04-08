package pl.neumann.dawid.task.services.implementations;

import org.springframework.stereotype.Service;
import pl.neumann.dawid.task.entities.CustomerDetails;
import pl.neumann.dawid.task.services.CustomerContextService;

import java.util.Optional;

@Service
public class CustomerContextMockService implements CustomerContextService {
    public static final String PREFERENCES = "Preferences";
    public static final String GENERAL_PREFERENCES = "General Preferences";

    @Override
    public Optional<CustomerDetails> getCustomerDetails(int customerId) throws InterruptedException {
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setPreferences(PREFERENCES);

        Thread.sleep(60);
        boolean failure = Math.random() * 100 == 0;
        if (failure) {
            return Optional.empty();
        }
        return Optional.of(customerDetails);
    }

    public CustomerDetails getGeneralCustomerDetails() {
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setPreferences(GENERAL_PREFERENCES);
        return customerDetails;
    }
}
