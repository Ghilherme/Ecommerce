package com.teamviewer.ecommerce.unittests.services;

import com.teamviewer.ecommerce.domain.Product;
import com.teamviewer.ecommerce.entity.ProductEntity;
import com.teamviewer.ecommerce.mappers.ProductMapper;
import com.teamviewer.ecommerce.repositories.ProductRepository;
import com.teamviewer.ecommerce.services.impl.ProductServiceDefault;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.teamviewer.ecommerce.TestUtils.PRODUCT_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {
    @MockBean
    private ProductMapper productMapper;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductServiceDefault productService;

    private Product productMock;
    private ProductEntity productEntityMock;

    @BeforeEach
    void setUp() {
        productMock = buildMockProduct();
        productEntityMock = buildMockProductEntity();
    }

    @Test
    void testFindAllProducts() {
        List<ProductEntity> entities = new ArrayList<>();
        entities.add(productEntityMock);

        when(productRepository.findAll()).thenReturn(entities);
        when(productMapper.fromEntityToDomain(any(ProductEntity.class))).thenReturn(productMock);

        List<Product> products = productService.findAllProducts();

        assertNotNull(products);
        assertEquals(1, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(productEntityMock));
        when(productMapper.fromEntityToDomain(any(ProductEntity.class))).thenReturn(productMock);

        Product foundProduct = productService.findById(PRODUCT_ID);

        assertNotNull(foundProduct);
        assertEquals(PRODUCT_ID, foundProduct.getId());
        verify(productRepository, times(1)).findById(PRODUCT_ID);
    }

    @Test
    void testFindById_NotFound() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

        Product foundProduct = productService.findById(PRODUCT_ID);

        assertNull(foundProduct);
        verify(productRepository, times(1)).findById(PRODUCT_ID);
    }

    @Test
    void testCreateProduct() {
        when(productMapper.fromDomainToEntity(any(Product.class))).thenReturn(productEntityMock);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntityMock);
        when(productMapper.fromEntityToDomain(any(ProductEntity.class))).thenReturn(productMock);

        Product createdProduct = productService.createProduct(productMock);

        assertNotNull(createdProduct);
        assertEquals(PRODUCT_ID, createdProduct.getId());
        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    void testUpdateProduct() {
        when(productMapper.fromDomainToEntity(any(Product.class))).thenReturn(productEntityMock);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntityMock);
        when(productMapper.fromEntityToDomain(any(ProductEntity.class))).thenReturn(productMock);

        Product updatedProduct = productService.updateProduct(productMock);

        assertNotNull(updatedProduct);
        assertEquals(PRODUCT_ID, updatedProduct.getId());
        verify(productRepository, times(1)).save(any(ProductEntity.class));
    }

    @Test
    void testDeleteById() {
        when(productRepository.existsById(PRODUCT_ID)).thenReturn(true);

        boolean isDeleted = productService.deleteById(PRODUCT_ID);

        assertTrue(isDeleted);
        verify(productRepository, times(1)).deleteById(PRODUCT_ID);
    }

    @Test
    void testDeleteById_NotFound() {
        when(productRepository.existsById(PRODUCT_ID)).thenReturn(false);

        boolean isDeleted = productService.deleteById(PRODUCT_ID);

        assertFalse(isDeleted);
        verify(productRepository, never()).deleteById(PRODUCT_ID);
    }

    private static @NotNull ProductEntity buildMockProductEntity() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(PRODUCT_ID);
        productEntity.setName("Test Product");
        productEntity.setDescription("Description");
        productEntity.setPrice(10.0);
        return productEntity;
    }

    private static @NotNull Product buildMockProduct() {
        Product product = new Product();
        product.setId(PRODUCT_ID);
        product.setName("Test Product");
        product.setDescription("Description");
        product.setPrice(10.0);
        return product;
    }
}
