package com.prokofeva.calculator_api.service;

import java.math.BigDecimal;

public interface InsuranceService {
     BigDecimal calculateInsurance(BigDecimal amount, Integer term, String logId);
}
