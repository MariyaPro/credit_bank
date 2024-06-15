package com.prokofeva.deal_api.mapper;

import com.prokofeva.deal_api.doman.Client;
import com.prokofeva.deal_api.doman.Credit;
import com.prokofeva.deal_api.doman.Statement;
import com.prokofeva.deal_api.doman.dto.ClientDto;
import com.prokofeva.deal_api.doman.dto.CreditDto;
import com.prokofeva.deal_api.doman.dto.LoanOfferDto;
import com.prokofeva.deal_api.doman.dto.StatementDto;
import com.prokofeva.deal_api.doman.enums.ApplicationStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class StatementMapperTest {
    @Autowired
    private StatementMapper statementMapper;

    private static Statement statementAc;
    private static StatementDto statementDtoAc;

    @BeforeAll
    static void prepareStatementAc() {
        statementAc = new Statement();
        statementAc.setStatementId(UUID.fromString("06115ad596-0873-0087-5764-c1f3730d90"));
        statementAc.setClientId(new Client());
        statementAc.setCreditId(new Credit());
        statementAc.setStatus(ApplicationStatus.APPROVED);
        statementAc.setCreationDate(LocalDateTime.of(2024, 6, 12, 11, 54, 44).truncatedTo(ChronoUnit.SECONDS));
        statementAc.setAppliedOffer(LoanOfferDto.builder().build());
        statementAc.setSignDate(null);
        statementAc.setSesCode("ses_code?");
    }

    @BeforeAll
    static void prepareStatementDtoAc() {
        statementDtoAc = new StatementDto();
        statementDtoAc.setStatementId(UUID.fromString("06115ad596-0873-0087-5764-c1f3730d90"));
        statementDtoAc.setClientId(ClientDto.builder().build());
        statementDtoAc.setCreditId(CreditDto.builder().build());
        statementDtoAc.setStatus(ApplicationStatus.APPROVED);
        statementDtoAc.setCreationDate(LocalDateTime.of(2024, 6, 12, 11, 54, 44).truncatedTo(ChronoUnit.SECONDS));
        statementDtoAc.setAppliedOffer(LoanOfferDto.builder().build());
        statementDtoAc.setSignDate(null);
        statementDtoAc.setSesCode("ses_code?");
    }

    @Test
    void convertEntityToDto() {
        StatementDto statementDtoEx = statementMapper.convertEntityToDto(statementAc);

        assertNotNull(statementDtoEx);
        assertEquals(statementDtoEx, statementDtoAc);
    }

    @Test
    void convertDtoToEntity() {
        Statement statementEx = statementMapper.convertDtoToEntity(statementDtoAc);

        assertNotNull(statementEx);
        assertEquals(statementEx, statementAc);
    }
}