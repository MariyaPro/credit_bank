package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.doman.Client;
import com.prokofeva.deal_api.doman.dto.*;
import com.prokofeva.deal_api.mapper.ClientMapper;
import com.prokofeva.deal_api.repositories.ClientRepo;
import com.prokofeva.deal_api.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepo clientRepo;
    private final ClientMapper clientMapper;

    @Override
    public ClientDto createClient(LoanStatementRequestDto loanStatementRequestDto) {
        PassportDto passport = PassportDto.builder()
                .series(loanStatementRequestDto.getPassportSeries())
                .number(loanStatementRequestDto.getPassportNumber())
                .build();

        Client client = new Client();
        client.setLastName(loanStatementRequestDto.getLastName());
        client.setFirstName(loanStatementRequestDto.getFirstName());
        client.setMiddleName(loanStatementRequestDto.getMiddleName());
        client.setBirthDate(loanStatementRequestDto.getBirthdate());
        client.setEmail(loanStatementRequestDto.getEmail());
        client.setPassport(passport);
        log.info("Создан новый клиент: {}.", client);
        return saveClient(client);
    }

    public ClientDto saveClient(Client client) {
        Client clientFromDb = clientRepo.saveAndFlush(client);
        log.info("Изменения успешно сохранены: {}", clientFromDb);
        return clientMapper.convertEntityToDto(clientFromDb);
    }

    @Override
    public ClientDto updateClientInfo(ClientDto clientDto,
                                      FinishRegistrationRequestDto finRegRequestDto
    ) {
        log.info("Обновление данных клиента (id = {}).", clientDto.getClientId());
        PassportDto passportDto = clientDto.getPassport();
        passportDto.setIssueDate(finRegRequestDto.getPassportIssueDate());
        passportDto.setIssueBranch(finRegRequestDto.getPassportIssueBrach());

        EmploymentDto employmentDto = finRegRequestDto.getEmployment();

        clientDto.setGender(finRegRequestDto.getGender());
        clientDto.setMaritalStatus(finRegRequestDto.getMaritalStatus());
        clientDto.setDependentAmount(finRegRequestDto.getDependentAmount());
        clientDto.setPassport(passportDto);
        clientDto.setEmployment(employmentDto);
        clientDto.setAccountNumber(finRegRequestDto.getAccountNumber());

        log.info("Данные клиента (id = {}) изменены.", clientDto.getClientId());

        return saveClient(clientMapper.convertDtoToEntity(clientDto));
    }
}
