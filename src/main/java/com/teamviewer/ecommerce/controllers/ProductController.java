package com.teamviewer.ecommerce.controllers;

import com.ecommerce.model.ProductApi;
import com.ecommerce.model.ProductApiResponse;
import com.teamviewer.ecommerce.mappers.ProductMapper;
import com.teamviewer.ecommerce.services.IProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductController {

    private final IProduct productService;

    @Autowired
    private ProductMapper productMapper;

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
}
