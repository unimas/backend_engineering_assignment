package pl.neumann.dawid.task.services;

import pl.neumann.dawid.task.entities.Product;

public interface ProductCatalogService {

    Product getProductDetails(int id) throws InterruptedException;
}
