package com.prokofeva.deal_api.config;

import com.prokofeva.dto.EmailMessageDto;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;
    @Value(value = "${kafka.topics.topicFinReg}")
    private String topicFinReg;
    @Value(value = "${kafka.topics.topicCreateDoc}")
    private String topicCreateDoc;
    @Value(value = "${kafka.topics.topicSendDoc}")
    private String topicSendDoc;
    @Value(value = "${kafka.topics.topicSendSes}")
    private String topicSendSes;
    @Value(value = "${kafka.topics.topicCreditIssued}")
    private String topicCreditIssued;
    @Value(value = "${kafka.topics.topicDenied}")
    private String topicDenied;

    @Bean
    public ProducerFactory<String, EmailMessageDto> messageDtoProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, EmailMessageDto> kafkaTemplate() {
        return new KafkaTemplate<>(messageDtoProducerFactory());
    }

    @Bean
    public NewTopic topicFinReg() {
        return new NewTopic(topicFinReg, 1, (short) 1);
    }

    @Bean
    public NewTopic topicCreateDoc() {
        return new NewTopic(topicCreateDoc, 1, (short) 1);
    }

    @Bean
    public NewTopic topicSendDoc() {
        return new NewTopic(topicSendDoc, 1, (short) 1);
    }

    @Bean
    public NewTopic topicSendSes() {
        return new NewTopic(topicSendSes, 1, (short) 1);
    }

    @Bean
    public NewTopic topicCreditIssued() {
        return new NewTopic(topicCreditIssued, 1, (short) 1);
    }

    @Bean
    public NewTopic topicDenied() {
        return new NewTopic(topicDenied, 1, (short) 1);
    }
}
