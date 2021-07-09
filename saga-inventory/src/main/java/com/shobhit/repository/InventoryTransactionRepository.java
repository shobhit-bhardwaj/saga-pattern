package com.shobhit.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shobhit.entity.InventoryTransaction;

@Repository
public interface InventoryTransactionRepository extends CrudRepository<InventoryTransaction, Integer> {
	public List<InventoryTransaction> findByOrderId(int orderId);
}