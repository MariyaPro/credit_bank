package com.prokofeva.dossier_api.service.messageCreatorCmd;

import com.prokofeva.dossier_api.service.FileService;
import com.prokofeva.dossier_api.service.MessageCreator;
import com.prokofeva.dossier_api.service.StatementService;
import com.prokofeva.dto.StatementDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageCreatorSendDoc implements MessageCreator {
    private final FileService fileService;
    private final StatementService statementService;

    @Override
    public void fill(MimeMessageHelper messageHelper, UUID statementId, String logId) throws MessagingException {
        StatementDto statementDto = statementService.getInfoFromDb(statementId, logId);

        Path creditAgreementFile = fileService.createCreditAgreementFile(statementDto, logId);
        Path questionnaireFile = fileService.createQuestionnaireFile(statementDto, logId);
        Path paymentScheduleFile = fileService.createPaymentScheduleFile(statementDto, logId);

        messageHelper.addAttachment(String.valueOf(creditAgreementFile.getFileName()), creditAgreementFile.toFile());
        messageHelper.addAttachment(String.valueOf(questionnaireFile.getFileName()), questionnaireFile.toFile());
        messageHelper.addAttachment(String.valueOf(paymentScheduleFile.getFileName()), paymentScheduleFile.toFile());

        String text = "Направляем документы на ознакомление:\n" +
                "1. Кредитный договор - внимательно ознакомьтесь с условиями.\n" +
                "2. Анкету с вашими данными - Проверьте правильность указанных данных.\n" +
                "3. График ежемесячных платежей.\n" +
                "Если вы согласны с условиями предоставления кредита, перейдите по ссылке:\n" +
                "http://localhost:8085/deal/document/" + statementId + "/sing";
        messageHelper.setText(text);
        log.info("{} -- Сообщение и ссылка для подтверждения согласия установлены в тело письма.", logId);
        //for test
        messageHelper.addAttachment("statementId", Paths.get("/home/dev/IdeaProjects/Neo-Task-SQL/src/main/resources/neostudy_task_sql_1.txt").toFile());

    }
}