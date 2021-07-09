package com.shobhit.event;

import com.shobhit.constants.OrderStatus;
import com.shobhit.entity.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
	private int orderId;
	private int userId;
	private int inventoryId;
	private int quantity;
	private double orderAmount;
	private OrderStatus orderStatus;

	public OrderEvent(Order order, OrderStatus orderStatus) {
		this.orderId = order.getOrderId();
		this.userId = order.getUserId();
		this.inventoryId = order.getInventoryId();
		this.quantity = order.getQuantity();
		this.orderAmount = order.getOrderAmount();
		this.orderStatus = orderStatus;
	}
}