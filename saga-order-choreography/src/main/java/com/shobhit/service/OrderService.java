package com.shobhit.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.shobhit.constants.InventoryStatus;
import com.shobhit.constants.OrderStatus;
import com.shobhit.constants.PaymentStatus;
import com.shobhit.entity.Order;
import com.shobhit.event.InventoryEvent;
import com.shobhit.event.OrderEvent;
import com.shobhit.event.PaymentEvent;
import com.shobhit.repository.OrderRepository;

import lombok.Synchronized;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Value("${kafka.topic.name.order}")
	private String topicName;

	@Autowired
	private KafkaTemplate<String, OrderEvent> kafkaTemplate;

	private final Object lock = new Object();

	public void createOrder(Order order) {
		if(order.getOrderId() != 0) {
			Order orderExist = orderRepository.findById(order.getOrderId()).get();
			if(orderExist != null) {
				System.out.println("Order Already Exist - " + orderExist);
				return;
			}
		}

		order.setOrderStatus(OrderStatus.Order_Created.name());
		orderRepository.save(order);

		OrderEvent orderEvent = new OrderEvent(order, OrderStatus.Order_Created);
		kafkaTemplate.send(topicName, orderEvent);
	}

	public Order getOrderById(int orderId) {
		return orderRepository.findById(orderId).orElse(null);
	}

	public List<Order> getOrders() {
		return orderRepository.findAll();
	}

	@KafkaListener(topics = "${kafka.topic.name.payment}", groupId = "${kafka.group.id.payment}", containerFactory = "listenerContainerFactoryPayment")
	public void consumePayment(PaymentEvent paymentEvent) {
		System.out.println("Consumed PaymentEvent Message - " + paymentEvent);
		processPayment(paymentEvent);
	}

	@KafkaListener(topics = "${kafka.topic.name.inventory}", groupId = "${kafka.group.id.inventory}", containerFactory = "listenerContainerFactoryInventory")
	public void consumeInventory(InventoryEvent inventoryEvent) {
		System.out.println("Consumed InventoryEvent Message - " + inventoryEvent);
		processInventory(inventoryEvent);
	}

	@Transactional
	@Synchronized(value="lock")
	private void processPayment(PaymentEvent paymentEvent) {
		Order order = orderRepository.findById(paymentEvent.getOrderId()).orElse(null);

		if(order != null) {
			PaymentStatus status = paymentEvent.getPaymentStatus();

			if(status == PaymentStatus.Payment_Received) {
				order.setTransactionIdPayment(paymentEvent.getTransactionId());
				if(order.getTransactionIdInventory() != 0)
					order.setOrderStatus(OrderStatus.Order_Placed.name());

				orderRepository.save(order);
			} else if(status == PaymentStatus.Payment_Rejected) {
				order.setTransactionIdPayment(paymentEvent.getTransactionId());
				orderRepository.save(order);

				OrderEvent orderEvent = new OrderEvent(order, OrderStatus.Order_Cancel_Inventory);
				kafkaTemplate.send(topicName, orderEvent);
			}  else if(status == PaymentStatus.Payment_Settled) {
				order.setTransactionIdPaymentSettlement(paymentEvent.getTransactionId());
				order.setOrderStatus(OrderStatus.Order_Cancel.name());
				orderRepository.save(order);
			}
		}
	}

	@Transactional
	@Synchronized(value="lock")
	private void processInventory(InventoryEvent inventoryEvent) {
		Order order = orderRepository.findById(inventoryEvent.getOrderId()).orElse(null);

		if(order != null) {
			InventoryStatus status = inventoryEvent.getInventoryStatus();

			if(status == InventoryStatus.Inventory_Available) {
				order.setTransactionIdInventory(inventoryEvent.getTransactionId());
				if(order.getTransactionIdPayment() != 0)
					order.setOrderStatus(OrderStatus.Order_Placed.name());

				orderRepository.save(order);
			} else if(status == InventoryStatus.Inventory_Unavailable) {
				order.setTransactionIdInventory(inventoryEvent.getTransactionId());
				orderRepository.save(order);

				OrderEvent orderEvent = new OrderEvent(order, OrderStatus.Order_Cancel_Payment);
				kafkaTemplate.send(topicName, orderEvent);
			}  else if(status == InventoryStatus.Inventory_Settled) {
				order.setTransactionIdInventorySettlement(inventoryEvent.getTransactionId());
				order.setOrderStatus(OrderStatus.Order_Cancel.name());
				orderRepository.save(order);
			}
		}
	}
}