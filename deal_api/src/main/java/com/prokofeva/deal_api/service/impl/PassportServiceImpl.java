package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.doman.Passport;
import com.prokofeva.deal_api.doman.dto.PassportDto;
import com.prokofeva.deal_api.mapper.PassportMapper;
import com.prokofeva.deal_api.repositories.PassportRepo;
import com.prokofeva.deal_api.service.PassportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PassportServiceImpl implements PassportService {
    private final PassportRepo passportRepo;
    private final PassportMapper passportMapper;

    @Override
    public PassportDto createPassport (String passportSeries, String  passportNumber){
        Passport passport = new Passport();
        passport.setSeries(passportSeries);
        passport.setNumber(passportNumber);

        return savePassport(passport);
    }

    @Override
    public PassportDto savePassport(Passport passport){
        return passportMapper.convertEntityToDto(passportRepo.save(passport));
    }
}
