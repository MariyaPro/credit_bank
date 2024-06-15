package com.prokofeva.deal_api.mapper;

import com.prokofeva.deal_api.doman.Client;
import com.prokofeva.deal_api.doman.dto.ClientDto;
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
        clientDtoAc = ClientDto.builder()
                .clientId(UUID.fromString("123e4567-e89b-12d3-a456-426655440000"))
                .lastName("testLastName")
                .firstName("testFirstName")
                .middleName("testMiddleName")
                .birthDate(LocalDate.of(2000, 8, 21))
                .email("testExample@gmail.com")
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(2)
                .passport(null)
                .employment(null)
                .accountNumber("123454321")
                .build();
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
}