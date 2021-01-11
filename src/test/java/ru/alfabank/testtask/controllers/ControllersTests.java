package ru.alfabank.testtask.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import io.restassured.RestAssured;
import ru.alfabank.testtask.services.forex.ForexService;
import ru.alfabank.testtask.services.gifsource.GifService;
import ru.alfabank.testtask.services.gifsource.GifServiceException;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

import static javax.servlet.http.HttpServletResponse.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ControllersTests {

    @MockBean
    ForexService forex;

    @MockBean
    GifService gifSource;

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class Home {

        @BeforeAll
        void setUp() {
            RestAssured.basePath = "/";
        }

        @Nested
        class GetRequest {

            @Test
            void respondeWithPage() {
                when()
                    .get()
                .then()
                    .statusCode(SC_OK)
                    .body(not(blankOrNullString()));
            }

        }

        @Nested
        class PostRequest {

            @Test
            void redirectsToAmirich() {
                String givenCurrency = "some_value";
                given()
                    .param("currency", givenCurrency)
                .when()
                    .redirects().follow(false)
                    .post()
                .then()
                    .statusCode(SC_FOUND)
                    .header("Location", endsWith("/amirich/" + givenCurrency));
            }

        }
    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class Amirich {

        static final String BASE_PATH = "/amirich";

        static final String CURRENCY_TO_CHECK = "USD";

        static final String RICH_URL = "rich_url";

        static final String BROKE_URL = "broke_url";

        static final String EXCEPTION_MESSAGE = "Some exception message";

        void setForexRespond(String currencyToCheck, Boolean isHigher) {
            try {
                Mockito.when(forex.isHigherThanYesterday(currencyToCheck))
                    .thenReturn(isHigher);
            }
            catch (Exception e) {}
        }

        @BeforeAll
        void setUp() {
            RestAssured.basePath = BASE_PATH;
        }

        @BeforeEach
        void setGifSourceRespond() throws GifServiceException {
            Mockito.when(gifSource.getBrokeGifUrl())
                .thenReturn(BROKE_URL);
            Mockito.when(gifSource.getRichGifUrl())
                .thenReturn(RICH_URL);
        }

        @Nested
        class GetRequest {

            @Test
            void returnRichUrlOnIncreasedRate() {
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
                Mockito.when(forex.isHigherThanYesterday(CURRENCY_TO_CHECK))
                    .thenThrow(new RuntimeException(EXCEPTION_MESSAGE));

                when()
                    .get(CURRENCY_TO_CHECK)
                .then()
                    .statusCode(SC_OK)
                    .body(containsString(EXCEPTION_MESSAGE));
            }

            @Test
            void containsMessageOfExceptionFromGetBrokeGifUrl() throws GifServiceException {
                setForexRespond(CURRENCY_TO_CHECK, false);
                Mockito.when(gifSource.getBrokeGifUrl())
                    .thenThrow(new RuntimeException(EXCEPTION_MESSAGE));

                when()
                    .get(CURRENCY_TO_CHECK)
                .then()
                    .statusCode(SC_OK)
                    .body(containsString(EXCEPTION_MESSAGE));
            }

            @Test
            void containsMessageOfExceptionFromGetRichGifUrl() throws GifServiceException {
                setForexRespond(CURRENCY_TO_CHECK, true);
                Mockito.when(gifSource.getRichGifUrl())
                    .thenThrow(new RuntimeException(EXCEPTION_MESSAGE));

                when()
                    .get(CURRENCY_TO_CHECK)
                .then()
                    .statusCode(SC_OK)
                    .body(containsString(EXCEPTION_MESSAGE));
            }

        }

    }

}
