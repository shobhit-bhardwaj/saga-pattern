package com.shobhit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequestBean {
	private int orderId;
	private int inventoryId;
	private int quantity;
	private int userId;
	private double amount;
}