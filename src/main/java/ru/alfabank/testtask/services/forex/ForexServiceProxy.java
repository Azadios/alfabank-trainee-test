package ru.alfabank.testtask.services.forex;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="forexService", url="${services.forex.url}")
public interface ForexServiceProxy {

    // Can't set base currency on free subscription plan
    @GetMapping(value=  "/historical/{date}.json" +
                        "?app_id=${services.forex.app_id}" +
                        "&symbols=${services.forex.base_currency}" +
                        "%2C{currencyToCheck}")
    public String getRateAndBaseAtDateAsJson (
        @PathVariable("currencyToCheck") String currencyToCheck,
        @PathVariable("date") String date
    );

}
