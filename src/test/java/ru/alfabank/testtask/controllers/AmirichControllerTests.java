package ru.alfabank.testtask.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import io.restassured.RestAssured;
import ru.alfabank.testtask.services.forex.ForexService;
import ru.alfabank.testtask.services.gifsource.GifService;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

import static javax.servlet.http.HttpServletResponse.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AmirichControllerTests {

    @MockBean
    ForexService forex;

    @MockBean
    GifService gifSource;

    static final String BASE_PATH = "/amirich";

    static final String CURRENCY_TO_CHECK = "USD";

    static final String RICH_URL = "rich";

    static final String BROKE_URL = "broke";

    void setForexRespond(String currencyToCheck, Boolean isHigher) {
        try {
            Mockito.when(forex.isHigherThanYesterday(currencyToCheck))
                .thenReturn(isHigher);
        }
        catch (Exception e) {}
    }

    @BeforeAll
    static void setUp() {
        RestAssured.basePath = BASE_PATH;
    }

    @BeforeEach
    void setGifSourceRespond() {
        Mockito.when(gifSource.getBrokeGifUrl())
            .thenReturn(BROKE_URL);
        Mockito.when(gifSource.getRichGifUrl())
            .thenReturn(RICH_URL);
    }

    @Nested
    class GetRequest {

        @Test
        void returnBrokeUrlOnIncreasedRate() {
            setForexRespond(CURRENCY_TO_CHECK, true);

            when()
                .get(CURRENCY_TO_CHECK)
            .then()
                .statusCode(SC_OK)
                .body(containsString(RICH_URL));
        }

        @Test
        void returnBrokeUrlOnDecreasedRate() {
            setForexRespond(CURRENCY_TO_CHECK, false);

            when()
                .get(CURRENCY_TO_CHECK)
            .then()
                .statusCode(SC_OK)
                .body(containsString(BROKE_URL));
        }

        @Test
        void containsMessageOfExceptionFromIsHigherThanYesterday() throws Exception {
            final String exceptionMessage = "Some exception message";

            Mockito.when(forex.isHigherThanYesterday(CURRENCY_TO_CHECK))
                .thenThrow(new RuntimeException(exceptionMessage));

            when()
                .get(CURRENCY_TO_CHECK)
            .then()
                .body(containsString(exceptionMessage));
        }

        @Test
        void containsMessageOfExceptionFromGetBrokeGifUrl() {
            final String exceptionMessage = "Some exception message";

            setForexRespond(CURRENCY_TO_CHECK, false);
            Mockito.when(gifSource.getBrokeGifUrl())
                .thenThrow(new RuntimeException(exceptionMessage));

            when()
                .get(CURRENCY_TO_CHECK)
            .then()
                .body(containsString(exceptionMessage));
        }

        @Test
        void containsMessageOfExceptionFromGetRichGifUrl() {
            final String exceptionMessage = "Some exception message";

            setForexRespond(CURRENCY_TO_CHECK, true);
            Mockito.when(gifSource.getRichGifUrl())
                .thenThrow(new RuntimeException(exceptionMessage));

            when()
                .get(CURRENCY_TO_CHECK)
            .then()
                .body(containsString(exceptionMessage));
        }

    }

}
