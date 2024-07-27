package com.teamviewer.ecommerce.controllers;

import com.ecommerce.model.ProductApi;
import com.ecommerce.model.ProductApiResponse;
import com.teamviewer.ecommerce.domain.Product;
import com.teamviewer.ecommerce.exceptions.InvalidDataException;
import com.teamviewer.ecommerce.mappers.ProductMapper;
import com.teamviewer.ecommerce.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.util.UriComponentsBuilder;

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
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new InvalidDataException("Name cannot be null or empty");
        }
        if (product.getPrice() == null) {
            throw new InvalidDataException("Price cannot be null");
        }

        product.setId(null);
        Product created = productService.createProduct(productMapper.fromApiToDomain(product));
        ProductApi response = productMapper.fromDomainToApi(created);

        return ResponseEntity.created(UriComponentsBuilder.fromPath("/api/products/{id}")
                        .buildAndExpand(response.getId())
                        .toUri())
                .body(response);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductApi> updateProduct(@RequestBody ProductApi product, @PathVariable String id) {
        if (product.getId() != null) {
           throw new InvalidDataException("Product id cannot be updated.");
        }

        product.setId(id);
        Product created = productService.updateProduct(productMapper.fromApiToDomain(product));

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
