package ru.alfabank.testtask.services.gifsource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GifService {

    @Autowired
    GifServiceAdapter adapter;

    public String getRichGifUrl() throws GifServiceException {
        return adapter.getRichGifUrl();
    }

    public String getBrokeGifUrl() throws GifServiceException {
        return adapter.getBrokeGifUrl();
    }

}
