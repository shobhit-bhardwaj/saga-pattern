package com.shobhit.model;

import com.shobhit.constants.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseBean {
	private int orderId;
	private int transactionId;
	private PaymentStatus paymentStatus;
}