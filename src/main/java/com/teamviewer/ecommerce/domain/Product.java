package com.teamviewer.ecommerce.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Data
@Validated
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Valid
    private String id;

    @NotNull(message = "Name cannot be null")
    private String name;

    private String description;

    @NotNull(message = "Price cannot be null")
    private Double price;
}
