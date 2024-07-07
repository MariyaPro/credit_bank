package com.prokofeva.dossier_api.service.impl;

import com.prokofeva.dossier_api.service.FileService;
import com.prokofeva.dto.StatementDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Override
    public Path createCreditAgreementFile(StatementDto statementDto, String logId) {
        String name = "Credit_agreement_" + statementDto.getStatementId();
        Path path;
        try {
            path = Files.createTempFile( name, ".txt");
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        try (FileWriter writer = new FileWriter(path.toFile())) {
            writer.write(statementDto.getCreditId().toString());
            writer.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return path;
    }

    @Override
    public Path createQuestionnaireFile(StatementDto statementDto, String logId) {
        String name = "Questionnaire_" + statementDto.getStatementId();
        Path path;
        try {
            path = Files.createTempFile( name, ".txt");
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        try (FileWriter writer = new FileWriter(path.toFile())) {
            writer.write(statementDto.getClientId().toString());
            writer.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return path;
    }

    @Override
    public Path createPaymentScheduleFile(StatementDto statementDto, String logId) {
        String name = "Payment_Schedule_" + statementDto.getStatementId();
        Path path;
        try {
            path = Files.createTempFile( name, ".txt");
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        try (FileWriter writer = new FileWriter(path.toFile())) {
            writer.write(statementDto.getCreditId().getPaymentSchedule().toString());
            writer.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return path;
    }
}
