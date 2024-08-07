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

    @Override
    public void sendMessage(String statementId, Theme theme, String logId) {
        String address = statementService.getStatementById(statementId,logId).getClientId().getEmail();
        EmailMessageDto emailMessageDto = new EmailMessageDto(address,theme,UUID.fromString(statementId));

        template.send(theme.getValue(), emailMessageDto);
    }
}
