package com.prokofeva.dossier_api.service.messageCreatorCmd;

import com.prokofeva.dossier_api.service.MessageCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.UUID;

@Slf4j
@Component
public class MessageCreatorDenied implements MessageCreator {
    @Override
    public void fill(MimeMessageHelper messageHelper, UUID statementId, String lodId) throws MessagingException {

    }
}