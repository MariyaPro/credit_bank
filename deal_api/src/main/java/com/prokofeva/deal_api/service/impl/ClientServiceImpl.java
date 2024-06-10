package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.doman.Client;
import com.prokofeva.deal_api.doman.Passport;
import com.prokofeva.deal_api.doman.dto.*;
import com.prokofeva.deal_api.mapper.ClientMapper;
import com.prokofeva.deal_api.repositories.ClientRepo;
import com.prokofeva.deal_api.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepo clientRepo;
    private final ClientMapper clientMapper;


    @Override
    public ClientDto createClient(LoanStatementRequestDto loanStatementRequestDto) {
        Passport passport = new Passport();
        passport.setSeries(loanStatementRequestDto.getPassportSeries());
        passport.setNumber(loanStatementRequestDto.getPassportNumber());

//        String s = passport.getSeries() + passport.getNumber();
//
//        UUID clientId = UUID.nameUUIDFromBytes(s.getBytes());
//
//        if (clientRepo.existsById(clientId)) {
//            return clientMapper.convertEntityToDto(
//                    clientRepo.findById(clientId).
//                            orElseThrow(RuntimeException::new));
//        }

        Client client = new Client();
        //  client.setClientId(clientId);
        client.setLastName(loanStatementRequestDto.getLastName());
        client.setFirstName(loanStatementRequestDto.getFirstName());
        client.setMiddleName(loanStatementRequestDto.getMiddleName());
        client.setBirthDate(loanStatementRequestDto.getBirthdate());
        client.setEmail(loanStatementRequestDto.getEmail());
        client.setPassport(passport);

        return saveClient(client);
    }

    @Override
    public ClientDto saveClient(Client client) {
        return clientMapper.convertEntityToDto(clientRepo.saveAndFlush(client));
    }

    @Override
    public ClientDto updateClientInfo(ClientDto clientDto,
                                      FinishRegistrationRequestDto finRegRequestDto
    ) {
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

        return saveClient(clientMapper.convertDtoToEntity(clientDto));

    }
}
