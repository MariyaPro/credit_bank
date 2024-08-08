package com.prokofeva.dossier_api.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SenderServiceImplTest {
    @Mock
    private JavaMailSender mailSender;
    @InjectMocks
    private SenderServiceImpl senderService;

    @Test
    void sendMailMessage() {
        Session session = Session.getDefaultInstance(System.getProperties());
        MimeMessage message = new MimeMessage(session);

        senderService.sendMailMessage(message, anyString());

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
}