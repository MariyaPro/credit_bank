package com.prokofeva.dossier_api.service.impl;

import com.prokofeva.dossier_api.service.DossierService;
import com.prokofeva.dossier_api.service.MessageCreator;
import com.prokofeva.dossier_api.service.MessageCreatorFactory;
import com.prokofeva.dossier_api.service.SenderService;
import com.prokofeva.dto.EmailMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DossierServiceImpl implements DossierService {
    private final JavaMailSender mailSender;
    private final SenderService senderService;
    private final MessageCreatorFactory messageCreatorFactory;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Override
    public void sendMessageToClient(EmailMessageDto emailMessageDto) {
        String lodId = UUID.randomUUID().toString();
        log.info("{} -- Необходимо отправить сообщение клиенту: {}", lodId,emailMessageDto);
        senderService.sendMailMessage(createMailMessage(emailMessageDto,lodId));
    }

    private MimeMessage createMailMessage(EmailMessageDto emailMessageDto, String lodId) {
        final MimeMessage message = mailSender.createMimeMessage();
        try {
            final MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setFrom(emailFrom);
            messageHelper.setTo(emailMessageDto.getAddress());
            messageHelper.setSubject(emailMessageDto.getTheme().getValue());

            MessageCreator creator = messageCreatorFactory.getCreator(emailMessageDto.getTheme(),lodId);
            creator.fill(messageHelper, emailMessageDto.getStatementId(), lodId);
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }

        return message;
    }

}