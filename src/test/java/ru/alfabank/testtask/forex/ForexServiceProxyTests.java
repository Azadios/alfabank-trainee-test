package ru.alfabank.testtask.forex;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import ru.alfabank.testtask.services.forex.ForexServiceProxy;

@SpringBootTest
public class ForexServiceProxyTests {

    @Autowired
    ForexServiceProxy forexProxy;

    static final LocalDate DAY = LocalDate.of(2020, 1, 1);

    static final String CURRENCY_TO_CHECK = "USD";

    @Autowired
    Environment env;

    @Nested
    class GetRateAndBaseAtDateAsJson {

        @Test
        void containsNonZeroRateAndBase() throws Exception {
            String ratesJson = forexProxy.getRateAndBaseAtDateAsJson(
                CURRENCY_TO_CHECK, DAY.toString());
            JsonNode rates = new ObjectMapper().readTree(ratesJson).path("rates");

            assertNotEquals(0.0, rates.path(CURRENCY_TO_CHECK).asDouble());
            assertNotEquals(0.0, rates.path(env.getProperty("services.forex.base_currency")).asDouble());
        }

    }

}
