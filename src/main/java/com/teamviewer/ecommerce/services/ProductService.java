package com.teamviewer.ecommerce.services;

import com.teamviewer.ecommerce.domain.Product;
import com.teamviewer.ecommerce.entity.ProductEntity;
import com.teamviewer.ecommerce.mappers.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProduct {

    private final ProductMapper productMapper;

    @Override
    public List<Product> findAllProducts() {
        ProductEntity entity = ProductEntity.builder()
                .id("id")
                .description("description")
                .name("tste name")
                .price(100)
                .build();

        return List.of(productMapper.fromEntityToDomain(entity));
    }
}
