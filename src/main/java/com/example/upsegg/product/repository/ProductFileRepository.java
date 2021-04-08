package com.example.upsegg.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.upsegg.product.entity.ProductFile;

@Repository
public interface ProductFileRepository extends JpaRepository<ProductFile, Long> {
	List<ProductFile> findByProductId(long productId);
}
