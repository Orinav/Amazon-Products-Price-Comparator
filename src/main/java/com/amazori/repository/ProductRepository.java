package com.amazori.repository;

import com.amazori.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Tell Spring this is our Database access component
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String>
{
    // That's it! No code needed inside here for now.
}