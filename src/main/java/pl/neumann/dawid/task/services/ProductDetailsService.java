package pl.neumann.dawid.task.services;

import pl.neumann.dawid.task.enums.Markets;
import pl.neumann.dawid.task.responses.ProductDetailsResponse;

public interface ProductDetailsService {
    ProductDetailsResponse getProductDetails(int productId, Markets market, Integer customerId) throws Exception;
}
