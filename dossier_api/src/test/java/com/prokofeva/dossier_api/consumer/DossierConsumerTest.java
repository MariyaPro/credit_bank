package com.prokofeva.dossier_api.consumer;

import com.prokofeva.dossier_api.service.DossierService;
import com.prokofeva.dto.EmailMessageDto;
import com.prokofeva.enums.Theme;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Testcontainers
class DossierConsumerTest {
    @Mock
    private DossierService dossierService;
    @InjectMocks
    private DossierConsumer dossierConsumer;

    private static KafkaTemplate<String, EmailMessageDto> kafkaTemplate;
    private static Consumer<String, EmailMessageDto> consumer;

    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.3.2"));

    @BeforeAll
    static void beforeAll() {
        kafkaContainer.start();
        setup();
    }

    @AfterAll
    static void afterAll() {
        kafkaContainer.stop();
    }

    @Test
    void sendFinReg() {
        Theme theme = Theme.FINISH_REGISTRATION;
        String topicName = theme.getValue();
        kafkaTemplate.send(theme.getValue(), new EmailMessageDto(
                "emailfortest@gmail.com",
                theme, UUID.randomUUID()));

        consumer.subscribe(List.of(topicName));
        ConsumerRecords<String, EmailMessageDto> records = consumer.poll(Duration.ofMillis(100000));

        assertEquals(records.count(), 1);

        for (ConsumerRecord<String, EmailMessageDto> record : records) {
            dossierConsumer.sendFinReg(record.value());
        }

        verify(dossierService, times(1)).sendMessageToClient(any(EmailMessageDto.class), anyString());
    }

    @Test
    void sendCreateDoc() {
        Theme theme = Theme.CREATE_DOCUMENTS;
        String topicName = theme.getValue();
        kafkaTemplate.send(theme.getValue(), new EmailMessageDto(
                "emailfortest@gmail.com",
                theme, UUID.randomUUID()));

        consumer.subscribe(List.of(topicName));
        ConsumerRecords<String, EmailMessageDto> records = consumer.poll(Duration.ofMillis(100000));

        assertEquals(records.count(), 1);

        for (ConsumerRecord<String, EmailMessageDto> record : records) {
            dossierConsumer.sendCreateDoc(record.value());
        }

        verify(dossierService, times(1)).sendMessageToClient(any(), anyString());
    }

    @Test
    void sendDoc() {
        Theme theme = Theme.SEND_DOCUMENTS;
        String topicName = theme.getValue();
        kafkaTemplate.send(theme.getValue(), new EmailMessageDto(
                "emailfortest@gmail.com",
                theme, UUID.randomUUID()));

        consumer.subscribe(List.of(topicName));
        ConsumerRecords<String, EmailMessageDto> records = consumer.poll(Duration.ofMillis(100000));

        assertEquals(records.count(), 1);

        for (ConsumerRecord<String, EmailMessageDto> record : records) {
            dossierConsumer.sendDoc(record.value());
        }

        verify(dossierService, times(1)).sendMessageToClient(any(), anyString());

    }

    @Test
    void sendSes() {
        Theme theme = Theme.SEND_SES;
        String topicName = theme.getValue();
        kafkaTemplate.send(theme.getValue(), new EmailMessageDto(
                "emailfortest@gmail.com",
                theme, UUID.randomUUID()));

        consumer.subscribe(List.of(topicName));
        ConsumerRecords<String, EmailMessageDto> records = consumer.poll(Duration.ofMillis(100000));

        assertEquals(records.count(), 1);

        for (ConsumerRecord<String, EmailMessageDto> record : records) {
            dossierConsumer.sendSes(record.value());
        }

        verify(dossierService, times(1)).sendMessageToClient(any(), anyString());

    }

    @Test
    void sendCreditIssued() {
        Theme theme = Theme.CREDIT_ISSUED;
        String topicName = theme.getValue();
        kafkaTemplate.send(theme.getValue(), new EmailMessageDto(
                "emailfortest@gmail.com",
                Theme.CREDIT_ISSUED,
                UUID.randomUUID()));

        consumer.subscribe(List.of(topicName));
        ConsumerRecords<String, EmailMessageDto> records = consumer.poll(Duration.ofMillis(100000));

        assertEquals(records.count(), 1);

        for (ConsumerRecord<String, EmailMessageDto> record : records) {
            dossierConsumer.sendCreditIssued(record.value());
        }
    }

    @Test
    void sendDenied() {
        Theme theme = Theme.STATEMENT_DENIED;
        String topicName = theme.getValue();
        kafkaTemplate.send(theme.getValue(), new EmailMessageDto(
                "emailfortest@gmail.com",
                Theme.STATEMENT_DENIED,
                UUID.randomUUID()));

        consumer.subscribe(List.of(topicName));
        ConsumerRecords<String, EmailMessageDto> records = consumer.poll(Duration.ofMillis(100000));

        assertEquals(records.count(), 1);

        for (ConsumerRecord<String, EmailMessageDto> record : records) {
            dossierConsumer.sendDenied(record.value());
        }

        verify(dossierService, times(1)).sendMessageToClient(any(), anyString());

    }


    static void setup() {
        String bootstrapServers = kafkaContainer.getBootstrapServers();

        Map<String, Object> configProd = new HashMap<>();
        configProd.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProd.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProd.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        kafkaTemplate = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(configProd));

        Map<String, Object> configCons = new HashMap<>();
        configCons.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configCons.put(ConsumerConfig.GROUP_ID_CONFIG, "all");
        configCons.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        ConsumerFactory<String, EmailMessageDto> factory = new DefaultKafkaConsumerFactory<>(
                configCons, new StringDeserializer(), new JsonDeserializer<>(EmailMessageDto.class));
        consumer = factory.createConsumer();
    }

}