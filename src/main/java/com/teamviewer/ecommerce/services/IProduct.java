package com.teamviewer.ecommerce.services;

import com.teamviewer.ecommerce.domain.Product;

import java.util.List;

public interface IProduct {

    List<Product> findAllProducts();
}
