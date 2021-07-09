package com.shobhit.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.shobhit.constants.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="USER_TRANSACTION")
public class UserTransaction {
	@Id
	@GeneratedValue
	private int transactionId;
	private int orderId;
	private int userId;
	private double amount;
	private String paymentStatus;

	public UserTransaction(int orderId, int userId, double amount, PaymentStatus paymentStatus) {
		this.orderId = orderId;
		this.userId = userId;
		this.amount = amount;
		this.paymentStatus = paymentStatus.name();
	}
}