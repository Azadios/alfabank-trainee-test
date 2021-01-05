package ru.alfabank.testtask.services.forex;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ForexServiceAdapter {

    @Autowired
    private Environment env;

    @Autowired
    private ForexServiceProxy forexProxy;

    public Double getRateAt(String currencyToCheck, LocalDate date) throws Exception {
        if (date.isAfter(LocalDate.now())) {
            throw new ForexException.BadDate("Sorry, can't predict currencies");
        }

        String ratesJson = forexProxy.getRateAndBaseAtDateAsJson(currencyToCheck, date.toString());

        return getRateFromNode(getRatesNode(ratesJson), currencyToCheck);
    }

    private Double getRateFromNode(JsonNode ratesNode, String currencyToCheck)
            throws ForexException.BadCurrencyCode {
        JsonNode currencyToCheckNode = ratesNode.path(currencyToCheck);
        JsonNode baseCurrencyNode = ratesNode.path(env.getProperty("services.forex.base_currency"));

        if (currencyToCheckNode.isMissingNode())
            throw new ForexException.BadCurrencyCode("Bad requested currency code");

        return currencyToCheckNode.asDouble() / baseCurrencyNode.asDouble();
    }

    private JsonNode getRatesNode(String ratesJson)
            throws ForexException.BadResultFromForex {
        try {
            JsonNode ratesNode = new ObjectMapper().readTree(ratesJson).path("rates");
            if (ratesNode.isMissingNode())
                throw new Exception();

            return ratesNode;
        }
        catch (Exception e) {
            var exception = new ForexException.BadResultFromForex("There's issue with forex service, sorry");
            exception.setStackTrace(e.getStackTrace());
            throw exception;
        }
    }

}
