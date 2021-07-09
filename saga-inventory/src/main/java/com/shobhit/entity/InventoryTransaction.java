package com.shobhit.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.shobhit.constants.InventoryStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="INVENTORY_TRANSACTION")
public class InventoryTransaction {
	@Id
	@GeneratedValue
	private int transactionId;
	private int orderId;
	private int userId;
	private int inventoryId;
	private int quantity;
	private double amount;
	private String inventoryStatus;

	public InventoryTransaction(int orderId, int userId, int inventoryId, int quantity, double amount,
			InventoryStatus inventoryStatus) {
		super();
		this.orderId = orderId;
		this.userId = userId;
		this.inventoryId = inventoryId;
		this.quantity = quantity;
		this.amount = amount;
		this.inventoryStatus = inventoryStatus.name();
	}
}