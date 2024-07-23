package com.prokofeva.dossier_api.service;

import com.prokofeva.dto.StatementDto;

import java.nio.file.Path;

public interface FileService {
    Path createCreditAgreementFile(StatementDto statementDto, String logId) ;
    Path createQuestionnaireFile(StatementDto statementDto, String logId);
    Path createPaymentScheduleFile(StatementDto statementDto, String logId);
}