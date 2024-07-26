package com.teamviewer.ecommerce.domain;


import com.teamviewer.ecommerce.configuration.ValidUUID;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Data
@Validated
public class Product {
    @ValidUUID
    @Valid
    private String id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private Double price;
}
