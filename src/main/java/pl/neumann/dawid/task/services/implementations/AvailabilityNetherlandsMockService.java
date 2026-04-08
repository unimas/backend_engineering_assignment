package pl.neumann.dawid.task.services.implementations;

import org.springframework.stereotype.Service;
import pl.neumann.dawid.task.entities.Availability;
import pl.neumann.dawid.task.enums.Markets;
import pl.neumann.dawid.task.services.AvailabilityService;

import java.util.Optional;

@Service
public class AvailabilityNetherlandsMockService implements AvailabilityService {
    @Override
    public Optional<Availability> getAvailabilityInfo() throws InterruptedException {
        Availability availability = new Availability();
        availability.setLocation(Markets.NETHERLANDS.language);
        availability.setStockLevel((int) (Math.random() * 10));
        availability.setExpectedDeliveryInDays((int) (Math.random() * 3 + 1));

        Thread.sleep(100);
        boolean failure = Math.random() * 100 < 2;
        if (failure) {
            return Optional.empty();
        }
        return Optional.of(availability);
    }
}
