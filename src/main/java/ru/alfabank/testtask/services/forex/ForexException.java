package ru.alfabank.testtask.services.forex;

public class ForexException {

    private ForexException() {};

    public static class BadDate extends RuntimeException {
        static final long serialVersionUID = 1L;

        public BadDate(String message) {
            super(message);
        }
    }

    public static class BadCurrencyCode extends RuntimeException {
        static final long serialVersionUID = 1L;

        public BadCurrencyCode(String message) {
            super(message);
        }
    }

    public static class BadResultFromForex extends RuntimeException {
        static final long serialVersionUID = 1L;

        public BadResultFromForex(String message) {
            super(message);
        }
    }

}
