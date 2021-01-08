package ru.alfabank.testtask.services.gifsource;

public class GifServiceException extends RuntimeException {

    static final long serialVersionUID = 1L;

    public GifServiceException() {
        super("Sorry, there's issue with gif source");
    }

}
