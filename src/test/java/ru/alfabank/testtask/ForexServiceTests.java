package ru.alfabank.testtask;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ru.alfabank.testtask.services.forex.ForexService;
import ru.alfabank.testtask.services.forex.ForexServiceAdapter;

@SpringBootTest
public class ForexServiceTests {

    @MockBean
    ForexServiceAdapter adapter;

    @Autowired
    ForexService forex;

    static final String CURRENCY_TO_CHECK = "USD";

    private void setAdapterRespond(Double yesterdayResponse, Double todayResponse) {
        try {
            Mockito.when(adapter.getRateAt(CURRENCY_TO_CHECK, LocalDate.now().minusDays(1)))
                .thenReturn(yesterdayResponse);
            Mockito.when(adapter.getRateAt(CURRENCY_TO_CHECK, LocalDate.now()))
                .thenReturn(todayResponse);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void isHigherThanYesterdayShouldReturnTrueOnIncreasedRate() throws Exception {
        setAdapterRespond(1.0, 2.0);
        assertTrue(forex.isHigherThanYesterday(CURRENCY_TO_CHECK));
    }

    @Test
    void isHigherThanYesterdayShouldReturnFalseOnDecreasedRate() throws Exception {
        setAdapterRespond(2.0, 1.0);
        assertFalse(forex.isHigherThanYesterday(CURRENCY_TO_CHECK));
    }

    @Test
    void isHigherThanYesterdayShouldReturnFalseOnEqualRate() throws Exception {
        setAdapterRespond(2.0, 2.0);
        assertFalse(forex.isHigherThanYesterday(CURRENCY_TO_CHECK));
    }

}
