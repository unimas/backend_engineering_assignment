package pl.neumann.dawid.task.controllers;


import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.neumann.dawid.task.entities.Availability;
import pl.neumann.dawid.task.entities.CustomerDetails;
import pl.neumann.dawid.task.entities.Price;
import pl.neumann.dawid.task.entities.Product;
import pl.neumann.dawid.task.enums.Markets;
import pl.neumann.dawid.task.responses.ProductDetailsResponse;
import pl.neumann.dawid.task.services.AvailabilityService;
import pl.neumann.dawid.task.services.CustomerContextService;
import pl.neumann.dawid.task.services.PricingService;
import pl.neumann.dawid.task.services.ProductCatalogService;
import pl.neumann.dawid.task.services.implementations.*;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class ProductDetailsController {
    public static final String UNAVAILABLE = "unavailable";
    public static final String UNKNOWN = "unknown";

    private final ProductCatalogService productCatalogService;
    private final CustomerContextMockService customerContextService;

    private final PricingGermanyMockService pricingGermanyService;
    private final PricingNetherlandsMockService pricingNetherlandsService;
    private final PricingPolandMockService pricingPolandService;

    private final AvailabilityGermanyMockService availabilityGermanyService;
    private final AvailabilityNetherlandsMockService availabilityNetherlandsMockService;
    private final AvailabilityPolandMockService availabilityPolandMockService;

    private TimeLimiter timeLimiter = TimeLimiter.of(TimeLimiterConfig.custom()
            .timeoutDuration(Duration.ofMillis(500)).build());

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDetailsResponse> getProductDetails(
            @PathVariable("id") int id,
            @RequestParam(name="market", required=true) Markets market,
            @RequestParam(name="customerID", required=false) Integer customerId) {
        ProductDetailsResponse response = new ProductDetailsResponse();
        PricingService pricingService = determinePricingHub(market);
        AvailabilityService availabilityService = determineAvailabilityHub(market);

        CompletableFuture<Product> futureProduct;
        CompletableFuture<Optional<Price>> futurePrice;
        CompletableFuture<Optional<Availability>> futureAvailability;
        CompletableFuture<Optional<CustomerDetails>> futureCustomerDetails;
        CustomerDetails customerDetails;
        try {
            futureProduct = obtainProductInfo(id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        try {
            futurePrice = obtainPricing(pricingService);
            futureAvailability = obtainAvailability(availabilityService);
            if (customerId != null) {
                futureCustomerDetails = obtainCustomerDetails(customerId, customerContextService);
                customerDetails = futureCustomerDetails.get().isPresent() ?
                        futureCustomerDetails.get().get() : customerContextService.getGeneralCustomerDetails();
            } else {
                customerDetails =customerContextService.getGeneralCustomerDetails();
            }
            aggregateResponses(futureProduct.get(), futurePrice.get(), futureAvailability.get(), customerDetails, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(response);
    }

    private PricingService determinePricingHub(Markets marketLanguage) {
        switch (marketLanguage) {
            case POLAND -> {
                return pricingPolandService;
            }
            case GERMANY -> {
                return pricingGermanyService;
            }
            default -> {
                return  pricingNetherlandsService;
            }
        }
    }

    private AvailabilityService determineAvailabilityHub(Markets marketLanguage) {
        switch (marketLanguage) {
            case POLAND -> {
                return availabilityPolandMockService;
            }
            case GERMANY -> {
                return availabilityGermanyService;
            }
            default ->  {
                return availabilityNetherlandsMockService;
            }
        }
    }

    private CompletableFuture<Product> obtainProductInfo(int id) throws Exception {
        CompletableFuture<Product> futureProduct = CompletableFuture.supplyAsync(() -> {
            try {
                return productCatalogService.getProductDetails(id);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        });
        Callable<Product> callableProduct = TimeLimiter.decorateFutureSupplier(timeLimiter, () -> futureProduct);
        callableProduct.call();

        return futureProduct;
    }

    private CompletableFuture<Optional<Price>> obtainPricing(PricingService pricingService) throws Exception {
        CompletableFuture<Optional<Price>> futurePricing = CompletableFuture.supplyAsync(() -> {
            try {
                return pricingService.getPricing();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        });
        Callable<Optional<Price>> callablePrice = TimeLimiter.decorateFutureSupplier(timeLimiter, () -> futurePricing);
        callablePrice.call();

        return futurePricing;
    }

    private CompletableFuture<Optional<Availability>> obtainAvailability(AvailabilityService availabilityService) throws Exception {
        CompletableFuture<Optional<Availability>> futureAvailability = CompletableFuture.supplyAsync(() -> {
            try {
                return availabilityService.getAvailabilityInfo();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        });
        Callable<Optional<Availability>> callablePrice = TimeLimiter.decorateFutureSupplier(timeLimiter, () -> futureAvailability);
        callablePrice.call();

        return futureAvailability;
    }

    private CompletableFuture<Optional<CustomerDetails>> obtainCustomerDetails(int customerId,
                                                                               CustomerContextService customerService) throws Exception {
        CompletableFuture<Optional<CustomerDetails>> futureCustomerDetails = CompletableFuture.supplyAsync(() -> {
            try {
                return customerService.getCustomerDetails(customerId);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        });
        Callable<Optional<CustomerDetails>> callableCustomerDetails = TimeLimiter.decorateFutureSupplier(timeLimiter, () -> futureCustomerDetails);
        callableCustomerDetails.call();

        return futureCustomerDetails;
    }

    private void aggregateResponses(Product futureProduct,
                                    Optional<Price> price,
                                    Optional<Availability> availability,
                                    CustomerDetails customerDetails,
                                    ProductDetailsResponse response) {
        response.setProductDetails(futureProduct);

        response.setPrice(price.isPresent() ? price.get() : null);
        response.setPriceMessage(price.isPresent() ? null : UNAVAILABLE);

        response.setAvailability(availability.isPresent() ? availability.get() : null);
        response.setAvailabilityMessage(availability.isPresent() ? null : UNKNOWN);

        response.setCustomerDetails(customerDetails);
    }
}
