package com.shobhit.service;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shobhit.constants.InventoryStatus;
import com.shobhit.entity.Inventory;
import com.shobhit.entity.InventoryTransaction;
import com.shobhit.model.InventoryRequestBean;
import com.shobhit.model.InventoryResponseBean;
import com.shobhit.repository.InventoryRepository;
import com.shobhit.repository.InventoryTransactionRepository;

@Service
public class InventoryService {

	@Autowired
	private InventoryRepository inventoryRepository;

	@Autowired
	private InventoryTransactionRepository inventoryTransactionRepository;

	@PostConstruct
	public void loadInventoryData() {
		List<Inventory> inventories = Arrays.asList(
				new Inventory("Laptop", 12),
				new Inventory("Mobile", 15),
				new Inventory("Ear Phone", 18),
				new Inventory("Charger", 20),
				new Inventory("Speakers", 25));
		inventoryRepository.saveAll(inventories);
	}

	@Transactional
	public InventoryResponseBean newInventoryTransaction(InventoryRequestBean requestBean) {
		List<InventoryTransaction> transactions = inventoryTransactionRepository.findByOrderId(requestBean.getOrderId());

		if(transactions == null || transactions.size() == 0) {
			Inventory inventory = inventoryRepository.findById(requestBean.getInventoryId()).orElse(null);

			if(inventory != null) {
				int quantity = inventory.getQuantity();

				if(quantity < requestBean.getQuantity()) {
					InventoryTransaction inventoryTransaction = new InventoryTransaction(requestBean.getOrderId(), requestBean.getUserId(), requestBean.getInventoryId(), requestBean.getQuantity(), requestBean.getAmount(), InventoryStatus.Inventory_Unavailable);
					inventoryTransaction = inventoryTransactionRepository.save(inventoryTransaction);

					return new InventoryResponseBean(requestBean.getOrderId(), inventoryTransaction.getTransactionId(), InventoryStatus.Inventory_Unavailable);
				} else {
					inventory.setQuantity(inventory.getQuantity() - requestBean.getQuantity());
					inventoryRepository.save(inventory);

					InventoryTransaction inventoryTransaction = new InventoryTransaction(requestBean.getOrderId(), requestBean.getUserId(), requestBean.getInventoryId(), requestBean.getQuantity(), requestBean.getAmount(), InventoryStatus.Inventory_Available);
					inventoryTransaction = inventoryTransactionRepository.save(inventoryTransaction);

					return new InventoryResponseBean(requestBean.getOrderId(), inventoryTransaction.getTransactionId(), InventoryStatus.Inventory_Available);
				}
			} else {
				return new InventoryResponseBean(requestBean.getOrderId(), 0, InventoryStatus.Unknown_Transaction);
			}
		} else {
			return new InventoryResponseBean(requestBean.getOrderId(), transactions.get(0).getTransactionId(), InventoryStatus.Transaction_Already_Processed);
		}
	}

	@Transactional
	public InventoryResponseBean cancelInventoryTransaction(InventoryRequestBean requestBean) {
		List<InventoryTransaction> transactions = inventoryTransactionRepository.findByOrderId(requestBean.getOrderId());

		if(transactions != null && transactions.size() == 1 && InventoryStatus.Inventory_Available.name().equalsIgnoreCase(transactions.get(0).getInventoryStatus())) {
			Inventory inventory = inventoryRepository.findById(requestBean.getInventoryId()).orElse(null);

			if(inventory != null) {
				inventory.setQuantity(inventory.getQuantity() + requestBean.getQuantity());
				inventoryRepository.save(inventory);

				InventoryTransaction inventoryTransaction = new InventoryTransaction(requestBean.getOrderId(), requestBean.getUserId(), requestBean.getInventoryId(), requestBean.getQuantity(), requestBean.getAmount(), InventoryStatus.Inventory_Settled);
				inventoryTransaction = inventoryTransactionRepository.save(inventoryTransaction);

				return new InventoryResponseBean(requestBean.getOrderId(), inventoryTransaction.getTransactionId(), InventoryStatus.Inventory_Settled);
			} else {
				return new InventoryResponseBean(requestBean.getOrderId(), 0, InventoryStatus.Unknown_Transaction);
			}
		} else {
			return new InventoryResponseBean(requestBean.getOrderId(), 0, InventoryStatus.Transaction_Already_Processed);
		}
	}
}