package com.shobhit.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.shobhit.entity.UserTransaction;

public interface UserTransactionRepository extends CrudRepository<UserTransaction, Integer> {
	public List<UserTransaction> findByOrderId(int orderId);
}