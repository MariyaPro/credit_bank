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
        log.info("{} -- В сообщение вложены файлы: {}, {}, {}", logId, creditAgreementFile.getFileName().toString(),
                questionnaireFile.getFileName().toString(), paymentScheduleFile.getFileName().toString());

        String text = "Направляем документы на ознакомление:\n" +
                "1. Кредитный договор - внимательно ознакомьтесь с условиями.\n" +
                "2. Анкету с вашими данными - Проверьте правильность указанных данных.\n" +
                "3. График ежемесячных платежей.\n" +
                "Если вы согласны с условиями предоставления кредита, перейдите по ссылке:\n" +
                "http://localhost:8085/deal/document/" + statementId + "/sign";
        messageHelper.setText(text);
        log.info("{} -- {}: сообщение, ses-код и ссылка для подписания кредитного договора установлены в тело письма.", logId, this.getClass().getSimpleName());
    }
}