package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.doman.Employment;
import com.prokofeva.deal_api.doman.Passport;
import com.prokofeva.deal_api.doman.dto.EmploymentDto;
import com.prokofeva.deal_api.doman.dto.PassportDto;
import com.prokofeva.deal_api.mapper.EmploymentMapper;
import com.prokofeva.deal_api.repositories.EmploymentRepo;
import com.prokofeva.deal_api.service.EmploymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmploymentServiceImpl implements EmploymentService {
    private final EmploymentRepo employmentRepo;
    private final EmploymentMapper employmentMapper;

    @Override
    public EmploymentDto createEmployment (){
       Employment employment = new Employment();

        return saveEmployment(employment);
    }

    @Override
    public EmploymentDto saveEmployment(Employment employment){
        return employmentMapper.convertEntityToDto(employmentRepo.save(employment));
    }

}
