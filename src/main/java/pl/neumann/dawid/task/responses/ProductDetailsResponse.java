package pl.neumann.dawid.task.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import pl.neumann.dawid.task.entities.Availability;
import pl.neumann.dawid.task.entities.CustomerDetails;
import pl.neumann.dawid.task.entities.Price;
import pl.neumann.dawid.task.entities.Product;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDetailsResponse {
    Product productDetails;
    Price price;
    String priceMessage;
    Availability availability;
    String availabilityMessage;
    CustomerDetails customerDetails;
}
