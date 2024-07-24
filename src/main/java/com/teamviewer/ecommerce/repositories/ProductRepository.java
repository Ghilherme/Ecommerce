package com.teamviewer.ecommerce.repositories;

import com.teamviewer.ecommerce.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, String>{
}
