package pl.neumann.dawid.task;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import pl.neumann.dawid.task.controllers.ProductDetailsController;
import pl.neumann.dawid.task.entities.Availability;
import pl.neumann.dawid.task.entities.CustomerDetails;
import pl.neumann.dawid.task.entities.Price;
import pl.neumann.dawid.task.entities.Product;
import pl.neumann.dawid.task.enums.Markets;
import pl.neumann.dawid.task.responses.ProductDetailsResponse;
import pl.neumann.dawid.task.services.AvailabilityService;
import pl.neumann.dawid.task.services.PricingService;
import pl.neumann.dawid.task.services.ProductCatalogService;
import pl.neumann.dawid.task.services.implementations.CustomerContextMockService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

//FIXME: DN: fixme
@ExtendWith(MockitoExtension.class)
public class ProductDetailsDefaultServiceTests {
    @Mock
    ProductCatalogService productCatalogService;
    @Mock
    CustomerContextMockService customerContextService;

    @InjectMocks
    ProductDetailsController controller;

    @Test
    void shouldReturnFullObjectWhenCorrectDataGiven() throws InterruptedException {
        //given
        when(productCatalogService.getProductDetails(any())).thenReturn(prepareProduct(1));
        //when(pricingService.getPricing()).thenReturn(preparePricing());
        //when(availabilityService.getAvailabilityInfo()).thenReturn(prepareAvailabilityInfo(Markets.NETHERLANDS));
        when(customerContextService.getCustomerDetails(any())).thenReturn(prepareCustomerDetails(312));

        //when
        ResponseEntity<ProductDetailsResponse> response = controller.getProductDetails(1, Markets.NETHERLANDS, 312);

        //then
        ProductDetailsResponse body = response.getBody();
        assertThat(body.getProductDetails()).isNotNull();
        assertThat(body.getPrice()).isNotNull();
        assertThat(body.getAvailability()).isNotNull();
        assertThat(body.getCustomerDetails()).isNotNull();
    }

    @Test
    void shouldReturnObjectWithoutCustomerDetailsWhenNoCustomerIdGiven() throws InterruptedException {
        //given
        when(productCatalogService.getProductDetails(any())).thenReturn(prepareProduct(1));
        //when(pricingService.getPricing()).thenReturn(preparePricing());
        //when(availabilityService.getAvailabilityInfo()).thenReturn(prepareAvailabilityInfo(Markets.NETHERLANDS));

        //when
        ResponseEntity<ProductDetailsResponse> response = controller.getProductDetails(1, Markets.NETHERLANDS, null);

        //then
        ProductDetailsResponse body = response.getBody();
        assertThat(body.getProductDetails()).isNotNull();
        assertThat(body.getPrice()).isNotNull();
        assertThat(body.getAvailability()).isNotNull();
    }

    Product prepareProduct(int id) {
        Product product = new Product();

        product.setName("Product Name" + id);
        product.setDescription("Product description");
        product.setSpecs("Product specification");
        List<String> images = new ArrayList<>();
        images.add("Product image");
        product.setImages(images);

        return product;
    }

    private Optional<Price> preparePricing() {
        Price price = new Price();

        price.setBasePrice(BigDecimal.valueOf(100));
        price.setDiscount(20);
        price.setFinalPrice(BigDecimal.valueOf(80));

        return Optional.of(price);

    }

    private Optional<Availability> prepareAvailabilityInfo(Markets market) {
        Availability availability = new Availability();

        availability.setLocation(market.name());
        availability.setStockLevel(20);
        availability.setExpectedDeliveryInDays(2);

        return Optional.of(availability);
    }

    private Optional<CustomerDetails> prepareCustomerDetails(int id) {
        CustomerDetails details = new CustomerDetails();

        details.setPreferences("Preferences of customer " + id);

        return Optional.of(details);
    }

}
