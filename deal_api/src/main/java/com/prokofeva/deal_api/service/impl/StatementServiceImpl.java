package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.doman.Statement;
import com.prokofeva.deal_api.doman.dto.ClientDto;
import com.prokofeva.deal_api.doman.dto.LoanOfferDto;
import com.prokofeva.deal_api.doman.dto.StatementDto;
import com.prokofeva.deal_api.doman.enums.ApplicationStatus;
import com.prokofeva.deal_api.mapper.ClientMapper;
import com.prokofeva.deal_api.mapper.StatementMapper;
import com.prokofeva.deal_api.repositories.StatementRepo;
import com.prokofeva.deal_api.service.StatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatementServiceImpl implements StatementService {
    private final StatementRepo statementRepo;
    private final StatementMapper statementMapper;
    private final ClientMapper clientMapper;

    @Override
    public void setAppliedOffer(LoanOfferDto loanOfferDto) {
        Optional<Statement> statementOptional = statementRepo.findById(loanOfferDto.getStatementId());
        Statement statement = statementOptional.orElseThrow(RuntimeException::new);
        statement.setAppliedOffer(loanOfferDto);
        statementRepo.save(statement);
        //Statement save = statementRepo.save(statement);
    }

    @Override
    public StatementDto createStatement(ClientDto clientDto) {
        Statement statement = new Statement();
        statement.setClientId(clientMapper.convertDtoToEntity(clientDto));
        statement.setStatus(ApplicationStatus.PREAPPROVAL);
        statement.setCreationDate(LocalDateTime.now());

        return statementMapper.convertEntityToDto(statementRepo.save(statement));
    }

}
