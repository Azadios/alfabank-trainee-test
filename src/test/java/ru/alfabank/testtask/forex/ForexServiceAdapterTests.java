package ru.alfabank.testtask.forex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;

import ru.alfabank.testtask.services.forex.ForexException;
import ru.alfabank.testtask.services.forex.ForexServiceAdapter;
import ru.alfabank.testtask.services.forex.ForexServiceProxy;

@SpringBootTest
public class ForexServiceAdapterTests {

    @MockBean
    ForexServiceProxy serviceProxy;

    @Autowired
    ForexServiceAdapter forexAdapter;

    static final String CURRENCY_TO_CHECK = "USD";
    static final Double CURRENCY_RATE = 2.0;
    static final Double BASE_RATE = 1.0;
    static final LocalDate DAY = LocalDate.of(2020, 01, 01);

    static String jsonWithRates;

    @BeforeAll
    static void setUpJson(@Autowired Environment env) throws Exception {
        Map<String, Double> rates = new HashMap<>();
        rates.put(env.getProperty("services.forex.base_currency"), BASE_RATE);
        rates.put(CURRENCY_TO_CHECK, CURRENCY_RATE);

        jsonWithRates = new JSONObject()
            .put("rates", new JSONObject(rates)).toString();
    }

    private void setProxyRespond(String currency, LocalDate day, String response) {
        try {
            Mockito.when(serviceProxy.getRateAndBaseAtDateAsJson(currency, day.toString()))
                .thenReturn(response);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nested
    class GetRateAt {

        @Test
        void returnCorrectRate() throws ForexException {
            setProxyRespond(CURRENCY_TO_CHECK, DAY, jsonWithRates);
            assertEquals(CURRENCY_RATE, forexAdapter.getRateAt(CURRENCY_TO_CHECK, DAY));
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", "weird rates", "{\"rates\":}"})
        void throwOnWrongJson(String responseJson) {
            setProxyRespond(CURRENCY_TO_CHECK, DAY, responseJson);
            assertThrows(ForexException.BadResultFromForex.class,
                () -> forexAdapter.getRateAt(CURRENCY_TO_CHECK, DAY));
        }

        @Test
        void throwOnFutureDayRate() {
            LocalDate futureDay = LocalDate.now().plusDays(2);

            setProxyRespond(CURRENCY_TO_CHECK, futureDay, jsonWithRates);
            assertThrows(ForexException.BadDate.class,
                () -> forexAdapter.getRateAt(CURRENCY_TO_CHECK, futureDay));
        }

        @Test
        void throwOnBadCurrencyCode() {
            final String badCurrency = "NON";

            setProxyRespond(badCurrency, DAY, jsonWithRates);
            assertThrows(ForexException.BadCurrencyCode.class,
                () -> forexAdapter.getRateAt(badCurrency, DAY));
        }

    }

}
