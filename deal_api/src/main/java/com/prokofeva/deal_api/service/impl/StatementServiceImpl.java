package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.doman.Statement;
import com.prokofeva.deal_api.doman.dto.*;
import com.prokofeva.deal_api.doman.enums.ApplicationStatus;
import com.prokofeva.deal_api.doman.enums.ChangeType;
import com.prokofeva.deal_api.mapper.ClientMapper;
import com.prokofeva.deal_api.mapper.StatementMapper;
import com.prokofeva.deal_api.repositories.StatementRepo;
import com.prokofeva.deal_api.service.StatementService;
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
    public void selectAppliedOffer(LoanOfferDto loanOfferDto) {
        Optional<Statement> statementOptional = statementRepo.findById(loanOfferDto.getStatementId());
        Statement statement = statementOptional.orElseThrow(EntityNotFoundException::new);
        statement.setAppliedOffer(loanOfferDto);
        statementRepo.saveAndFlush(statement);
    }

    private StatementDto saveStatement(Statement statement) {
        Statement statementFromDb = statementRepo.saveAndFlush(statement);
        log.info("Изменения успешно сохранены в базу данных. {}", statementFromDb);

        return statementMapper.convertEntityToDto(statementFromDb);
    }

    @Override
    public StatementDto createStatement(ClientDto clientDto) {
        Statement statement = new Statement();
        statement.setClientId(clientMapper.convertDtoToEntity(clientDto));
        statement.setCreationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        statement.setStatusHistoryList(new LinkedList<>());

        addStatusHistory(statement, ApplicationStatus.PREAPPROVAL);

        log.info("Создана новая заявка на кредит: {}.", statement);
        return saveStatement(statement);
    }

    @Override
    public StatementDto getStatementById(String statementId) {
        Statement statement = statementRepo.findById(UUID.fromString(statementId))
                .orElseThrow(EntityNotFoundException::new);
        log.info("Заявка с Id {} успешно плучена из БД.", statementId);
        return statementMapper.convertEntityToDto(statement);
    }

    @Override
    public void registrationCredit(StatementDto statementDto, CreditDto creditDtoFromDb) {
        statementDto.setCreditId(creditDtoFromDb);
        log.info("Изменение данных заявки: заполнено поле creditId = {}.", creditDtoFromDb.getCreditId());
        Statement statement = statementMapper.convertDtoToEntity(statementDto);
        addStatusHistory(statement, ApplicationStatus.APPROVED);
        saveStatement(statement);
    }

    private void addStatusHistory(Statement statement, ApplicationStatus status) {
        log.info("Изменение статуса заявки (заявка id = {}, статус = {}).", statement.getStatementId(), status);
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        statement.setStatus(status);

        StatusHistory statusHistory = new StatusHistory(status, now, ChangeType.AUTOMATIC);
        statement.getStatusHistoryList().add(statusHistory);
        log.info("Статус заявки исменен. В журнал статусов добавлена новая запись: {}.", statusHistory);
    }
}
