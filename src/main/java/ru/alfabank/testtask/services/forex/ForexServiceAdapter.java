package ru.alfabank.testtask.services.forex;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import ru.alfabank.testtask.services.forex.ForexException.BadResultFromForex;
import ru.alfabank.testtask.services.forex.ForexException.BadCurrencyCode;
import ru.alfabank.testtask.services.forex.ForexException.BadDate;;

@Component
public class ForexServiceAdapter {

    @Autowired
    private Environment env;

    @Autowired
    private ForexServiceProxy forexProxy;

    public Double getRateAt(String currencyToCheck, LocalDate date) {
        if (date.isAfter(LocalDate.now()))
            throw new BadDate("Sorry, can't predict currencies");

        String ratesJson;
        try {
            ratesJson = forexProxy.getRateAndBaseAtDateAsJson(
                currencyToCheck, date.toString());

        }
        catch (Exception e) {
            throw new BadResultFromForex();
        }

        return getRateFromNode(getRatesNode(ratesJson), currencyToCheck);
    }

    private Double getRateFromNode(JsonNode ratesNode, String currencyToCheck)
            throws BadCurrencyCode {
        JsonNode currencyToCheckNode = ratesNode.path(currencyToCheck);
        JsonNode baseCurrencyNode = ratesNode.path(env.getProperty("services.forex.base_currency"));

        if (currencyToCheckNode.isMissingNode())
            throw new BadCurrencyCode("Bad requested currency code");

        return currencyToCheckNode.asDouble() / baseCurrencyNode.asDouble();
    }

    private JsonNode getRatesNode(String ratesJson)
            throws BadResultFromForex {
        try {
            JsonNode ratesNode = new ObjectMapper().readTree(ratesJson).path("rates");
            if (ratesNode.isMissingNode())
                throw new Exception();

            return ratesNode;
        }
        catch (Exception e) {
            throw new BadResultFromForex();
        }
    }

}
