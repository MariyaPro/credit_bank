package com.prokofeva.dossier_api.controller;

import com.prokofeva.dossier_api.service.DossierService;
import com.prokofeva.dto.EmailMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MailController {
    private final DossierService dossierService;

    @PostMapping("/test_message")
    public ResponseEntity<String> testMessage(@RequestBody EmailMessageDto emailMessageDto) throws MessagingException {
        log.info("Попытка отравить сообщение: {}", "kk");
//        EmailMessageDto emailMessageDto = EmailMessageDto.builder()
//                .address("mariaprokof@gmail.com")
//                .theme(Theme.SEND_DOCUMENTS.SEND_DOCUMENTS)
//                .statementId(UUID.randomUUID())
//                .build();

        dossierService.sendMessageToClient(emailMessageDto);
        return ResponseEntity.ok("вроде норм, проверь почту");
    }
}
