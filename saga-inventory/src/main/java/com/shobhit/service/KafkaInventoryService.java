package com.shobhit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shobhit.event.InventoryEvent;
import com.shobhit.event.OrderEvent;
import com.shobhit.model.InventoryRequestBean;
import com.shobhit.model.InventoryResponseBean;

@Service
public class KafkaInventoryService {

	@Autowired
	private InventoryService inventoryService;

	public InventoryEvent newOrderEvent(OrderEvent orderEvent) {
		InventoryRequestBean requestBean = new InventoryRequestBean(orderEvent.getOrderId(), orderEvent.getInventoryId(), orderEvent.getQuantity(), orderEvent.getUserId(), orderEvent.getOrderAmount());
		InventoryResponseBean responseBean = inventoryService.newInventoryTransaction(requestBean);

		return new InventoryEvent(responseBean.getOrderId(), responseBean.getTransactionId(), responseBean.getInventoryStatus());
	}

	public InventoryEvent cancelOrderEvent(OrderEvent orderEvent) {
		InventoryRequestBean requestBean = new InventoryRequestBean(orderEvent.getOrderId(), orderEvent.getInventoryId(), orderEvent.getQuantity(), orderEvent.getUserId(), orderEvent.getOrderAmount());
		InventoryResponseBean responseBean = inventoryService.cancelInventoryTransaction(requestBean);

		return new InventoryEvent(responseBean.getOrderId(), responseBean.getTransactionId(), responseBean.getInventoryStatus());
	}
}