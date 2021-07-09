package com.shobhit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shobhit.event.OrderEvent;
import com.shobhit.event.PaymentEvent;
import com.shobhit.model.PaymentRequestBean;
import com.shobhit.model.PaymentResponseBean;

@Service
public class KafkaPaymentService {

	@Autowired
	private PaymentService paymentService;

	public PaymentEvent newOrderEvent(OrderEvent orderEvent) {
		PaymentRequestBean requestBean = new PaymentRequestBean(orderEvent.getOrderId(), orderEvent.getUserId(), orderEvent.getOrderAmount());
		PaymentResponseBean responseBean = paymentService.newPaymentTransaction(requestBean);

		return new PaymentEvent(responseBean.getOrderId(), responseBean.getTransactionId(), responseBean.getPaymentStatus());
	}

	public PaymentEvent cancelOrderEvent(OrderEvent orderEvent) {
		PaymentRequestBean requestBean = new PaymentRequestBean(orderEvent.getOrderId(), orderEvent.getUserId(), orderEvent.getOrderAmount());
		PaymentResponseBean responseBean = paymentService.cancelPaymentTransaction(requestBean);

		return new PaymentEvent(responseBean.getOrderId(), responseBean.getTransactionId(), responseBean.getPaymentStatus());
	}
}