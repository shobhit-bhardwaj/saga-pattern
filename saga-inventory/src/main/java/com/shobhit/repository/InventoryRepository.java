package com.shobhit.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shobhit.entity.Inventory;

@Repository
public interface InventoryRepository extends CrudRepository<Inventory, Integer> {
}