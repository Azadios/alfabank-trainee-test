package ru.alfabank.testtask.services.gifsource;

public class GifServiceException extends Exception {

    static final long serialVersionUID = 1L;

    static final String STD_MESSAGE = "Sorry, there's issue with gif source";

    public GifServiceException() {
        super(STD_MESSAGE);
    }

    public GifServiceException(Throwable cause) {
        super(STD_MESSAGE, cause);
    }

}
