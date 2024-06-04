package com.prokofeva.deal_api.service.mapper;

import com.prokofeva.deal_api.doman.Statement;
import com.prokofeva.deal_api.doman.dto.StatementDto;

public class StatementMapper {
    public static StatementDto convertEntityToDto(Statement statement){
        StatementDto statementDto = new StatementDto();
        statementDto.setStatementId(statement.getStatementId());
        statementDto.setClientId(statement.getClientId());
        statementDto.setCreditId(statement.getCreditId());
        statementDto.setStatus(statement.getStatus());
        statementDto.setCreationDate(statement.getCreationDate());
        statementDto.setAppliedOffer(statement.getAppliedOffer());
        statementDto.setSignDate(statement.getSignDate());
        statementDto.setSesCode(statement.getSesCode());

        return statementDto;
    }
}
