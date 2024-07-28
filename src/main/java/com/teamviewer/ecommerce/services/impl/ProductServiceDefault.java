package com.teamviewer.ecommerce.services.impl;

import com.teamviewer.ecommerce.domain.Product;
import com.teamviewer.ecommerce.entity.ProductEntity;
import com.teamviewer.ecommerce.mappers.ProductMapper;
import com.teamviewer.ecommerce.repositories.ProductRepository;
import com.teamviewer.ecommerce.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceDefault implements ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Override
    public List<Product> findAllProducts() {
        List<Product> products = new ArrayList<>();

        productRepository.findAll().forEach(product ->
                products.add(productMapper.fromEntityToDomain(product))
        );

        return products;
    }

    @Override
    public Product findById(String id) {
        return productRepository.findById(id)
                .map(productMapper::fromEntityToDomain)
                .orElse(null);
    }

    @Override
    @Transactional
    public Product createProduct(Product product) {
        return saveProduct(product);
    }

    @Override
    @Transactional
    public Product updateProduct(Product product) {
        return saveProduct(product);
    }

    @Override
    @Transactional
    public boolean deleteById(String id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Product saveProduct(Product product) {
        ProductEntity entity = productMapper.fromDomainToEntity(product);
        productRepository.save(entity);

        return productMapper.fromEntityToDomain(entity);
    }
}
