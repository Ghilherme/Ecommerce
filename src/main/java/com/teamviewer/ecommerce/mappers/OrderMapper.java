package com.teamviewer.ecommerce.mappers;

import com.ecommerce.model.OrderApi;
import com.teamviewer.ecommerce.domain.Order;
import com.teamviewer.ecommerce.entity.OrderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order fromApiToDomain(OrderApi orderApi);

    OrderEntity fromDomainToEntity(Order productDomain);

    Order fromEntityToDomain(OrderEntity orderEntity);

    OrderApi fromDomainToApi(Order order);
}
