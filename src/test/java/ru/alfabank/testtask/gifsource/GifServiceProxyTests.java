package ru.alfabank.testtask.gifsource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.json.JSONObject;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.alfabank.testtask.services.gifsource.GifServiceProxy;

@SpringBootTest
public class GifServiceProxyTests {

    @Autowired
    GifServiceProxy gifSource;

    @Nested
    class GetJsonWithRandomGifByTag {

        @Test
        void returnJsonOnTagRich() {
            assertDoesNotThrow(() ->
                new JSONObject(gifSource.getJsonWithRandomGifByTag("rich")));
        }

    }

}
