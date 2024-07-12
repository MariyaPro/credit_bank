package com.prokofeva.dossier_api.service.impl;

import com.prokofeva.dossier_api.service.SenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class SenderServiceImpl implements SenderService {
    private final JavaMailSender mailSender;

    @Override
    public void sendMailMessage(MimeMessage message, String logId) {
        log.info("{} -- Для отправки клиенту сформировано сообщение: {}.",logId,message);
        mailSender.send(message);
    }
}
