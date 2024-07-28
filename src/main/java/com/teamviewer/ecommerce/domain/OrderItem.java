package com.teamviewer.ecommerce.domain;

import lombok.Data;


@Data
public class OrderItem {

    private String id;
    private Order order;
    private Product product;
    private int quantity;
    private double discount;
    private double finalPrice;
}
