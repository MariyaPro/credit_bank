package com.prokofeva.dossier_api.service;

import javax.mail.internet.MimeMessage;

public interface SenderService {
    void sendMailMessage(MimeMessage message, String logId);
}
