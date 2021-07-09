package com.shobhit.config;

import java.util.function.Function;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shobhit.constants.OrderStatus;
import com.shobhit.constants.PaymentStatus;
import com.shobhit.event.OrderEvent;
import com.shobhit.event.PaymentEvent;
import com.shobhit.service.KafkaPaymentService;

@Configuration
public class PaymentConfiguration {

	@Autowired
	private KafkaPaymentService kafkaPaymentService;

	@Bean
	public Function<KStream<String, OrderEvent>, KStream<String, PaymentEvent>> paymentProcessor() {
		return kStream -> kStream.mapValues((key, orderEvent) -> {
			PaymentEvent paymentEvent = processOrder(orderEvent);
			return paymentEvent;
		});
	}

	private PaymentEvent processOrder(OrderEvent orderEvent) {
		if (orderEvent.getOrderStatus() == OrderStatus.Order_Created)
			return kafkaPaymentService.newOrderEvent(orderEvent);
		else if (orderEvent.getOrderStatus() == OrderStatus.Order_Cancel_Payment)
			return kafkaPaymentService.cancelOrderEvent(orderEvent);
		else
			return new PaymentEvent(orderEvent.getOrderId(), 0, PaymentStatus.Unknown_Event);
	}
}