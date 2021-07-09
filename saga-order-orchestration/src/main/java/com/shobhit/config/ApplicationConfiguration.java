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

import com.shobhit.event.OrderPlacedEvent;
import com.shobhit.event.OrderProcessEvent;

@Configuration
public class ApplicationConfiguration {

	@Value("${kafka.server.host}:${kafka.server.port}")
	private String kafkaServer;

	@Value("${kafka.group.id.order}")
	private String groupIdOrder;

	public ProducerFactory<String, OrderPlacedEvent> producerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

		return new DefaultKafkaProducerFactory<String, OrderPlacedEvent>(config);
	}

	@Bean
	public KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	public ConsumerFactory<String, OrderProcessEvent> consumerFactoryOrder() {
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, groupIdOrder);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

		return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new JsonDeserializer<>(OrderProcessEvent.class));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, OrderProcessEvent> listenerContainerFactoryOrder() {
		ConcurrentKafkaListenerContainerFactory<String, OrderProcessEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactoryOrder());

		return factory;
	}
}