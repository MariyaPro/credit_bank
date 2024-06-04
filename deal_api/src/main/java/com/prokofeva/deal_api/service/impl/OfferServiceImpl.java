package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.doman.dto.ClientDto;
import com.prokofeva.deal_api.doman.dto.LoanOfferDto;
import com.prokofeva.deal_api.doman.dto.LoanStatementRequestDto;
import com.prokofeva.deal_api.doman.dto.StatementDto;
import com.prokofeva.deal_api.service.ClientService;
import com.prokofeva.deal_api.service.OfferService;
import com.prokofeva.deal_api.service.StatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {
    private final ClientService clientService;
    private final StatementService statementService;

    @Override
    public List<LoanOfferDto> getListOffers(LoanStatementRequestDto loanStatementRequestDto) {
        ClientDto clientDto = clientService.saveClient(loanStatementRequestDto);
        StatementDto statementDto = statementService.saveStatement(clientDto);

        List<LoanOfferDto> listOffers = List.of(new LoanOfferDto());  //todo из калькулятора

        for (LoanOfferDto offer : listOffers)
            offer.setStatementId(statementDto.getStatementId());

        return listOffers;
    }
}
