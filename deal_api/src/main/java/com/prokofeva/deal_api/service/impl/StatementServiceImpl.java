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
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;

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
        System.out.println("uel!");
    }

    @Override
    public StatementDto saveStatement(Statement statement) {
        return statementMapper.convertEntityToDto(statementRepo.saveAndFlush(statement));
    }

    @Override
    public StatementDto createStatement(ClientDto clientDto) {
        Statement statement = new Statement();
        statement.setClientId(clientMapper.convertDtoToEntity(clientDto));
        statement.setCreationDate(LocalDateTime.now());
        statement.setStatusHistoryList(new LinkedList<>());
        statement.setAppliedOffer(new LoanOfferDto());

        addStatusHistory(statement, ApplicationStatus.PREAPPROVAL);


        return saveStatement(statement);
    }

    @Override
    public StatementDto getStatementById(String statementId) {
        Statement statement = statementRepo.findById(UUID.fromString(statementId))
                .orElseThrow(EntityNotFoundException::new);
        return statementMapper.convertEntityToDto(statement);
    }

    @Override
    public void registrationCredit(StatementDto statementDto, CreditDto creditDtoFromDb) {
        statementDto.setCreditId(creditDtoFromDb);
        Statement statement = statementMapper.convertDtoToEntity(statementDto);
        addStatusHistory(statement, ApplicationStatus.APPROVED);

        saveStatement(statement);
    }

    public void addStatusHistory(Statement statement, ApplicationStatus status) {
        LocalDateTime localDateTime = LocalDateTime.now();

        statement.setStatus(status);

        StatusHistory statusHistory = new StatusHistory(status, localDateTime, ChangeType.AUTOMATIC);
        statement.getStatusHistoryList().add(statusHistory);
    }
}
