package com.teamviewer.ecommerce.repositories;

import com.teamviewer.ecommerce.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, String> {

    long count();
}
