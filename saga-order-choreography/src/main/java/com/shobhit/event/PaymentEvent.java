package com.shobhit.event;

import com.shobhit.constants.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent {
	private int orderId;
	private int transactionId;
	private double amount;
	private PaymentStatus paymentStatus;
}