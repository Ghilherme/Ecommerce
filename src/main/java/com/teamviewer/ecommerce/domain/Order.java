package com.teamviewer.ecommerce.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private String id;

    private String name;

    private double totalPriceAmount;

    private int totalQuantityItems;
}
