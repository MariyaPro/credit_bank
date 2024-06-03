package com.prokofeva.deal_api.doman;

import com.prokofeva.deal_api.doman.enums.EmploymentPosition;
import com.prokofeva.deal_api.doman.enums.EmploymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

public class Employment {
    private UUID employmentId;
    private EmploymentStatus status;
    private String employerInn;
    private BigDecimal salary;
    private EmploymentPosition position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
