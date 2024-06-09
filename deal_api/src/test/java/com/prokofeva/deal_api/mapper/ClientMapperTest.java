package com.prokofeva.deal_api.mapper;

import com.prokofeva.deal_api.doman.Client;
import com.prokofeva.deal_api.doman.Passport;
import com.prokofeva.deal_api.doman.dto.ClientDto;
import com.prokofeva.deal_api.doman.dto.PassportDto;
import com.prokofeva.deal_api.doman.enums.MaritalStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ClientMapperTest {
    @Autowired
    private ClientMapper clientMapper;
    private static Client clientAc;
    private static ClientDto clientDtoAc;

    @BeforeAll
    static void prepareClientEx() {
        clientAc = new Client();
        clientAc.setClientId(UUID.fromString("123e4567-e89b-12d3-a456-426655440000"));
        clientAc.setLastName("testLastName");
        clientAc.setFirstName("testFirstName");
        clientAc.setMiddleName("testMiddleName");
        clientAc.setBirthDate(LocalDate.of(2000, 8, 21));
        clientAc.setEmail("testExample@gmail.com");
        clientAc.setMaritalStatus(MaritalStatus.MARRIED);
        clientAc.setDependentAmount(2);
        clientAc.setPassport(null);
        clientAc.setEmployment(null);
        clientAc.setAccountNumber("123454321");
    }

    @BeforeAll
    static void prepareClientDtoEx() {
        clientDtoAc = new ClientDto();
        clientDtoAc.setClientId(UUID.fromString("123e4567-e89b-12d3-a456-426655440000"));
        clientDtoAc.setLastName("testLastName");
        clientDtoAc.setFirstName("testFirstName");
        clientDtoAc.setMiddleName("testMiddleName");
        clientDtoAc.setBirthDate(LocalDate.of(2000, 8, 21));
        clientDtoAc.setEmail("testExample@gmail.com");
        clientDtoAc.setMaritalStatus(MaritalStatus.MARRIED);
        clientDtoAc.setDependentAmount(2);
        clientDtoAc.setPassport(null);
        clientDtoAc.setEmployment(null);
        clientDtoAc.setAccountNumber("123454321");
    }

    @Test
    void convertEntityToDto() {
        ClientDto clientDtoEx = clientMapper.convertEntityToDto(clientAc);

        assertNotNull(clientDtoEx);
        assertEquals(clientDtoEx, clientDtoAc);
    }

    @Test
    void convertDtoToEntity() {
        Client clientEx = clientMapper.convertDtoToEntity(clientDtoAc);

        assertNotNull(clientEx);
        assertEquals(clientEx, clientAc);
    }


    @Test
    void convertPassport() {
        Passport passportAc =new Passport();
        passportAc.setSeries("3434");
        passportAc.setNumber("666666");

        PassportDto passportDtoAc =new PassportDto();
        passportDtoAc.setSeries("3434");
        passportDtoAc.setNumber("666666");

        clientAc.setPassport(passportAc);
        clientDtoAc.setPassport(passportDtoAc);

        Client clientEx = clientMapper.convertDtoToEntity(clientDtoAc);

        assertNotNull(clientEx);
        assertEquals(clientEx.getPassport(), clientAc.getPassport());

        ClientDto clientDtoEx = clientMapper.convertEntityToDto(clientAc);

        assertNotNull(clientDtoEx);
        assertEquals(clientDtoEx.getPassport(), clientDtoAc.getPassport());
    }
}