package com.shobhit.config;

import java.util.function.Function;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shobhit.constants.InventoryStatus;
import com.shobhit.constants.OrderStatus;
import com.shobhit.event.InventoryEvent;
import com.shobhit.event.OrderEvent;
import com.shobhit.service.KafkaInventoryService;

@Configuration
public class InventoryConfiguration {

	@Autowired
	private KafkaInventoryService kafkaInventoryService;

	@Bean
	public Function<KStream<String, OrderEvent>, KStream<String, InventoryEvent>> inventoryProcessor() {
		return kStream -> kStream.mapValues((key, orderEvent) -> {
			InventoryEvent inventoryEvent = processOrder(orderEvent);
			return inventoryEvent;
		});
	}

	private InventoryEvent processOrder(OrderEvent orderEvent) {
		if (orderEvent.getOrderStatus() == OrderStatus.Order_Created)
			return kafkaInventoryService.newOrderEvent(orderEvent);
		else if (orderEvent.getOrderStatus() == OrderStatus.Order_Cancel_Inventory)
			return kafkaInventoryService.cancelOrderEvent(orderEvent);
		else
			return new InventoryEvent(orderEvent.getOrderId(), 0, InventoryStatus.Unknown_Event);
	}
}