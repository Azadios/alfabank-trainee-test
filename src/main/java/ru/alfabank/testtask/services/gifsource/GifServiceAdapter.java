package ru.alfabank.testtask.services.gifsource;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GifServiceAdapter {

    @Autowired
    private GifServiceProxy proxy;

    public String getRichGifUrl() throws GifServiceException {
        try {
            String jsonWithGif = proxy.getJsonWithRandomGifByTag("rich");

            return getFixedHeightGifUrl(jsonWithGif);
        }
        catch (Exception e) {
            throw new GifServiceException("Sorry, there's issue with gif service");
        }
    }

    public String getBrokeGifUrl() throws GifServiceException {
        try {
            String jsonWithGif = proxy.getJsonWithRandomGifByTag("broke");

            return getFixedHeightGifUrl(jsonWithGif);
        }
        catch (Exception e) {
            throw new GifServiceException("Sorry, there's issue with gif service");
        }
    }

    private String getFixedHeightGifUrl(String jsonWithGif) throws Exception {
        return new ObjectMapper().readTree(jsonWithGif)
            .path("data").path("images").path("fixed_height").path("url")
            .asText();
    }

}
