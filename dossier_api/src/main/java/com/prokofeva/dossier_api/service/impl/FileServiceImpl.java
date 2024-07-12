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
        Path path = null;
        try {
            path = Files.createTempFile(name, ".txt");
            log.info("{} -- Создан файл {}.", logId, path.getFileName());
        } catch (IOException e) {
            log.error("{} -- При создании файла {} перехвачено исключение: {}.", logId, name, e.getMessage());
        }
        try (FileWriter writer = new FileWriter(path.toFile())) {
            writer.write(statementDto.getCreditId().toString());
            writer.flush();
            log.info("{} -- Информация о заявке {} успешно записана в файл {}.", logId, statementDto.getStatementId().toString(),path.getFileName());
        } catch (IOException e) {
            log.error("{} -- При записи данных в файл {} перехвачено исключение: {}.", logId, name, e.getMessage());
        }

        return path;
    }

    @Override
    public Path createQuestionnaireFile(StatementDto statementDto, String logId) {
        String name = "Questionnaire_" + statementDto.getStatementId();
        Path path = null;
        try {
            path = Files.createTempFile(name, ".txt");
            log.info("{} -- Создан файл {}.", logId, path.getFileName());
        } catch (IOException e) {
            log.error("{} -- При создании файла {} перехвачено исключение: {}.", logId, name, e.getMessage());
        }
        try (FileWriter writer = new FileWriter(path.toFile())) {
            writer.write(statementDto.getClientId().toString());
            writer.flush();
            log.info("{} -- Информация о заявке {} успешно записана в файл {}.", logId, statementDto.getStatementId().toString(),path.getFileName());
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return path;
    }

    @Override
    public Path createPaymentScheduleFile(StatementDto statementDto, String logId) {
        String name = "Payment_Schedule_" + statementDto.getStatementId();
        Path path = null;
        try {
            path = Files.createTempFile(name, ".txt");
            log.info("{} -- Создан файл {}.", logId, path.getFileName());
        } catch (IOException e) {
            log.error("{} -- При создании файла {} перехвачено исключение: {}.", logId, name, e.getMessage());
        }
        try (FileWriter writer = new FileWriter(path.toFile())) {
            writer.write(statementDto.getCreditId().getPaymentSchedule().toString());
            writer.flush();
            log.info("{} -- Информация о заявке {} успешно записана в файл {}.", logId, statementDto.getStatementId().toString(),path.getFileName());
        } catch (IOException e) {
            log.error("{} -- При записи данных в файл {} перехвачено исключение: {}.", logId, name, e.getMessage());
        }

        return path;
    }
}
