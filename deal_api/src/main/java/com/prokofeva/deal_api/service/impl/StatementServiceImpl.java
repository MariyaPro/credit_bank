package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.doman.Statement;
import com.prokofeva.deal_api.doman.dto.ClientDto;
import com.prokofeva.deal_api.doman.dto.LoanOfferDto;
import com.prokofeva.deal_api.doman.dto.StatementDto;
import com.prokofeva.deal_api.doman.enums.ApplicationStatus;
import com.prokofeva.deal_api.repositories.StatementRepo;
import com.prokofeva.deal_api.service.ClientService;
import com.prokofeva.deal_api.service.StatementService;
import com.prokofeva.deal_api.service.mapper.ClientMapper;
import com.prokofeva.deal_api.service.mapper.StatementMapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.type.LocalDateType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatementServiceImpl implements StatementService {
    private final StatementRepo statementRepo;
    private final ClientService clientService;

    @Override
    public void setAppliedOffer(LoanOfferDto loanOfferDto){
        Optional<Statement> statementOptional = statementRepo.findById(loanOfferDto.getStatementId());
        Statement statement = statementOptional.orElseThrow();
        statement.setAppliedOffer(loanOfferDto);
        statementRepo.save(statement);
        //Statement save = statementRepo.save(statement);
    }

    public StatementDto createStatement (ClientDto clientDto){
        Statement statement = new Statement();
        statement.setClientId(clientDto.getClientId());
        statement.setStatus(ApplicationStatus.PREAPPROVAL);
        statement.setCreationDate(LocalDateTime.now());

        return StatementMapper.convertEntityToDto(statementRepo.save(statement));

    }

}
