package com.shobhit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.shobhit.constants.InventoryStatus;
import com.shobhit.constants.PaymentStatus;
import com.shobhit.event.OrderPlacedEvent;
import com.shobhit.event.OrderProcessEvent;
import com.shobhit.event.OrderStatus;
import com.shobhit.model.InventoryRequestBean;
import com.shobhit.model.InventoryResponseBean;
import com.shobhit.model.PaymentRequestBean;
import com.shobhit.model.PaymentResponseBean;
import com.shobhit.process.InventoryWorkFlowStep;
import com.shobhit.process.PaymentWorkFlowStep;

@Service
public class OrderOrchestratorService {

	@Autowired
	@Qualifier("payment")
	private WebClient paymentClient;

	@Autowired
	@Qualifier("inventory")
	private WebClient inventoryClient;

	public OrderProcessEvent processOrder(OrderPlacedEvent placedEvent) {
		OrderProcessEvent processEvent = new OrderProcessEvent();
		processEvent.setOrderId(placedEvent.getOrderId());
		processEvent.setUserId(placedEvent.getUserId());
		processEvent.setInventoryId(placedEvent.getInventoryId());
		processEvent.setQuantity(placedEvent.getQuantity());
		processEvent.setOrderAmount(placedEvent.getOrderAmount());

		PaymentRequestBean paymentRequestBean = new PaymentRequestBean(placedEvent.getOrderId(), placedEvent.getUserId(), placedEvent.getOrderAmount());
		PaymentWorkFlowStep paymentStep = new PaymentWorkFlowStep(paymentRequestBean, paymentClient);

		PaymentResponseBean paymentResponseBean = paymentStep.process();
		processEvent.setTransactionIdPayment(paymentResponseBean.getTransactionId());
		if (paymentResponseBean.getPaymentStatus() == PaymentStatus.Payment_Received) {
			InventoryRequestBean inventoryRequestBean = new InventoryRequestBean(placedEvent.getOrderId(), placedEvent.getInventoryId(), placedEvent.getQuantity(), placedEvent.getUserId(), placedEvent.getOrderAmount());
			InventoryWorkFlowStep inventoryStep = new InventoryWorkFlowStep(inventoryRequestBean, inventoryClient);

			InventoryResponseBean inventoryResponseBean = inventoryStep.process();
			processEvent.setTransactionIdInventory(inventoryResponseBean.getTransactionId());
			if (inventoryResponseBean.getInventoryStatus() == InventoryStatus.Inventory_Available) {
				processEvent.setOrderStatus(OrderStatus.Order_Placed);
			} else {
				paymentResponseBean = paymentStep.revert();
				processEvent.setTransactionIdPaymentSettlement(paymentResponseBean.getTransactionId());
				processEvent.setOrderStatus(OrderStatus.Order_Cancel_Inventory);
			}
		} else {
			processEvent.setOrderStatus(OrderStatus.Order_Cancel_Payment);
		}

		return processEvent;
	}
}