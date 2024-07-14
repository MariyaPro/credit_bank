package com.prokofeva.dossier_api.service.impl;

import com.prokofeva.dossier_api.service.DossierService;
import com.prokofeva.dossier_api.service.MessageCreator;
import com.prokofeva.dossier_api.service.MessageCreatorFactory;
import com.prokofeva.dossier_api.service.SenderService;
import com.prokofeva.dto.EmailMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
@RequiredArgsConstructor
@Setter
public class DossierServiceImpl implements DossierService {
    private final JavaMailSender mailSender;
    private final SenderService senderService;
    private final MessageCreatorFactory messageCreatorFactory;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Override
    public void sendMessageToClient(EmailMessageDto emailMessageDto, String logId) {
        log.info("{} -- Необходимо отправить сообщение клиенту: {}", logId,emailMessageDto);
        senderService.sendMailMessage(createMailMessage(emailMessageDto,logId),logId);
        log.info("{} -- Сообщение отправлено.",logId);
    }

    private MimeMessage createMailMessage(EmailMessageDto emailMessageDto, String logId) {
        final MimeMessage message = mailSender.createMimeMessage();
        log.info("{} -- Формирование письма.",logId);
        try {
            final MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setFrom(emailFrom);
            log.info("{} -- Установлен адрес отправителя.",logId);
            messageHelper.setTo(emailMessageDto.getAddress());
            log.info("{} -- Установлен адрес получателя.",logId);
            messageHelper.setSubject(emailMessageDto.getTheme().getValue());
            log.info("{} -- Установлена тема сообщения.",logId);

            MessageCreator creator = messageCreatorFactory.getCreator(emailMessageDto.getTheme(),logId);
            creator.fill(messageHelper, emailMessageDto.getStatementId(), logId);
        } catch (MessagingException e) {
            log.error("{} -- Перехвачено исключении при заполнении сообщения: {}", logId, e.getMessage());
        }

        return message;
    }

}