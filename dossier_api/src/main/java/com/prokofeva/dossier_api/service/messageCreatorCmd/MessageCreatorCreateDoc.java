package com.prokofeva.dossier_api.service.messageCreatorCmd;

import com.prokofeva.dossier_api.service.MessageCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.UUID;

@Slf4j
@Component
public class MessageCreatorCreateDoc implements MessageCreator {

    @Override
    public void fill(MimeMessageHelper messageHelper, UUID statementId, String logId) throws MessagingException {
        String text = "Ваша заявка на кредит одобрена. Для получения документов перейдите по ссылке\n" +
                "http://localhost:8000/deal/document/" + statementId + "/send";
        messageHelper.setText(text);
        log.info("{} -- {}: сообщение и ссылка для получения документов установлены в тело письма.",logId,this.getClass().getSimpleName());
    }
}
