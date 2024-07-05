package com.prokofeva.deal_api.config;

import com.prokofeva.deal_api.dto.EmailMessageDto;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value("${io.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public Map<String, Object> producerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return props;
    }

    @Bean
    public ProducerFactory<String, EmailMessageDto> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<String, EmailMessageDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NewTopic finishRegistration() {
        return new NewTopic("finish-registration", 1, (short) 1);
    }

    @Bean
    public NewTopic createDocuments() {
        return new NewTopic("create-documents", 1, (short) 1);
    }

    @Bean
    public NewTopic sendDocuments() {
        return new NewTopic("send-documents", 1, (short) 1);
    }

    @Bean
    public NewTopic sendSes() {
        return new NewTopic("send-ses", 1, (short) 1);
    }

    @Bean
    public NewTopic creditIssued() {
        return new NewTopic("credit-issued", 1, (short) 1);
    }

    @Bean
    public NewTopic statementDenied() {
        return new NewTopic("statement-denied", 1, (short) 1);
    }

}