package com.prokofeva.deal_api.service;

import com.prokofeva.deal_api.enums.Theme;

import java.util.UUID;

public interface MessageService {

    void sendMessage(String email, Theme theme, UUID statementId);

}
