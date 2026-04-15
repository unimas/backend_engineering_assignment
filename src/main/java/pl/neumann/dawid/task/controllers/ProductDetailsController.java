package pl.neumann.dawid.task.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.neumann.dawid.task.enums.Markets;
import pl.neumann.dawid.task.responses.ProductDetailsResponse;
import pl.neumann.dawid.task.services.ProductDetailsService;

@RestController
@RequiredArgsConstructor
public class ProductDetailsController {

    private final ProductDetailsService productDetailsService;

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDetailsResponse> getProductDetails(
            @PathVariable("productId") int productId,
            @RequestParam(name="market", required=true) Markets market,
            @RequestParam(name="customerID", required=false) Integer customerId) {
        try {
            return ResponseEntity.ok(productDetailsService.getProductDetails(productId, market, customerId));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
