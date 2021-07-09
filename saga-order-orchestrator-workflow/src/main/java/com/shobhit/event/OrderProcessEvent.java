package com.shobhit.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderProcessEvent {
	private int orderId;
	private int userId;
	private int inventoryId;
	private int quantity;
	private double orderAmount;
	private int transactionIdPayment;
	private int transactionIdPaymentSettlement;
	private int transactionIdInventory;
	private int transactionIdInventorySettlement;
	private OrderStatus orderStatus;
}