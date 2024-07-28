package com.teamviewer.ecommerce.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    private String id;
    private Order order;
    private Product product;
    private int quantity;
    private double discount;
    private double finalPrice;
}
