package com.prokofeva.dossier_api.service.impl;

import com.prokofeva.dossier_api.service.SenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class SenderServiceImpl implements SenderService {
    private final JavaMailSender mailSender;

    @Override
    public void sendMailMessage(MimeMessage message) {
        mailSender.send(message);
    }
}
