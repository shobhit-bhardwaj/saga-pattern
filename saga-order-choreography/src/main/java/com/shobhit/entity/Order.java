package com.shobhit.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.shobhit.constants.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="ORDER_MASTER")
public class Order {
	@Id
	@GeneratedValue
	private int orderId;
	private int userId;
	private int inventoryId;
	private int quantity;
	private double orderAmount;
	private int transactionIdPayment;
	private int transactionIdPaymentSettlement;
	private int transactionIdInventory;
	private int transactionIdInventorySettlement;
	private String orderStatus;

	public Order(int inventoryId, int quantity, int userId, double orderAmount, OrderStatus orderStatus) {
		this.inventoryId = inventoryId;
		this.quantity = quantity;
		this.userId = userId;
		this.orderAmount = orderAmount;
		this.orderStatus = orderStatus.name();
	}
}