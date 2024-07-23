package com.prokofeva.dossier_api.service;

import com.prokofeva.dto.StatementDto;

import java.util.UUID;

public interface StatementService {
    StatementDto getInfoFromDb(UUID statementId,String logId);
}
