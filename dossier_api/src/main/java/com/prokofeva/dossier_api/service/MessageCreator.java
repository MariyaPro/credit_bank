package com.prokofeva.dossier_api.service;

import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import java.util.UUID;

public interface MessageCreator {
    void fill(MimeMessageHelper messageHelper, UUID statementId, String logId) throws MessagingException;
}
