package com.prokofeva.dossier_api.service.messageCreatorCmd;

import com.prokofeva.dossier_api.service.MessageCreator;
import com.prokofeva.dossier_api.service.StatementService;
import com.prokofeva.dto.StatementDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageCreatorSesCode implements MessageCreator {
    private final StatementService statementService;

    @Override
    public void fill(MimeMessageHelper messageHelper, UUID statementId, String logId) throws MessagingException {
        StatementDto statementDto = statementService.getInfoFromDb(statementId, logId);
        String sesCode = statementDto.getSesCode();
        messageHelper.setText("Для подписания крелитного договора отправьте код: \n" +
                sesCode+"\n по адресу:\n" +
                "http://localhost:8085/deal/document/" + statementId + "/code/" );
        log.info("{} -- {}: сообщение записано в тело сообщения." , logId, this.getClass().getSimpleName());
    }
}