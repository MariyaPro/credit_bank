package com.prokofeva.deal_api.service;

import com.prokofeva.deal_api.doman.Passport;
import com.prokofeva.deal_api.doman.dto.PassportDto;

public interface PassportService {
    PassportDto createPassport (String passportSeries, String  passportNumber);
    PassportDto savePassport(Passport passport);
}
