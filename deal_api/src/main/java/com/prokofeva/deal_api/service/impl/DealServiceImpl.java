package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.client.CalcFeignClient;
import com.prokofeva.deal_api.exeption.ExternalServiceException;
import com.prokofeva.deal_api.service.*;
import com.prokofeva.dto.*;
import com.prokofeva.enums.ApplicationStatus;
import com.prokofeva.enums.CreditStatus;
import com.prokofeva.enums.Theme;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {
    private final ClientService clientService;
    private final StatementService statementService;
    private final CalcFeignClient calcFeignClient;
    private final CreditService creditService;
    private final KafkaProducer kafkaProducer;

    @Value("${calc_feignclient_url}")
    private String calcFeignClientUrl;

    @Transactional
    @Override
    public List<LoanOfferDto> getListOffers(LoanStatementRequestDto loanStatementRequestDto,String logId) {
        ClientDto clientDto = clientService.createClient(loanStatementRequestDto, logId);
        StatementDto statementDto = statementService.createStatement(clientDto, logId);

        List<LoanOfferDto> listOffers;
        try {
            listOffers = calcFeignClient.getListOffers(loanStatementRequestDto);
            log.info("{} -- Получен ответ от внешнего сервиса ({}offers).", logId, calcFeignClientUrl);
        } catch (FeignException e) {
            String message = e.status() == 406 ?
                    new String(e.responseBody().get().array())
                    : "Error from external service (" + calcFeignClientUrl + "offers).";
            log.error("{} -- {}", logId, message);
            throw new ExternalServiceException(message);
        }

        for (LoanOfferDto offer : listOffers)
            offer.setStatementId(statementDto.getStatementId());
        log.info("{} -- В полученных вариантах займа уснановлен UUID заявки (statementId = {}).", logId, statementDto.getStatementId());

        return listOffers;
    }

    @Override
    public void selectAppliedOffer(LoanOfferDto loanOfferDto, String logId) {
        statementService.selectAppliedOffer(loanOfferDto);
        kafkaProducer.sendMessage(loanOfferDto.getStatementId().toString(), Theme.FINISH_REGISTRATION);
    }

    @Transactional
    @Override
    public void registrationCredit(FinishRegistrationRequestDto finishRegistrationRequestDto, String statementId, String logId) {
        log.info("{} -- Получение заявки из БД.", statementId);
        StatementDto statementDto = statementService.getStatementById(statementId);
        log.info("{} -- Необходимо заполнить недостающие данные клиента.", statementId);
        ClientDto clientDtoUp = clientService.updateClientInfo(statementDto.getClientId(), finishRegistrationRequestDto, statementId);

        ScoringDataDto scoringDataDto = createScoringData(statementDto.getAppliedOffer(), clientDtoUp);
        log.info("{} -- Запрос сформирован: {}.", statementId, scoringDataDto);

        CreditDto creditDto;
        try {
            creditDto = calcFeignClient.calculateCredit(scoringDataDto);
            log.info("{} -- Получен ответ от внешнего сервиса ({}calc).", statementId, calcFeignClientUrl);
        } catch (FeignException e) {
            String message;
            if (e.status() == 406 && e.responseBody().isPresent()) {
                message = new String(e.responseBody().get().array());
                kafkaProducer.sendMessage(statementId, Theme.STATEMENT_DENIED);
                statementService.updateStatementStatus(ApplicationStatus.CC_DENIED,statementId, logId);
            } else {
                message = "Error from external service (" + calcFeignClientUrl + "calc).";
            }
            log.error("{} -- {}", statementId, message);
            throw new ExternalServiceException(message);
        }

        CreditDto creditDtoFromDb = creditService.createCredit(creditDto, statementId);

        statementService.registrationCredit(statementDto, creditDtoFromDb);
        log.info("{} -- Процедура регистрации кредита завершена успешно.", statementId);

        kafkaProducer.sendMessage(statementId,Theme.CREATE_DOCUMENTS);
    }

    @Override
    public StatementDto getStatement(String statementId, String logId) {
        return statementService.getStatementById(statementId);
    }

    @Override
    public void updateStatementStatus(ApplicationStatus status, String statementId, String logId) {
        statementService.updateStatementStatus(status, statementId, logId);
    }

    @Override
    public void checkSesCode(String sesCode, String statementId, String logId) {
        log.info("{} -- Получен ses-код от клиента.",logId);
        if (statementService.checkSesCode(sesCode, statementId, logId)) {
            log.info("{} -- Сверка кода прошла успешно.",logId);
            statementService.updateStatementStatus(ApplicationStatus.DOCUMENT_SIGNED, statementId, logId);
            CreditDto creditDto = statementService.getStatementById(statementId).getCreditId();
            creditService.updateCreditStatus(CreditStatus.ISSUED, creditDto, logId);

            kafkaProducer.sendMessage(statementId,Theme.CREDIT_ISSUED);
        }
        else {log.info("{} --Полученный код не соответсвует сохраненному.",logId);}
    }

    @Override
    public void signDocuments(String statementId, String logId) {
        statementService.setupSesCode(statementId, logId);
        log.info("{} -- Установлен ses-код в заявке id={}.",logId,statementId);
        statementService.updateStatementStatus(ApplicationStatus.DOCUMENT_CREATED,statementId,logId);

       kafkaProducer.sendMessage(statementId,Theme.SEND_SES);
    }

    private ScoringDataDto createScoringData(LoanOfferDto loanOfferDto, ClientDto clientDto) {
        log.info("{} -- Формирование запроса к внешнему сервису.", loanOfferDto.getStatementId());
        return ScoringDataDto.builder()
                .amount(loanOfferDto.getRequestedAmount())
                .term(loanOfferDto.getTerm())
                .isInsuranceEnabled(loanOfferDto.getIsInsuranceEnabled())
                .isSalaryClient(loanOfferDto.getIsSalaryClient())

                .firstName(clientDto.getFirstName())
                .lastName(clientDto.getLastName())
                .middleName(clientDto.getMiddleName())
                .gender(clientDto.getGender())
                .birthdate(clientDto.getBirthDate())
                .passportSeries(clientDto.getPassport().getSeries())
                .passportNumber(clientDto.getPassport().getNumber())
                .passportIssueDate(clientDto.getPassport().getIssueDate())
                .passportIssueBranch(clientDto.getPassport().getIssueBranch())
                .maritalStatus(clientDto.getMaritalStatus())
                .dependentAmount(clientDto.getDependentAmount())
                .employment(clientDto.getEmployment())
                .accountNumber(clientDto.getAccountNumber())

                .build();
    }
}
