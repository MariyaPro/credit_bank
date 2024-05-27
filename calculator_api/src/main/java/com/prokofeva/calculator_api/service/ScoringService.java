package com.prokofeva.calculator_api.service;

import com.prokofeva.calculator_api.doman.ScoringDataDto;

import java.math.BigDecimal;

public interface ScoringService {
    BigDecimal calculateRate(boolean isInsuranceEnabled, boolean isSalaryClient);

    BigDecimal scoring(ScoringDataDto scoringDataDto);
}
