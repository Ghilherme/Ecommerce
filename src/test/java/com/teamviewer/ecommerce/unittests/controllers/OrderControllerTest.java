package com.teamviewer.ecommerce.unittests.controllers;

import com.ecommerce.model.OrderApi;
import com.ecommerce.model.OrderApiResponse;
import com.teamviewer.ecommerce.TestUtils;
import com.teamviewer.ecommerce.controllers.OrderController;
import com.teamviewer.ecommerce.domain.Order;
import com.teamviewer.ecommerce.exceptions.InvalidDataException;
import com.teamviewer.ecommerce.mappers.OrderMapper;
import com.teamviewer.ecommerce.services.OrderService;
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

import static com.teamviewer.ecommerce.TestUtils.ORDER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService OrderService;

    @MockBean
    private OrderMapper OrderMapper;

    private Order createdOrderMock;
    private OrderApi createdOrderApiMock;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        createdOrderMock = buildMockOrder();
        createdOrderApiMock = buildMockOrderApi();
    }

    @Test
    public void whenPostOrder_thenReturnCreatedOrder() throws Exception {

        when(OrderService.createOrder(any(Order.class))).thenReturn(createdOrderMock);
        when(OrderMapper.fromApiToDomain(any(OrderApi.class))).thenReturn(createdOrderMock);
        when(OrderMapper.fromDomainToApi(any(Order.class))).thenReturn(createdOrderApiMock);

        String response = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Order #1\",\"total_price_amount\":100,\"total_quantity_items\":2}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderApi OrderResponse = TestUtils.readValue(response, OrderApi.class);

        assertEquals(createdOrderMock.getId(), OrderResponse.getId());
        assertEquals(createdOrderMock.getName(), OrderResponse.getName());
        assertEquals(createdOrderMock.getTotalPriceAmount(), OrderResponse.getTotalPriceAmount());
        assertEquals(createdOrderMock.getTotalQuantityItems(), OrderResponse.getTotalQuantityItems());
    }

    @Test
    void whenPostOrderWithInvalidData_thenReturns400() throws Exception {
        String invalidOrderJson = "{ \"total_price_amount\": 10.0 }";

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidOrderJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenGetOrders_thenReturnOrders() throws Exception {

        when(OrderService.findAllOrders()).thenReturn(List.of(createdOrderMock));
        when(OrderMapper.fromDomainToApi(createdOrderMock)).thenReturn(createdOrderApiMock);

        String response = mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderApiResponse OrderList = TestUtils.readValue(response, OrderApiResponse.class);

        assertEquals(createdOrderMock.getId(), OrderList.getOrders().getFirst().getId());
        assertEquals(createdOrderMock.getName(), OrderList.getOrders().getFirst().getName());
        assertEquals(createdOrderMock.getTotalQuantityItems(), OrderList.getOrders().getFirst().getTotalQuantityItems());
        assertEquals(createdOrderMock.getTotalPriceAmount(), OrderList.getOrders().getFirst().getTotalPriceAmount());
    }

    @Test
    public void whenGetOrderById_thenReturnOrder() throws Exception {

        when(OrderService.findById(ORDER_ID)).thenReturn(createdOrderMock);
        when(OrderMapper.fromDomainToApi(createdOrderMock)).thenReturn(createdOrderApiMock);

        String response = mockMvc.perform(get("/api/orders/{id}", ORDER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderApi Order = TestUtils.readValue(response, OrderApi.class);

        assertEquals(createdOrderMock.getId(), Order.getId());
        assertEquals(createdOrderMock.getName(), Order.getName());
        assertEquals(createdOrderMock.getTotalPriceAmount(), Order.getTotalPriceAmount());
        assertEquals(createdOrderMock.getTotalQuantityItems(), Order.getTotalQuantityItems());
    }

    @Test
    public void whenUpdateOrderById_thenOrderUpdatedIsReturned() throws Exception {
        Order updatedOrder = buildMockOrder();
        updatedOrder.setId(ORDER_ID);

        when(OrderMapper.fromApiToDomain(any(OrderApi.class))).thenReturn(createdOrderMock);
        when(OrderService.updateOrder(createdOrderMock)).thenReturn(updatedOrder);
        when(OrderMapper.fromDomainToApi(updatedOrder)).thenReturn(createdOrderApiMock);

        String response = mockMvc.perform(put("/api/orders/{id}", ORDER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Order #1\",\"total_price_amount\":100,\"total_quantity_items\":2}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderApi Order = TestUtils.readValue(response, OrderApi.class);

        assertEquals(createdOrderMock.getId(), Order.getId());
        assertEquals(createdOrderMock.getName(), Order.getName());
        assertEquals(createdOrderMock.getTotalQuantityItems(), Order.getTotalQuantityItems());
        assertEquals(createdOrderMock.getTotalPriceAmount(), Order.getTotalPriceAmount());
        verify(OrderService, times(1)).updateOrder(createdOrderMock);
    }

    @Test
    public void whenUpdateOrderWithId_thenThrowInvalidDataException() throws Exception {

        mockMvc.perform(put("/api/orders/{id}", ORDER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"4587\",\"name\":\"Order #1\",\"total_price_amount\":100,\"total_quantity_items\":2}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(InvalidDataException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals("Order id cannot be updated.",
                        result.getResolvedException().getMessage()));
    }

    @Test
    void whenDeleteOrderExists_thenStatusNoContent() throws Exception {

        when(OrderService.deleteById(ORDER_ID)).thenReturn(true);

        mockMvc.perform(delete("/api/orders/{id}", ORDER_ID))
                .andExpect(status().isNoContent());

        verify(OrderService, times(1)).deleteById(ORDER_ID);
    }

    @Test
    void whenDeleteOrderDoesNotExist_thenStatusNotFound() throws Exception {

        when(OrderService.deleteById(ORDER_ID)).thenReturn(false);

        mockMvc.perform(delete("/api/orders/{id}", ORDER_ID))
                .andExpect(status().isNotFound());

        verify(OrderService, times(1)).deleteById(ORDER_ID);
    }

    private static @NotNull OrderApi buildMockOrderApi() {
        OrderApi orderApi = new OrderApi();
        orderApi.setId(ORDER_ID);
        orderApi.setName("Test Order");
        orderApi.setTotalPriceAmount(100.0);
        orderApi.setTotalQuantityItems(2);
        return orderApi;
    }

    private static @NotNull Order buildMockOrder() {
        Order Order = new Order();
        Order.setId(ORDER_ID);
        Order.setName("Test Order");
        Order.setTotalPriceAmount(100.0);
        Order.setTotalQuantityItems(2);
        return Order;
    }
}
