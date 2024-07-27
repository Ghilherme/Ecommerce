package com.teamviewer.ecommerce.controllers;

import com.ecommerce.model.OrderApi;
import com.ecommerce.model.OrderApiResponse;
import com.teamviewer.ecommerce.domain.Order;
import com.teamviewer.ecommerce.exceptions.InvalidDataException;
import com.teamviewer.ecommerce.mappers.OrderMapper;
import com.teamviewer.ecommerce.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping("/orders")
    public ResponseEntity<OrderApiResponse> findAll() {
        List<OrderApi> orders = orderService.findAllOrders()
                .stream()
                .map(orderMapper::fromDomainToApi)
                .toList();
        OrderApiResponse response = new OrderApiResponse();
        response.setOrders(orders);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderApi> findById(@PathVariable String id) {
        Order order = orderService.findById(id);

        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        OrderApi response = orderMapper.fromDomainToApi(order);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderApi> createOrder(@RequestBody OrderApi order) {
        if (order.getTotalPriceAmount() == null || order.getTotalQuantityItems() == null) {
            throw new InvalidDataException("Total price amount or Total quantity items cannot be null");
        }

        order.setId(null);
        Order created = orderService.createOrder(orderMapper.fromApiToDomain(order));
        OrderApi response = orderMapper.fromDomainToApi(created);

        return ResponseEntity.created(UriComponentsBuilder.fromPath("/api/orders/{id}")
                        .buildAndExpand(response.getId())
                        .toUri())
                .body(response);
    }

    @PutMapping("/orders/{id}")
    public ResponseEntity<OrderApi> updateOrder(@RequestBody OrderApi order, @PathVariable String id) {
        if (order.getId() != null) {
            throw new InvalidDataException("Order id cannot be updated.");
        }

        order.setId(id);
        Order f = orderMapper.fromApiToDomain(order);
        Order created = orderService.updateOrder(f);

        OrderApi response = orderMapper.fromDomainToApi(created);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        boolean isDeleted = orderService.deleteById(id);

        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
