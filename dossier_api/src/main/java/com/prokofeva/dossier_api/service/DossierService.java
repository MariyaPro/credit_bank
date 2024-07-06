package com.prokofeva.dossier_api.service;

import com.prokofeva.dossier_api.dto.EmailMessageDto;

import javax.mail.internet.MimeMessage;

public interface DossierService {
    void sendMessageToClient(EmailMessageDto emailMessageDto) ;

    MimeMessage createMailMessage(EmailMessageDto emailMessageDto) ;
}
