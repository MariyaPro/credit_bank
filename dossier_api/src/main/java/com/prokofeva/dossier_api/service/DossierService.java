package com.prokofeva.dossier_api.service;

import com.prokofeva.dto.EmailMessageDto;

public interface DossierService {
    void sendMessageToClient(EmailMessageDto emailMessageDto) ;
}
