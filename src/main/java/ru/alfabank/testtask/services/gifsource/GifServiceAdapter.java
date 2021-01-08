package ru.alfabank.testtask.services.gifsource;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GifServiceAdapter {

    @Autowired
    private GifServiceProxy proxy;

    public String getRichGifUrl() throws GifServiceException {
        String jsonWithGif = proxy.getJsonWithRandomGifByTag("rich");

        return getFixedHeightGifUrl(jsonWithGif);
    }

    public String getBrokeGifUrl() {
        String jsonWithGif = proxy.getJsonWithRandomGifByTag("broke");

        return getFixedHeightGifUrl(jsonWithGif);
    }

    private String getFixedHeightGifUrl(String jsonWithGif) {
        try {
            return new ObjectMapper().readTree(jsonWithGif)
                .path("data").path("images").path("fixed_height").path("url")
                .asText();
        }
        catch (Exception e) {
            throw new GifServiceException();
        }
    }

}
