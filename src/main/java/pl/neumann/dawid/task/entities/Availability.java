package pl.neumann.dawid.task.entities;

import lombok.Data;

@Data
public class Availability {
    int stockLevel;
    String location;
    int expectedDeliveryInDays;
}
