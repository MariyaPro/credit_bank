package com.prokofeva.deal_api.service;

import com.prokofeva.deal_api.doman.dto.ClientDto;
import com.prokofeva.deal_api.doman.dto.LoanStatementRequestDto;

public interface ClientService {
    ClientDto saveClient(LoanStatementRequestDto loanStatementRequestDto);
}
