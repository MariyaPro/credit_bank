package com.prokofeva.dossier_api.service.impl;

import com.prokofeva.dto.EmailMessageDto;
import com.prokofeva.dossier_api.service.ContentService;
import com.prokofeva.dossier_api.service.DossierService;
import com.prokofeva.dossier_api.service.FileService;
import com.prokofeva.dossier_api.service.SenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
public class DossierServiceImpl implements DossierService {
    private final JavaMailSender mailSender;
    private final SenderService senderService;
    private final FileService fileService;
    private final ContentService contentService;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Override
    public void sendMessageToClient(EmailMessageDto emailMessageDto) {

        senderService.sendMailMessage(createMailMessage(emailMessageDto));
    }

    @Override
    public MimeMessage createMailMessage(EmailMessageDto emailMessageDto) {

        final MimeMessage message = mailSender.createMimeMessage();
        try {
            final MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

            messageHelper.setFrom(emailFrom);
            messageHelper.setTo(emailMessageDto.getAddress());
            messageHelper.setSubject(emailMessageDto.getTheme().getValue());

            switch (emailMessageDto.getTheme()) {
                case FINISH_REGISTRATION -> fillContentFinReg(messageHelper);
                case CREATE_DOCUMENTS ->
                        fillContentCreateDoc(messageHelper, emailMessageDto.getStatementId().toString());
                case SEND_DOCUMENTS -> fillContentSendDoc(messageHelper, emailMessageDto.getStatementId().toString());
//            case SEND_SES ->  fillContentSes(messageHelper);
//            case CREDIT_ISSUED ->  fillContentCredit(messageHelper);
//            case STATEMENT_DENIED -> fillContentDenied(messageHelper);
                default -> throw new IllegalArgumentException("Неизвестная тема письма.");
            }
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }

        return message;
    }

    private void fillContentFinReg(MimeMessageHelper messageHelper) throws MessagingException {
        messageHelper.setText("Ваша заявка предварительно одобрена, завершите оформление.");
    }

    private void fillContentCreateDoc(MimeMessageHelper messageHelper, String statementId) throws MessagingException {
        String text = "Ваша заявка одобрена. Для получения документов перейдите по ссылке\n" +
                "http://localhost:8085/deal/document/" + statementId + "/send";
        messageHelper.setText(text);
    }

    private void fillContentSendDoc(MimeMessageHelper messageHelper, String statementId) throws MessagingException {
//        StatementDto statementDto = contentService.getInfoFromDb(statementId);
//
//        Path creditAgreementFile = fileService.createCreditAgreementFile();
//        Path questionnaireFile = fileService.createQuestionnaireFile();
//        Path paymentScheduleFile = fileService.createPaymentScheduleFile();
//
//        messageHelper.addAttachment(String.valueOf(creditAgreementFile.getFileName()), creditAgreementFile.toFile());
//        messageHelper.addAttachment(String.valueOf(questionnaireFile.getFileName()), questionnaireFile.toFile());
//        messageHelper.addAttachment(String.valueOf(paymentScheduleFile.getFileName()), paymentScheduleFile.toFile());


        String text = "Направляем документы на ознакомление:\n" +
                "1. Кредитный договор - внимательно ознакомьтесь с условиями.\n" +
                "2. Анкету с вашими данными - Проверьте правильность указанных данных.\n" +
                "3. График ежемесячных платежей.\n" +
                "Если вы согласны с условиями предоставления кредита, перейдите по ссылке:\n" +
                "http://localhost:8085/deal/document/" + statementId + "/sing";
        messageHelper.setText(text);

        //for test
         messageHelper.addAttachment("statementId", Paths.get("/home/dev/IdeaProjects/Neo-Task-SQL/src/main/resources/neostudy_task_sql_1.txt").toFile());
    }


}