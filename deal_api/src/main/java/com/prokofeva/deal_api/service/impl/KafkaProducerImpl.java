package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.service.KafkaProducer;
import com.prokofeva.deal_api.service.StatementService;
import com.prokofeva.dto.EmailMessageDto;
import com.prokofeva.enums.Theme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerImpl implements KafkaProducer {
    private final KafkaTemplate<String, EmailMessageDto> template;
    private final StatementService statementService;
//    private final ObjectMapper objectMapper;

    @Override
    public void sendMessage(String statementId, Theme theme) {
        String address = statementService.getStatementById(statementId).getClientId().getEmail();
        EmailMessageDto emailMessageDto = new EmailMessageDto(address,theme,UUID.fromString(statementId));

//        Bytes bytes = null;
//        try {
//            bytes = new Bytes(objectMapper.writeValueAsBytes(EmailMessageDto.class));
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        kafkaTemplate.send("topic",bytes);
        template.send(theme.getValue(), emailMessageDto);
    }
}
