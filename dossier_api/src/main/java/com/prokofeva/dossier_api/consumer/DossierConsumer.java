package com.prokofeva.dossier_api.consumer;

import com.prokofeva.dossier_api.service.DossierService;
import com.prokofeva.dto.EmailMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DossierConsumer {
    private final DossierService dossierService;
    @Value("${spring.application.name}")
    private String appName;

    @KafkaListener(groupId = "all", topics = "${kafka.topics.topicFinReg}", containerFactory = "kafkaListenerContainerFactory")
    public void sendFinReg(@Payload EmailMessageDto messageDto) {
        String logId = String.join(":", appName, UUID.randomUUID().toString());
        log.info("{} -- Прочитано сообщение из топика {}.", logId, "${kafka.topics.topicFinReg}");
        read(messageDto, logId);
    }

    @KafkaListener(groupId = "all", topics = "${kafka.topics.topicCreateDoc}", containerFactory = "kafkaListenerContainerFactory")
    public void sendCreateDoc(@Payload EmailMessageDto messageDto) {
        String logId = String.join(":", appName, UUID.randomUUID().toString());
        log.info("{} -- Прочитано сообщение из топика {}.", logId, "${kafka.topics.topicCreateDoc}");
        read(messageDto, logId);
    }

    @KafkaListener(groupId = "all", topics = "${kafka.topics.topicSendDoc}", containerFactory = "kafkaListenerContainerFactory")
    public void sendDoc(@Payload EmailMessageDto messageDto) {
        String logId = String.join(":", appName, UUID.randomUUID().toString());
        log.info("{} -- Прочитано сообщение из топика {}.", logId, "${kafka.topics.topicSendDoc}");
        read(messageDto, logId);
    }

    @KafkaListener(groupId = "all", topics = "${kafka.topics.topicSendSes}", containerFactory = "kafkaListenerContainerFactory")
    public void sendSes(@Payload EmailMessageDto messageDto) {
        String logId = String.join(":", appName, UUID.randomUUID().toString());
        log.info("{} -- Прочитано сообщение из топика {}.", logId, "${kafka.topics.topicSendSes}");
        read(messageDto, logId);
    }

    @KafkaListener(groupId = "all", topics = "${kafka.topics.topicCreditIssued}", containerFactory = "kafkaListenerContainerFactory")
    public void sendCreditIssued(@Payload EmailMessageDto messageDto) {
        String logId = String.join(":", appName, UUID.randomUUID().toString());
        log.info("{} -- Прочитано сообщение из топика {}.", logId, "${kafka.topics.topicCreditIssued}");
        read(messageDto, logId);
    }

    @KafkaListener(groupId = "all", topics = "${kafka.topics.topicDenied}", containerFactory = "kafkaListenerContainerFactory")
    public void sendDenied(@Payload EmailMessageDto messageDto) {
        String logId = String.join(":", appName, UUID.randomUUID().toString());
        log.info("{} -- Прочитано сообщение из топика {}.", logId, "${kafka.topics.topicDenied}");
        read(messageDto, logId);
    }

    private void read(EmailMessageDto messageDto, String logId) {
        try {
            dossierService.sendMessageToClient(messageDto, logId);
        } catch (Exception e) {
            log.error("{} -- Ошибка при отправке уведомления.\nEmailMessageDto: {}\nCause: {}", logId, messageDto, e.getMessage());
        }
    }
}