package com.teamviewer.ecommerce;

import com.ecommerce.model.ProductApi;
import com.ecommerce.model.ProductApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;

import static com.teamviewer.ecommerce.TestUtils.PRODUCT_ENDPOINT;
import static com.teamviewer.ecommerce.TestUtils.PRODUCT_ID;
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
     * Verifies Creation of single product
     */
    @Test
    @Sql(scripts = "classpath:scripts/populate.sql")
    public void whenPostProduct_thenAllProductsReturnedSuccessfully() throws IOException {
        ProductApi postBody = TestUtils.readJsonFromFile("src/test/resources/data/post_body_product.json", ProductApi.class);

        ResponseEntity<ProductApi> response = restTemplate.postForEntity(PRODUCT_ENDPOINT, postBody, ProductApi.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(postBody.getName(), response.getBody().getName());
        assertEquals(postBody.getDescription(), response.getBody().getDescription());
        assertEquals(postBody.getPrice(), response.getBody().getPrice());

    }
}
