package com.teamviewer.ecommerce.integration;

import com.ecommerce.model.*;
import com.teamviewer.ecommerce.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;

import static com.teamviewer.ecommerce.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderItemIT extends EcommerceApplicationTests {

    /**
     * Verifies return of all order items on the ecommerce
     */
    @Test
    @Sql(scripts = "classpath:scripts/populate.sql")
    public void whenGetAllOrderItems_thenAllOrderItemsReturnedSuccessfully() {
        ResponseEntity<OrderItemApiResponse> response = restTemplate.getForEntity(ORDER_ITEM_ENDPOINT, OrderItemApiResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getOrders().getFirst().getId());
        assertEquals(ORDER_ITEM_ID, response.getBody().getOrders().getFirst().getId());
    }

    /**
     * Verifies return of single order item by ID
     */
    @Test
    @Sql(scripts = "classpath:scripts/populate.sql")
    public void whenFindOrderItemById_thenOrderItemsReturnedSuccessfully() {
        ResponseEntity<OrderItemApi> response =
                restTemplate.getForEntity(ORDER_ITEM_ID_ENDPOINT, OrderItemApi.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ORDER_ITEM_ID, response.getBody().getId());
    }

    /**
     * Verifies Creation of single order item
     */
    @Test
    @Sql(scripts = "classpath:scripts/populate.sql")
    public void whenPostOrderItem_thenOrderItemIsCreatedSuccessfully() throws IOException {
        OrderItemApiRequest postBody = TestUtils.readJsonFromFile(
                "src/test/resources/data/order_items/post_body_order_items.json", OrderItemApiRequest.class);

        ResponseEntity<OrderItemApi> response = restTemplate.postForEntity(ORDER_ITEM_ENDPOINT, postBody, OrderItemApi.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertNotNull(response.getBody().getOrderId());
        assertEquals(postBody.getQuantity(), response.getBody().getQuantity());
        assertEquals(postBody.getDiscount(), response.getBody().getDiscount());
    }

    /**
     * Verifies an error message returned when creating order items with invalid data
     */
    @Test
    @Sql(scripts = "classpath:scripts/populate.sql")
    void whenPostOrderItemsWithInvalidData_thenReturns400() throws Exception {
        OrderItemApiRequest postBody = TestUtils.readJsonFromFile(
                "src/test/resources/data/order_items/post_body_order_item_invalid_data.json", OrderItemApiRequest.class);

        ResponseEntity<OrderItemApi> response = restTemplate.postForEntity(ORDER_ENDPOINT, postBody, OrderItemApi.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * Verifies update of single order item
     */
    @Test
    @Sql(scripts = "classpath:scripts/populate.sql")
    public void whenUpdateOrderItems_thenOrderItemIsUpdatedSuccessfully() throws IOException {
        OrderItemApiRequest putBody = TestUtils.readJsonFromFile(
                "src/test/resources/data/order_items/put_body_order_items.json", OrderItemApiRequest.class);

        restTemplate.put(ORDER_ITEM_ID_ENDPOINT, putBody);

        ResponseEntity<OrderItemApi> response = restTemplate.getForEntity(ORDER_ITEM_ID_ENDPOINT, OrderItemApi.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ORDER_ITEM_ID, response.getBody().getId());
        assertEquals(putBody.getProductId(), response.getBody().getProductId());
        assertEquals(putBody.getOrderId(), response.getBody().getOrderId());
        assertEquals(putBody.getDiscount(), response.getBody().getDiscount());
    }

    /**
     * Verifies deletion of single order item
     */
    @Test
    @Sql(scripts = "classpath:scripts/populate.sql")
    public void whenDeleteOrderItems_thenOrderItemIsDeletedSuccessfully() {
        restTemplate.delete(ORDER_ITEM_ID_ENDPOINT);

        ResponseEntity<OrderItemApi> response = restTemplate.getForEntity(ORDER_ITEM_ID_ENDPOINT, OrderItemApi.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
