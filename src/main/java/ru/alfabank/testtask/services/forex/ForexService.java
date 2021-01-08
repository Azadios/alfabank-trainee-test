package ru.alfabank.testtask.services.forex;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ForexService {

    @Autowired
    ForexServiceAdapter adapter;

    public Boolean isHigherThanYesterday(String currencyToCheck) {
        Double todayRate = adapter.getRateAt(currencyToCheck, LocalDate.now());
        Double yesterdayRate = adapter.getRateAt(currencyToCheck, LocalDate.now().minusDays(1));

        return todayRate > yesterdayRate;
    }

}
