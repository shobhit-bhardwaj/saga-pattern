package com.shobhit.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.shobhit.entity.Order;
import com.shobhit.event.OrderPlacedEvent;
import com.shobhit.event.OrderProcessEvent;
import com.shobhit.event.OrderStatus;
import com.shobhit.repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Value("${kafka.topic.name.order.placed}")
	private String topicName;

	@Autowired
	private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

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

		OrderPlacedEvent orderEvent = new OrderPlacedEvent(order, OrderStatus.Order_Created);
		kafkaTemplate.send(topicName, orderEvent);
	}

	public Order getOrderById(int orderId) {
		return orderRepository.findById(orderId).orElse(null);
	}

	public List<Order> getOrders() {
		return orderRepository.findAll();
	}

	@KafkaListener(topics = "${kafka.topic.name.order.process}", groupId = "${kafka.group.id.order}", containerFactory = "listenerContainerFactoryOrder")
	public void consumePayment(OrderProcessEvent orderEvent) {
		System.out.println("Consumed OrderEvent Message - " + orderEvent);
		processOrder(orderEvent);
	}

	@Transactional
	private void processOrder(OrderProcessEvent orderEvent) {
		if(orderEvent.getOrderId() != 0) {
			Order order = orderRepository.findById(orderEvent.getOrderId()).get();
			if(order != null) {
				order.setTransactionIdPayment(orderEvent.getTransactionIdPayment());
				order.setTransactionIdPaymentSettlement(orderEvent.getTransactionIdPaymentSettlement());
				order.setTransactionIdInventory(orderEvent.getTransactionIdInventory());
				order.setTransactionIdInventorySettlement(orderEvent.getTransactionIdInventorySettlement());

				if(orderEvent.getOrderStatus() == OrderStatus.Order_Cancel_Payment || orderEvent.getOrderStatus() == OrderStatus.Order_Cancel_Inventory)
					order.setOrderStatus(OrderStatus.Order_Cancel.name());
				else
					order.setOrderStatus(orderEvent.getOrderStatus().name());

				orderRepository.save(order);
			}
		}
	}
}