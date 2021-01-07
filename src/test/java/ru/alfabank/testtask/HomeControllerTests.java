package ru.alfabank.testtask;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

import static javax.servlet.http.HttpServletResponse.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class HomeControllerTests {

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
