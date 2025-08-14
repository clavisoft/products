package com.msproducts.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.msproducts.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

  Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
