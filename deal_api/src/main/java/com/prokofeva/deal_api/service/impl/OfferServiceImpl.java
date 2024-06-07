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
    //  private final CalculatorClient calculatorClient;

    @Override
    public List<LoanOfferDto> getListOffers(LoanStatementRequestDto loanStatementRequestDto) {
        System.out.println("!!!!");
        ClientDto clientDto = clientService.createClient(loanStatementRequestDto);
        StatementDto statementDto = statementService.createStatement(clientDto);

        //  List<LoanOfferDto> listOffers = calculatorClient.createLoanOffer(loanStatementRequestDto);
        System.out.println("EEEE");
        List<LoanOfferDto> listOffers = List.of(new LoanOfferDto(), new LoanOfferDto(),
                new LoanOfferDto(), new LoanOfferDto());  //todo из калькулятора

        for (LoanOfferDto offer : listOffers)
            offer.setStatementId(statementDto.getStatementId());

        return listOffers;
    }
}
