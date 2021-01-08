package ru.alfabank.testtask.forex;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.alfabank.testtask.services.forex.ForexServiceProxy;

@SpringBootTest
public class ForexServiceProxyTests {

    @Autowired
    ForexServiceProxy forexProxy;

    @Test
    void resultForUsdTodayShouldNotBeZero() throws Exception {
        assertNotEquals(0, getUsdRateAt(LocalDate.now()));
    }

    @Test
    void resultForUsdYesterdayShouldNotBeZero() throws Exception {
        assertNotEquals(0, getUsdRateAt(LocalDate.now().minusDays(1)));
    }

    private Double getUsdRateAt(LocalDate date) throws Exception {
        String ratesJson = forexProxy.getRateAndBaseAtDateAsJson("USD", date.toString());

        return new ObjectMapper().readTree(ratesJson)
            .path("rates").path("USD").asDouble();
    }

}
