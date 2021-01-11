package ru.alfabank.testtask.services.forex;

public class ForexException extends Exception {

    static final long serialVersionUID = 1L;

    private ForexException(String message) {
        super(message);
    };

    private ForexException(String message, Throwable cause) {
        super(message, cause);
    };

    public static class BadDate extends ForexException {
        static final long serialVersionUID = 1L;

        public BadDate(String message) {
            super(message);
        }

        public BadDate(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class BadCurrencyCode extends ForexException {
        static final long serialVersionUID = 1L;

        public BadCurrencyCode(String message) {
            super(message);
        }

        public BadCurrencyCode(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class BadResultFromForex extends ForexException {
        static final long serialVersionUID = 1L;

        static final String STD_MESSAGE = "Sorry, there's issue with forex service";

        public BadResultFromForex(String message) {
            super(message);
        }

        public BadResultFromForex(String message, Throwable cause) {
            super(message, cause);
        }

        public BadResultFromForex() {
            super(STD_MESSAGE);
        }

        public BadResultFromForex(Throwable cause) {
            super(STD_MESSAGE, cause);
        }
    }

}
