package com.prokofeva.deal_api.service;

import com.prokofeva.enums.Theme;

public interface KafkaProducer {
    void sendMessage(String statementId, Theme theme);
}