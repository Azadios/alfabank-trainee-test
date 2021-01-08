package ru.alfabank.testtask.gifsource;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ru.alfabank.testtask.services.gifsource.GifService;
import ru.alfabank.testtask.services.gifsource.GifServiceAdapter;

@SpringBootTest
public class GifServiceTests {

    @MockBean
    GifServiceAdapter adapter;

    @Autowired
    GifService service;

    static final String CORRECT_URL = "https://media.giphy.com/media/LdOyjZ7io5Msw/giphy.gif";

    void setAdapterRespond(String url) {
        Mockito.when(adapter.getRichGifUrl()).thenReturn(url);
        Mockito.when(adapter.getBrokeGifUrl()).thenReturn(url);
    }

    @Nested
    class GetRichGifUrl {

        @Test
        void returnNotBlankOnCorrectUrl() {
            setAdapterRespond(CORRECT_URL);
            assertFalse(service.getRichGifUrl().isBlank());
        }

    }

    @Nested
    class GetBrokeGifUrl {

        @Test
        void returnNotBlankOnCorrectUrl() {
            setAdapterRespond(CORRECT_URL);
            assertFalse(service.getBrokeGifUrl().isBlank());
        }

    }

}
