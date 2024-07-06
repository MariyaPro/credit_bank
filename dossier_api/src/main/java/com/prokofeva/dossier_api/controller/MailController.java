package com.prokofeva.dossier_api.controller;

import com.prokofeva.dossier_api.dto.EmailMessageDto;
import com.prokofeva.dossier_api.enums.Theme;
import com.prokofeva.dossier_api.service.DossierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MailController {
    private final DossierService dossierService;

    @GetMapping("/test_message")
    public ResponseEntity<String> testMessage() throws MessagingException {
        log.info("Попытка отравить сообщение: {}", "kk");
        EmailMessageDto emailMessageDto = EmailMessageDto.builder()
                .address("mariaprokof@gmail.com")
                .theme(Theme.SEND_DOCUMENTS)
                .statementId(UUID.randomUUID())
                .build();

        dossierService.sendMessageToClient(emailMessageDto);
        return ResponseEntity.ok("вроде норм, проверь почту");
    }
}
