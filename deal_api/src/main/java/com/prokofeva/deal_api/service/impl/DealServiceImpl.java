package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.client.CalcFeignClient;
import com.prokofeva.deal_api.doman.dto.*;
import com.prokofeva.deal_api.service.ClientService;
import com.prokofeva.deal_api.service.CreditService;
import com.prokofeva.deal_api.service.DealService;
import com.prokofeva.deal_api.service.StatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {
    private final ClientService clientService;
    private final StatementService statementService;
    private final CalcFeignClient calcFeignClient;
    private final CreditService creditService;

    @Override
    public List<LoanOfferDto> getListOffers(LoanStatementRequestDto loanStatementRequestDto) {
        ClientDto clientDto = clientService.createClient(loanStatementRequestDto);
        StatementDto statementDto = statementService.createStatement(clientDto);

        List<LoanOfferDto> listOffers = calcFeignClient.getListOffers(loanStatementRequestDto);

        for (LoanOfferDto offer : listOffers)
            offer.setStatementId(statementDto.getStatementId());

        return listOffers;
    }

    @Override
    public void setAppliedOffer(LoanOfferDto loanOfferDto) {
        statementService.setAppliedOffer(loanOfferDto);
    }

    @Override
    public void registrationCredit(FinishRegistrationRequestDto finishRegistrationRequestDto, String statementId) {
        StatementDto statementDto = statementService.getStatementById(statementId);
        ClientDto clientDtoUp = clientService.updateClientInfo(statementDto.getClientId(), finishRegistrationRequestDto);

        ScoringDataDto scoringDataDto = createScoringData(statementDto.getAppliedOffer(), clientDtoUp);

        CreditDto creditDto = calcFeignClient.calculateCredit(scoringDataDto);
        CreditDto creditDtoFromDb = creditService.createCredit(creditDto);

        statementService.registrationCredit(statementDto,creditDtoFromDb);
    }

    private ScoringDataDto createScoringData(LoanOfferDto loanOfferDto, ClientDto clientDto) {
        ScoringDataDto scoringDataDto = new ScoringDataDto();

        scoringDataDto.setAmount(loanOfferDto.getRequestedAmount());
        scoringDataDto.setTerm(loanOfferDto.getTerm());
        scoringDataDto.setFirstName(clientDto.getFirstName());
        scoringDataDto.setLastName(clientDto.getLastName());
        scoringDataDto.setMiddleName(clientDto.getMiddleName());
        scoringDataDto.setGender(clientDto.getGender());
        scoringDataDto.setBirthdate(clientDto.getBirthDate());
        scoringDataDto.setPassportSeries(clientDto.getPassport().getSeries());
        scoringDataDto.setPassportNumber(clientDto.getPassport().getNumber());
        scoringDataDto.setMaritalStatus(clientDto.getMaritalStatus());
        scoringDataDto.setDependentAmount(clientDto.getDependentAmount());
        scoringDataDto.setEmployment(clientDto.getEmployment());
        scoringDataDto.setAccountNumber(clientDto.getAccountNumber());
        scoringDataDto.setIsInsuranceEnabled(loanOfferDto.getIsInsuranceEnabled());
        scoringDataDto.setIsSalaryClient(loanOfferDto.getIsSalaryClient());

        return scoringDataDto;
    }
}
