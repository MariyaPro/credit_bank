package com.prokofeva.dossier_api.service;

import com.prokofeva.dossier_api.service.messageCreatorCmd.*;
import com.prokofeva.enums.Theme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageCreatorFactory {
    private final MessageCreatorFinReg creatorFinReg;
    private final MessageCreatorCreateDoc creatorCreateDoc;
    private final MessageCreatorSendDoc creatorSendDoc;
    private final MessageCreatorSesCode creatorSesCode;
    private final MessageCreatorCreditIssued creatorCreditIssued;
    private final MessageCreatorDenied creatorDenied;


    public MessageCreator getCreator(Theme theme, String logId) {
        MessageCreator creator;
        switch (theme) {
            case FINISH_REGISTRATION -> creator = creatorFinReg;
            case CREATE_DOCUMENTS -> creator = creatorCreateDoc;
            case SEND_DOCUMENTS -> creator = creatorSendDoc;
            case SEND_SES -> creator = creatorSesCode;
            case CREDIT_ISSUED -> creator = creatorCreditIssued;
            case STATEMENT_DENIED -> creator = creatorDenied;
            default -> {
                log.error("{} -- Задана неизвестная тема письма: {}", logId, theme.getValue());
                throw new IllegalArgumentException("Неизвестная тема письма.");
            }
        }
        return creator;
    }
}
