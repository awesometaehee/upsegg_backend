package com.example.upsegg.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.upsegg.product.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
