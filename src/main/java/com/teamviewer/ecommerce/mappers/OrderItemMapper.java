package com.teamviewer.ecommerce.mappers;

import com.ecommerce.model.OrderItemApi;
import com.ecommerce.model.OrderItemApiRequest;
import com.ecommerce.model.ProductApi;
import com.teamviewer.ecommerce.domain.Order;
import com.teamviewer.ecommerce.domain.OrderItem;
import com.teamviewer.ecommerce.domain.Product;
import com.teamviewer.ecommerce.entity.OrderEntity;
import com.teamviewer.ecommerce.entity.OrderItemEntity;
import com.teamviewer.ecommerce.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {OrderMapper.class, ProductMapper.class})
public interface OrderItemMapper {

    @Mapping(source = "orderId", target = "order", qualifiedByName = "mapIdToOrderItem")
    @Mapping(source = "productId", target = "product", qualifiedByName = "mapIdToProduct")
    OrderItem fromApiToDomain(OrderItemApiRequest orderItemApi);

    @Mapping(source = "order.id", target = "order", qualifiedByName = "mapIdToOrderEntity")
    @Mapping(source = "product.id", target = "product", qualifiedByName = "mapIdToProductEntity")
    OrderItemEntity fromDomainToEntity(OrderItem orderItem);

    @Mapping(source = "order.id", target = "order.id")
    @Mapping(source = "product.id", target = "product.id")
    OrderItem fromEntityToDomain(OrderItemEntity orderItemEntity);

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "product.id", target = "productId")
    OrderItemApi fromDomainToApi(OrderItem orderItem);


    @Named("mapIdToOrderItem")
    default Order mapIdToOrder(String orderId) {
        if (orderId == null) {
            return null;
        }
        Order order = new Order();
        order.setId(orderId);
        return order;
    }

    @Named("mapIdToOrderEntity")
    default OrderEntity mapIdToOrderEntity(String orderId) {
        if (orderId == null) {
            return null;
        }
        OrderEntity order = new OrderEntity();
        order.setId(orderId);
        return order;
    }

    @Named("mapIdToProduct")
    default Product mapIdToProduct(String productId) {
        if (productId == null) {
            return null;
        }
        Product product = new Product();
        product.setId(productId);
        return product;
    }

    @Named("mapIdToProductEntity")
    default ProductEntity mapIdToProductEntity(String productId) {
        if (productId == null) {
            return null;
        }
        ProductEntity product = new ProductEntity();
        product.setId(productId);
        return product;
    }


}
