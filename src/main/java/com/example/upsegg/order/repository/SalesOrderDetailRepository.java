package com.example.upsegg.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.upsegg.order.entity.SalesOrderDetail;

public interface SalesOrderDetailRepository extends JpaRepository<SalesOrderDetail, Long> {

}
