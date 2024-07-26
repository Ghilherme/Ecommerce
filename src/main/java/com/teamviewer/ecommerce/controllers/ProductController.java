package com.teamviewer.ecommerce.controllers;

import com.ecommerce.model.ProductApi;
import com.ecommerce.model.ProductApiResponse;
import com.teamviewer.ecommerce.domain.Product;
import com.teamviewer.ecommerce.mappers.ProductMapper;
import com.teamviewer.ecommerce.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping("/products")
    public ResponseEntity<ProductApiResponse> findAll() {
        List<ProductApi> products = productService.findAllProducts()
                .stream()
                .map(productMapper::fromDomainToApi)
                .toList();
        ProductApiResponse response = new ProductApiResponse();
        response.setProducts(products);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductApi> findById(@PathVariable String id) {
        Product product = productService.findById(id);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        ProductApi response = productMapper.fromDomainToApi(product);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/products")
    public ResponseEntity<ProductApi> createProduct(@RequestBody ProductApi product) {
        product.setId(null);
        Product created = productService.createProduct(productMapper.fromApiToDomain(product));
        ProductApi response = productMapper.fromDomainToApi(created);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductApi> updateProduct(@RequestBody ProductApi product, @PathVariable String id) {
        product.setId(id);
        Product f = productMapper.fromApiToDomain(product);
        Product created = productService.updateProduct(f);

        ProductApi response = productMapper.fromDomainToApi(created);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        boolean isDeleted = productService.deleteById(id);

        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
