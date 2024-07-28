package com.teamviewer.ecommerce.repositories;

import com.teamviewer.ecommerce.entity.OrderItemEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends CrudRepository<OrderItemEntity, String> {
}
