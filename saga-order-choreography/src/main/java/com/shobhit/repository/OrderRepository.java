package com.shobhit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shobhit.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}