package com.prokofeva.deal_api.service;

import com.prokofeva.deal_api.doman.Employment;
import com.prokofeva.deal_api.doman.dto.EmploymentDto;

public interface EmploymentService {
    EmploymentDto createEmployment ();
    EmploymentDto saveEmployment(Employment employment);
}
