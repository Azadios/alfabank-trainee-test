package ru.alfabank.testtask.gifsource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

import ru.alfabank.testtask.services.gifsource.GifServiceAdapter;
import ru.alfabank.testtask.services.gifsource.GifServiceException;
import ru.alfabank.testtask.services.gifsource.GifServiceProxy;

@SpringBootTest
@TestPropertySource("classpath:test.properties")
public class GifServiceAdapterTests {

    @MockBean
    GifServiceProxy proxy;

    @Autowired
    GifServiceAdapter adapter;

    static String CORRECT_JSON;
    static String INCORRECT_JSON;
    static String responseFileName = "gifServiceResponse.json";

    @BeforeAll
    static void setUp(@Autowired Environment env) throws Exception {
        CORRECT_JSON = new String(Files.readAllBytes(Paths.get(env.getProperty("test.src_dir"), responseFileName)));
        INCORRECT_JSON = "incorrect json";
    }

    void setProxyRespond(String tag, String result) {
        try {
            Mockito.when(proxy.getJsonWithRandomGifByTag(tag))
                .thenReturn(result);
        }
        catch (Exception e) {}
    }

    @Nested
    class GetRichGifUrl {

        final String tag = "rich";

        @Test
        void returnNotBlankOnCorrectJson() {
            setProxyRespond(tag, CORRECT_JSON);
            assertFalse(adapter.getRichGifUrl().isBlank());
        }

        @Test
        void throwsOnIncorrectJson() {
            setProxyRespond(tag, INCORRECT_JSON);
            assertThrows(GifServiceException.class, () -> adapter.getRichGifUrl());
        }

    }

    @Nested
    class GetBrokeGifUrl {

        final String tag = "broke";

        @Test
        void returnNotBlankOnCorrectJson() {
            setProxyRespond(tag, CORRECT_JSON);
            assertFalse(adapter.getBrokeGifUrl().isBlank());
        }

        @Test
        void throwsOnIncorrectJson() {
            setProxyRespond(tag, INCORRECT_JSON);
            assertThrows(GifServiceException.class, () -> adapter.getBrokeGifUrl());
        }

    }

}
