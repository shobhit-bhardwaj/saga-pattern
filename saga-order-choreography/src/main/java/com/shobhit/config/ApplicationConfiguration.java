package com.shobhit.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.shobhit.event.InventoryEvent;
import com.shobhit.event.OrderEvent;
import com.shobhit.event.PaymentEvent;

@Configuration
public class ApplicationConfiguration {

	@Value("${kafka.server.host}:${kafka.server.port}")
	private String kafkaServer;

	@Value("${kafka.group.id.payment}")
	private String groupIdPayment;

	@Value("${kafka.group.id.inventory}")
	private String groupIdInventory;

	public ProducerFactory<String, OrderEvent> producerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

		return new DefaultKafkaProducerFactory<String, OrderEvent>(config);
	}

	@Bean
	public KafkaTemplate<String, OrderEvent> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	public ConsumerFactory<String, PaymentEvent> consumerFactoryPayment() {
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, groupIdPayment);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

		return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new JsonDeserializer<>(PaymentEvent.class));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> listenerContainerFactoryPayment() {
		ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactoryPayment());

		return factory;
	}

	@Bean
	public ConsumerFactory<String, InventoryEvent> consumerFactoryInventory() {
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, groupIdInventory);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

		return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new JsonDeserializer<>(InventoryEvent.class));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, InventoryEvent> listenerContainerFactoryInventory() {
		ConcurrentKafkaListenerContainerFactory<String, InventoryEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactoryInventory());

		return factory;
	}
}