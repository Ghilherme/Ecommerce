package com.teamviewer.ecommerce.mappers;

import com.ecommerce.model.ProductApi;
import com.teamviewer.ecommerce.domain.Product;
import com.teamviewer.ecommerce.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product fromApiToDomain(ProductApi productApi);

    ProductEntity fromDomainToEntity(Product productDomain);

    Product fromEntityToDomain(ProductEntity productEntity);

    ProductApi fromDomainToApi(Product productDomain);
}
