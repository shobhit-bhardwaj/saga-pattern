package com.shobhit.model;

import com.shobhit.constants.InventoryStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponseBean {
	private int orderId;
	private int transactionId;
	private InventoryStatus inventoryStatus;
}