package com.prokofeva.dossier_api.service;

import com.prokofeva.dto.EmailMessageDto;

import javax.validation.Valid;

public interface DossierService {
    void sendMessageToClient(@Valid EmailMessageDto emailMessageDto, String logId) ;
}
