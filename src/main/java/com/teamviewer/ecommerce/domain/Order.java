package com.teamviewer.ecommerce.domain;

import lombok.Data;


@Data
public class Order {

    private String id;

    private String name;

    private double totalPriceAmount;

    private int totalQuantityItems;
}
