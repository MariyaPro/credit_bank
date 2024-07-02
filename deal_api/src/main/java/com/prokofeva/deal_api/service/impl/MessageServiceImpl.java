package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.dto.EmailMessageDto;
import com.prokofeva.deal_api.enums.Theme;
import com.prokofeva.deal_api.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final KafkaTemplate<String, EmailMessageDto> kafkaTemplate;

    @Override
    public void sendMessage(String email, Theme theme, UUID statementId) {
        EmailMessageDto messageDto = EmailMessageDto.builder()
                .address(email)
                .theme(theme)
                .statementId(statementId)
                .build();
        log.info("{} -- Сообщение сформировано и отправлено: {}.", statementId, messageDto);
        kafkaTemplate.send(theme.getValue(), messageDto);
    }

}
