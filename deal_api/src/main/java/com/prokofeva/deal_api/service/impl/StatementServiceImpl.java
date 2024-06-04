package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.doman.Statement;
import com.prokofeva.deal_api.doman.dto.LoanOfferDto;
import com.prokofeva.deal_api.repositories.StatementRepo;
import com.prokofeva.deal_api.service.StatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatementServiceImpl implements StatementService {
    private final StatementRepo statementRepo;

    @Override
    public void setAppliedOffer(LoanOfferDto loanOfferDto){
        Optional<Statement> statementOptional = statementRepo.findById(loanOfferDto.getStatementId());
        Statement statement = statementOptional.orElseThrow();
        statement.setAppliedOffer(loanOfferDto.toString()); // todo jsonb
        statementRepo.save(statement);
        //Statement save = statementRepo.save(statement);
    }
}
