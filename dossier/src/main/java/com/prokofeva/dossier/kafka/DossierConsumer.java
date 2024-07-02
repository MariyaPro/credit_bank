package com.prokofeva.dossier.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DossierConsumer {

    @KafkaListener(topics = "finish-registration", idIsGroup = false)
    public void finishRegistration(ConsumerRecord<String, String> record) {
        log.info("finishRegistration: {}",record.value());

    }

    @KafkaListener(topics = "create-documents", idIsGroup = false)
    public void createDocuments(ConsumerRecord<String, String> record) {

    }

    @KafkaListener(topics = "send-documents", idIsGroup = false)
    public void sendDocuments(ConsumerRecord<String, String> record) {

    }

    @KafkaListener(topics = "send-ses", idIsGroup = false)
    public void sendSes(ConsumerRecord<String, String> record) {

    }

    @KafkaListener(topics = "credit-issued", idIsGroup = false)
    public void creditIssued(ConsumerRecord<String, String> record) {

    }

    @KafkaListener(topics = "statement-denied", idIsGroup = false)
    public void statementDenied(ConsumerRecord<String, String> record) {

    }
}