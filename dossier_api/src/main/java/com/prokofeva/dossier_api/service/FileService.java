package com.prokofeva.dossier_api.service;

import java.nio.file.Path;

public interface FileService {
    Path createCreditAgreementFile();
    Path createQuestionnaireFile();
    Path createPaymentScheduleFile();
}