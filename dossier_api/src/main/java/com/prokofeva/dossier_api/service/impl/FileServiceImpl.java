package com.prokofeva.dossier_api.service.impl;

import com.prokofeva.dossier_api.service.FileService;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public Path createCreditAgreementFile() {
        return null;
    }

    @Override
    public Path createQuestionnaireFile() {
        return null;
    }

    @Override
    public Path createPaymentScheduleFile() {
        return null;
    }
}
