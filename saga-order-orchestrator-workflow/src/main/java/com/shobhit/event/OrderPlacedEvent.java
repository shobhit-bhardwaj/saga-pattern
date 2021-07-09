package com.shobhit.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPlacedEvent {
	private int orderId;
	private int userId;
	private int inventoryId;
	private int quantity;
	private double orderAmount;
	private OrderStatus orderStatus;
}