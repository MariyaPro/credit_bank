package com.prokofeva.dossier_api.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prokofeva.dossier_api.service.DossierService;
import com.prokofeva.dto.EmailMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DossierConsumer {
    private final DossierService dossierService;
//    private final ObjectMapper objectMapper;

    @KafkaListener(groupId = "all", topics = "${kafka.topics.topicFinReg}")
    public void sendFinReg(ConsumerRecord<String, String> record) {
        String logId = UUID.randomUUID().toString();
        log.info("{} -- Прочитано сообщение из топика {}.", logId,"${kafka.topics.topicFinReg}");
        read(record,logId);
    }

    @KafkaListener(groupId = "all", topics = "${kafka.topics.topicCreateDoc}")
    public void sendCreateDoc(ConsumerRecord<String, String> record)  {
        String logId = UUID.randomUUID().toString();
        log.info("{} -- Прочитано сообщение из топика {}.", logId,"${kafka.topics.topicCreateDoc}");
        read(record,logId);
    }

    @KafkaListener(groupId = "all", topics = "${kafka.topics.topicSendDoc}")
    public void sendDoc(ConsumerRecord<String, String> record) {
        String logId = UUID.randomUUID().toString();
        log.info("{} -- Прочитано сообщение из топика {}.", logId,"${kafka.topics.topicSendDoc}");
        read(record,logId);
    }

    @KafkaListener(groupId = "all", topics = "${kafka.topics.topicSendSes}")
    public void sendSes(ConsumerRecord<String, String> record) {
        String logId = UUID.randomUUID().toString();
        log.info("{} -- Прочитано сообщение из топика {}.", logId,"${kafka.topics.topicSendSes}");
        read(record,logId);
    }

    @KafkaListener(groupId = "all", topics = "${kafka.topics.topicCreditIssued}")
    public void sendCreditIssued(ConsumerRecord<String, String> record) {
        String logId = UUID.randomUUID().toString();
        log.info("{} -- Прочитано сообщение из топика {}.", logId,"${kafka.topics.topicCreditIssued}");
        read(record,logId);
    }

    @KafkaListener(groupId = "all", topics = "${kafka.topics.topicDenied}")
    public void sendDenied(ConsumerRecord<String, String> record) {
        String logId = UUID.randomUUID().toString();
        log.info("{} -- Прочитано сообщение из топика {}.", logId,"${kafka.topics.topicDenied}");
        read(record,logId);
    }

    private void read(ConsumerRecord<String,String> record, String logId){
        EmailMessageDto emailMessageDto;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            emailMessageDto = objectMapper.readValue(record.value().getBytes(StandardCharsets.UTF_8), EmailMessageDto.class);
        } catch (IOException e) {
            log.error("{} -- Перехвачено исключение при десериализации записи ({})",logId,record);
            throw new RuntimeException(e);
        }
        log.info("{} -- Десериализация прошла успешно: {} .", logId, emailMessageDto);
        dossierService.sendMessageToClient(emailMessageDto, logId);
    }
}