package pl.neumann.dawid.task.services;

import pl.neumann.dawid.task.entities.Availability;

import java.util.Optional;

public interface AvailabilityService {

    Optional<Availability> getAvailabilityInfo() throws InterruptedException;
}
