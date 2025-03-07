package com.prokofeva.calculator_api.service;

import com.prokofeva.dto.ScoringDataDto;

import java.math.BigDecimal;

public interface ScoringService {
    BigDecimal calculateRate(boolean isInsuranceEnabled, boolean isSalaryClient, String logId);

    BigDecimal scoring(ScoringDataDto scoringDataDto, String logId);
}