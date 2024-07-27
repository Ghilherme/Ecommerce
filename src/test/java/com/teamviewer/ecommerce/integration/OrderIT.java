package com.teamviewer.ecommerce.integration;

import com.ecommerce.model.OrderApi;
import com.ecommerce.model.OrderApiResponse;
import com.teamviewer.ecommerce.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;

import static com.teamviewer.ecommerce.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderIT extends EcommerceApplicationTests {

    /**
     * Verifies return of all orders on the ecommerce
     */
    @Test
    @Sql(scripts = "classpath:scripts/populate.sql")
    public void whenGetAllOrderS_thenAllOrdersReturnedSuccessfully() {
        ResponseEntity<OrderApiResponse> response = restTemplate.getForEntity(ORDER_ENDPOINT, OrderApiResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getOrders());
        assertEquals(ORDER_ID, response.getBody().getOrders().getFirst().getId());
    }

    /**
     * Verifies return of single Order by ID
     */
    @Test
    @Sql(scripts = "classpath:scripts/populate.sql")
    public void whenFindOrderById_thenOrdersReturnedSuccessfully() {
        ResponseEntity<OrderApi> response =
                restTemplate.getForEntity(ORDER_ID_ENDPOINT, OrderApi.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody());
        assertEquals(ORDER_ID, response.getBody().getId());
        assertEquals(response.getBody().getName(), response.getBody().getName());
        assertEquals(response.getBody().getTotalPriceAmount(), response.getBody().getTotalPriceAmount());
        assertEquals(response.getBody().getTotalQuantityItems(), response.getBody().getTotalQuantityItems());
    }

    /**
     * Verifies Creation of single Order
     */
    @Test
    @Sql(scripts = "classpath:scripts/populate.sql")
    public void whenPostOrder_thenOrderIsCreatedSuccessfully() throws IOException {
        OrderApi postBody = TestUtils.readJsonFromFile("src/test/resources/data/orders/post_body_order.json", OrderApi.class);

        ResponseEntity<OrderApi> response = restTemplate.postForEntity(ORDER_ENDPOINT, postBody, OrderApi.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(postBody.getName(), response.getBody().getName());
        assertEquals(postBody.getTotalQuantityItems(), response.getBody().getTotalQuantityItems());
        assertEquals(postBody.getTotalPriceAmount(), response.getBody().getTotalPriceAmount());
    }

    /**
     * Verifies an error message returned when creating a Order with invalid data
     */
    @Test
    void whenPostOrderWithInvalidData_thenReturns400() throws Exception {
        OrderApi postBody = TestUtils.readJsonFromFile("src/test/resources/data/orders/post_body_order_invalid_data.json", OrderApi.class);

        ResponseEntity<OrderApi> response = restTemplate.postForEntity(ORDER_ENDPOINT, postBody, OrderApi.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * Verifies update of single Order
     */
    @Test
    @Sql(scripts = "classpath:scripts/populate.sql")
    public void whenUpdateOrder_thenOrderIsUpdatedSuccessfully() throws IOException {
        OrderApi putBody = TestUtils.readJsonFromFile("src/test/resources/data/orders/put_body_order.json", OrderApi.class);

        restTemplate.put(ORDER_ID_ENDPOINT, putBody);

        ResponseEntity<OrderApi> response =
                restTemplate.getForEntity(ORDER_ID_ENDPOINT, OrderApi.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(putBody.getName(), response.getBody().getName());
        assertEquals(putBody.getTotalPriceAmount(), response.getBody().getTotalPriceAmount());
        assertEquals(putBody.getTotalQuantityItems(), response.getBody().getTotalQuantityItems());
    }

    /**
     * Verifies deletion of single Order
     */
    @Test
    @Sql(scripts = "classpath:scripts/populate.sql")
    public void whenDeleteOrder_thenOrderIsDeletedSuccessfully() {
        restTemplate.delete(ORDER_ID_ENDPOINT);

        ResponseEntity<OrderApi> response =
                restTemplate.getForEntity(ORDER_ID_ENDPOINT, OrderApi.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
