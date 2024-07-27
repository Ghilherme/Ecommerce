package com.teamviewer.ecommerce.integration;

import com.ecommerce.model.ProductApi;
import com.ecommerce.model.ProductApiResponse;
import com.teamviewer.ecommerce.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;

import static com.teamviewer.ecommerce.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductIT extends EcommerceApplicationTests {

    /**
     * Verifies return of all products on the ecommerce
     */
    @Test
    @Sql(scripts = "classpath:scripts/populate.sql")
    public void whenGetAllProducts_thenAllProductsReturnedSuccessfully() {
        ResponseEntity<ProductApiResponse> response = restTemplate.getForEntity(PRODUCT_ENDPOINT, ProductApiResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getProducts());
        assertEquals(PRODUCT_ID, response.getBody().getProducts().getFirst().getId());
    }

    /**
     * Verifies return of single product by ID
     */
    @Test
    @Sql(scripts = "classpath:scripts/populate.sql")
    public void whenFindProductById_thenProductsReturnedSuccessfully() {
        ResponseEntity<ProductApi> response =
                restTemplate.getForEntity(PRODUCT_ID_ENDPOINT, ProductApi.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody());
        assertEquals(PRODUCT_ID, response.getBody().getId());
        assertEquals(response.getBody().getName(), response.getBody().getName());
        assertEquals(response.getBody().getDescription(), response.getBody().getDescription());
        assertEquals(response.getBody().getPrice(), response.getBody().getPrice());
    }

    /**
     * Verifies Creation of single product
     */
    @Test
    @Sql(scripts = "classpath:scripts/populate.sql")
    public void whenPostProduct_thenProductIsCreatedSuccessfully() throws IOException {
        ProductApi postBody = TestUtils.readJsonFromFile("src/test/resources/data/post_body_product.json", ProductApi.class);

        ResponseEntity<ProductApi> response = restTemplate.postForEntity(PRODUCT_ENDPOINT, postBody, ProductApi.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(postBody.getName(), response.getBody().getName());
        assertEquals(postBody.getDescription(), response.getBody().getDescription());
        assertEquals(postBody.getPrice(), response.getBody().getPrice());
    }

    /**
     * Verifies update of single product
     */
    @Test
    @Sql(scripts = "classpath:scripts/populate.sql")
    public void whenUpdateProduct_thenProductIsUpdatedSuccessfully() throws IOException {
        ProductApi putBody = TestUtils.readJsonFromFile("src/test/resources/data/put_body_product.json", ProductApi.class);

        restTemplate.put(PRODUCT_ID_ENDPOINT, putBody);

        ResponseEntity<ProductApi> response =
                restTemplate.getForEntity(PRODUCT_ID_ENDPOINT, ProductApi.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(putBody.getName(), response.getBody().getName());
        assertEquals(putBody.getDescription(), response.getBody().getDescription());
        assertEquals(putBody.getPrice(), response.getBody().getPrice());
    }

    /**
     * Verifies deletion of single product
     */
    @Test
    @Sql(scripts = "classpath:scripts/populate.sql")
    public void whenDeleteProduct_thenProductIsDeletedSuccessfully() {
        restTemplate.delete(PRODUCT_ID_ENDPOINT);

        ResponseEntity<ProductApi> response =
                restTemplate.getForEntity(PRODUCT_ID_ENDPOINT, ProductApi.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
