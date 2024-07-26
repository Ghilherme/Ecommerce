package com.teamviewer.ecommerce.services;

import com.teamviewer.ecommerce.domain.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAllProducts();

    Product findById(String id);

    Product createProduct(Product product);

    Product updateProduct(Product product);

    boolean deleteById(String id);
}
