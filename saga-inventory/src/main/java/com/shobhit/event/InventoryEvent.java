package com.shobhit.event;

import com.shobhit.constants.InventoryStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEvent {
	private int orderId;
	private int transactionId;
	private InventoryStatus inventoryStatus;
}