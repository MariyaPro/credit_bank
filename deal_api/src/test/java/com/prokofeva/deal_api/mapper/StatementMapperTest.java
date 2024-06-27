package com.prokofeva.deal_api.mapper;

import com.prokofeva.deal_api.dto.ClientDto;
import com.prokofeva.deal_api.dto.CreditDto;
import com.prokofeva.deal_api.dto.LoanOfferDto;
import com.prokofeva.deal_api.dto.StatementDto;
import com.prokofeva.deal_api.enums.ApplicationStatus;
import com.prokofeva.deal_api.model.Client;
import com.prokofeva.deal_api.model.Credit;
import com.prokofeva.deal_api.model.Statement;
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
        statementDtoAc = StatementDto.builder()
                .statementId(UUID.fromString("06115ad596-0873-0087-5764-c1f3730d90"))
                .clientId(ClientDto.builder().build())
                .creditId(CreditDto.builder().build())
                .status(ApplicationStatus.APPROVED)
                .creationDate(LocalDateTime.of(2024, 6, 12, 11, 54, 44).truncatedTo(ChronoUnit.SECONDS))
                .appliedOffer(LoanOfferDto.builder().build())
                .signDate(null)
                .sesCode("ses_code?")
                .build();
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