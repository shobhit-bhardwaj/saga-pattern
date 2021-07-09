package com.shobhit.config;

import java.util.function.Function;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shobhit.event.OrderPlacedEvent;
import com.shobhit.event.OrderProcessEvent;
import com.shobhit.event.OrderStatus;
import com.shobhit.service.OrderOrchestratorService;

@Configuration
public class WorkflowConfiguration {

	@Autowired
	private OrderOrchestratorService orderService;

	@Bean
	public Function<KStream<String, OrderPlacedEvent>, KStream<String, OrderProcessEvent>> workflowProcessor() {
		return kStream -> kStream.mapValues((key, orderPlacedEvent) -> {
			OrderProcessEvent orderProcessEvent = processOrder(orderPlacedEvent);
			return orderProcessEvent;
		});
	}

	private OrderProcessEvent processOrder(OrderPlacedEvent orderEvent) {
		if (orderEvent.getOrderStatus() == OrderStatus.Order_Created)
			return orderService.processOrder(orderEvent);

		return null;
	}
}