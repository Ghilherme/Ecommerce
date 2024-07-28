package com.teamviewer.ecommerce.unittests.controllers;

import com.ecommerce.model.OrderItemApi;
import com.ecommerce.model.OrderItemApiRequest;
import com.ecommerce.model.OrderItemApiResponse;
import com.teamviewer.ecommerce.TestUtils;
import com.teamviewer.ecommerce.controllers.OrderItemController;
import com.teamviewer.ecommerce.domain.Order;
import com.teamviewer.ecommerce.domain.OrderItem;
import com.teamviewer.ecommerce.domain.Product;
import com.teamviewer.ecommerce.exceptions.InvalidDataException;
import com.teamviewer.ecommerce.mappers.OrderItemMapper;
import com.teamviewer.ecommerce.services.OrderItemService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static com.teamviewer.ecommerce.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderItemController.class)
public class OrderItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderItemService orderItemService;

    @MockBean
    private OrderItemMapper orderItemMapper;

    private OrderItem createdOrderItemMock;
    private OrderItemApi createdOrderItemApiMock;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        createdOrderItemMock = buildMockOrderItem();
        createdOrderItemApiMock = buildMockOrderItemApi();
    }

    @Test
    public void whenPostOrderItem_thenReturnCreatedOrderItem() throws Exception {

        when(orderItemService.createOrderItem(any(OrderItem.class))).thenReturn(createdOrderItemMock);
        when(orderItemMapper.fromApiToDomain(any(OrderItemApiRequest.class))).thenReturn(createdOrderItemMock);
        when(orderItemMapper.fromDomainToApi(any(OrderItem.class))).thenReturn(createdOrderItemApiMock);

        String response = mockMvc.perform(post(ORDER_ITEM_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"order_id\":\"order_id\",\"product_id\":\"product_id\",\"quantity\":2,\"final_price\":100.0}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderItemApi orderItemResponse = TestUtils.readValue(response, OrderItemApi.class);

        assertEquals(createdOrderItemMock.getId(), orderItemResponse.getId());
        assertEquals(createdOrderItemMock.getOrder().getId(), orderItemResponse.getOrderId());
        assertEquals(createdOrderItemMock.getProduct().getId(), orderItemResponse.getProductId());
        assertEquals(createdOrderItemMock.getQuantity(), orderItemResponse.getQuantity());
        assertEquals(createdOrderItemMock.getFinalPrice(), orderItemResponse.getFinalPrice());
    }

    @Test
    void whenPostOrderItemWithInvalidData_thenReturns400() throws Exception {
        String invalidOrderItemJson = "{ \"quantity\": 2 }";

        mockMvc.perform(post(ORDER_ITEM_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidOrderItemJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenGetOrderItems_thenReturnOrderItems() throws Exception {

        when(orderItemService.findAllOrderItems()).thenReturn(List.of(createdOrderItemMock));
        when(orderItemMapper.fromDomainToApi(createdOrderItemMock)).thenReturn(createdOrderItemApiMock);

        String response = mockMvc.perform(get(ORDER_ITEM_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderItemApiResponse orderItemList = TestUtils.readValue(response, OrderItemApiResponse.class);

        assertEquals(createdOrderItemMock.getId(), orderItemList.getOrderItems().getFirst().getId());
        assertEquals(createdOrderItemMock.getOrder().getId(), orderItemList.getOrderItems().getFirst().getOrderId());
        assertEquals(createdOrderItemMock.getProduct().getId(), orderItemList.getOrderItems().getFirst().getProductId());
        assertEquals(createdOrderItemMock.getQuantity(), orderItemList.getOrderItems().getFirst().getQuantity());
        assertEquals(createdOrderItemMock.getFinalPrice(), orderItemList.getOrderItems().getFirst().getFinalPrice());
    }

    @Test
    public void whenGetOrderItemById_thenReturnOrderItem() throws Exception {

        when(orderItemService.findById(ORDER_ITEM_ID)).thenReturn(createdOrderItemMock);
        when(orderItemMapper.fromDomainToApi(createdOrderItemMock)).thenReturn(createdOrderItemApiMock);

        String response = mockMvc.perform(get(ORDER_ITEM_ID_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderItemApi orderItem = TestUtils.readValue(response, OrderItemApi.class);

        assertEquals(createdOrderItemMock.getId(), orderItem.getId());
        assertEquals(createdOrderItemMock.getOrder().getId(), orderItem.getOrderId());
        assertEquals(createdOrderItemMock.getProduct().getId(), orderItem.getProductId());
        assertEquals(createdOrderItemMock.getQuantity(), orderItem.getQuantity());
        assertEquals(createdOrderItemMock.getFinalPrice(), orderItem.getFinalPrice());
    }

    @Test
    public void whenUpdateOrderItemById_thenOrderItemUpdatedIsReturned() throws Exception {
        OrderItem updatedOrderItem = buildMockOrderItem();
        updatedOrderItem.setId(ORDER_ITEM_ID);

        when(orderItemMapper.fromApiToDomain(any(OrderItemApiRequest.class))).thenReturn(createdOrderItemMock);
        when(orderItemService.updateOrderItem(createdOrderItemMock)).thenReturn(updatedOrderItem);
        when(orderItemMapper.fromDomainToApi(updatedOrderItem)).thenReturn(createdOrderItemApiMock);

        String response = mockMvc.perform(put(ORDER_ITEM_ID_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"order_id\":\"order_id\",\"product_id\":\"product_id\",\"quantity\":2,\"final_price\":100.0}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderItemApi orderItem = TestUtils.readValue(response, OrderItemApi.class);

        assertEquals(createdOrderItemMock.getId(), orderItem.getId());
        assertEquals(createdOrderItemMock.getOrder().getId(), orderItem.getOrderId());
        assertEquals(createdOrderItemMock.getProduct().getId(), orderItem.getProductId());
        assertEquals(createdOrderItemMock.getQuantity(), orderItem.getQuantity());
        assertEquals(createdOrderItemMock.getFinalPrice(), orderItem.getFinalPrice());
        verify(orderItemService, times(1)).updateOrderItem(createdOrderItemMock);
    }

    @Test
    public void whenUpdateOrderItemWithId_thenThrowInvalidDataException() throws Exception {

        mockMvc.perform(put(ORDER_ITEM_ID_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"4587\",\"order_id\":\"order_id\",\"product_id\":\"product_id\",\"quantity\":2,\"final_price\":100.0}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(InvalidDataException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals("Order Item Id cannot be updated.",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void whenDeleteOrderItemExists_thenStatusNoContent() throws Exception {

        when(orderItemService.deleteById(ORDER_ITEM_ID)).thenReturn(true);

        mockMvc.perform(delete(ORDER_ITEM_ID_ENDPOINT))
                .andExpect(status().isNoContent());

        verify(orderItemService, times(1)).deleteById(ORDER_ITEM_ID);
    }

    @Test
    void whenDeleteOrderItemDoesNotExist_thenStatusNotFound() throws Exception {

        when(orderItemService.deleteById(ORDER_ITEM_ID)).thenReturn(false);

        mockMvc.perform(delete(ORDER_ITEM_ID_ENDPOINT))
                .andExpect(status().isNotFound());

        verify(orderItemService, times(1)).deleteById(ORDER_ITEM_ID);
    }

    private static @NotNull OrderItemApi buildMockOrderItemApi() {
        OrderItemApi orderItemApi = new OrderItemApi();
        orderItemApi.setId(ORDER_ITEM_ID);
        orderItemApi.setOrderId("order_id");
        orderItemApi.setProductId("product_id");
        orderItemApi.setQuantity(2);
        orderItemApi.setFinalPrice(100.0);
        return orderItemApi;
    }

    private static @NotNull OrderItem buildMockOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(ORDER_ITEM_ID);
        orderItem.setOrder(new Order("order_id", "order #1", 100.0, 2));
        orderItem.setProduct(new Product("product_id", "product 1", "nice product", 100.0));
        orderItem.setQuantity(2);
        orderItem.setFinalPrice(100.0);
        return orderItem;
    }
}
