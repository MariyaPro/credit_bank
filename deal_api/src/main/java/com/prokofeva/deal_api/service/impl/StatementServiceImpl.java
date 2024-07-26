package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.mapper.ClientMapper;
import com.prokofeva.deal_api.mapper.StatementMapper;
import com.prokofeva.deal_api.model.Statement;
import com.prokofeva.deal_api.repositories.StatementRepo;
import com.prokofeva.deal_api.service.StatementService;
import com.prokofeva.dto.*;
import com.prokofeva.enums.ApplicationStatus;
import com.prokofeva.enums.ChangeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatementServiceImpl implements StatementService {
    private final StatementRepo statementRepo;
    private final StatementMapper statementMapper;
    private final ClientMapper clientMapper;

    @Override
    public void selectAppliedOffer(LoanOfferDto loanOfferDto, String logId) {
        Optional<Statement> statementOptional = statementRepo.findById(loanOfferDto.getStatementId());
        Statement statement = statementOptional.orElseThrow(EntityNotFoundException::new);
        statement.setAppliedOffer(loanOfferDto);
        saveStatement(statement, logId);
    }

    private StatementDto saveStatement(Statement statement, String logId) {
        Statement statementFromDb = statementRepo.saveAndFlush(statement);
        log.info("{} -- Изменения успешно сохранены в базу данных. {}", logId, statementFromDb);

        return statementMapper.convertEntityToDto(statementFromDb);
    }

    @Override
    public StatementDto createStatement(ClientDto clientDto, String logId) {
        Statement statement = new Statement();
        statement.setClientId(clientMapper.convertDtoToEntity(clientDto));
        statement.setCreationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        statement.setStatusHistoryList(new LinkedList<>());

        addStatusHistory(statement, ApplicationStatus.PREAPPROVAL, logId);

        log.info("{} -- Создана новая заявка на кредит: {}.", logId, statement);
        return saveStatement(statement, logId);
    }

    @Override
    public StatementDto getStatementById(String statementId, String logId) {
        Statement statement = statementRepo.findById(UUID.fromString(statementId))
                .orElseThrow(EntityNotFoundException::new);
        log.info("{} -- Заявка (id={}) успешно плучена из БД.", logId,statementId);
        return statementMapper.convertEntityToDto(statement);
    }

    @Override
    public void registrationCredit(StatementDto statementDto, CreditDto creditDtoFromDb, String logId) {
        statementDto.setCreditId(creditDtoFromDb);
        log.info("{} -- Изменение данных заявки: заполнено поле creditId = {}.", logId, creditDtoFromDb.getCreditId());
        Statement statement = statementMapper.convertDtoToEntity(statementDto);
        addStatusHistory(statement, ApplicationStatus.APPROVED, logId);
        saveStatement(statement, logId);
    }

    @Override
    public void updateStatementStatus(ApplicationStatus status, String statementId, String logId) {
        Statement statement = statementRepo.findById(UUID.fromString(statementId)).orElseThrow(EntityNotFoundException::new);
        addStatusHistory(statement, status, logId);
        saveStatement(statement, logId);
    }

    @Override
    public boolean checkSesCode(String sesCode, String statementId, String logId) {
        Statement statement = statementRepo.findById(UUID.fromString(statementId)).orElseThrow(EntityNotFoundException::new);
        String sesCodeDb = statement.getSesCode();
        log.info("{} --Полученный код ({}). Сохраненный в базе ({}).",logId,sesCode,sesCodeDb);
     return sesCode.equals(sesCodeDb);
    }

    @Override
    public void setupSesCode(String statementId, String logId) {
        Statement statement = statementRepo.findById(UUID.fromString(statementId)).orElseThrow(EntityNotFoundException::new);
        statement.setSesCode(UUID.randomUUID().toString());
        saveStatement(statement,logId);
    }

    private void addStatusHistory(Statement statement, ApplicationStatus status, String logId) {
        log.info("{} -- Изменение статуса заявки (статус = {}).", logId, status);
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        statement.setStatus(status);
        if(status.equals(ApplicationStatus.DOCUMENT_SIGNED)) statement.setSignDate(now);

        StatusHistory statusHistory = new StatusHistory(status, now, ChangeType.AUTOMATIC);
        statement.getStatusHistoryList().add(statusHistory);
        log.info("{} -- Статус заявки изменен. В журнал статусов добавлена новая запись: {}.", logId, statusHistory);
    }
}