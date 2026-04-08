package pl.neumann.dawid.task.services.implementations;

import org.springframework.stereotype.Service;
import pl.neumann.dawid.task.entities.Product;
import pl.neumann.dawid.task.services.ProductCatalogService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductCatalogMockService implements ProductCatalogService {

    public static final String SPECIFICATION = "Specification";
    public static final String PRODUCT_NR = "Product nr ";
    public static final String DESCRIPTION_OF_PRODUCT_NR = "Description of product nr ";
    public static final String IMAGE = "Image ";
    public static final String OF_PRODUCT = " of product ";
    public static final String PNG_EXTENSION = ".png";

    @Override
    public Product getProductDetails(int id) throws InterruptedException {
        Product product = new Product();
        product.setName(PRODUCT_NR + id);
        product.setDescription(DESCRIPTION_OF_PRODUCT_NR + id);
        product.setSpecs(SPECIFICATION);
        List<String> images = new ArrayList<>();
        images.add(prepareImage(1, id));
        images.add(prepareImage(2, id));
        product.setImages(images);

        Thread.sleep(50);
        boolean failure =Math.random() * 1000 == 0;
        if (failure) {
            return null;
        }
        return product;
    }


    private String prepareImage(int imageNr,int productId) {
        StringBuilder builder = new StringBuilder();
        builder.append(IMAGE).append(imageNr).append(OF_PRODUCT).append(productId).append(PNG_EXTENSION);
        return builder.toString();
    }
}
