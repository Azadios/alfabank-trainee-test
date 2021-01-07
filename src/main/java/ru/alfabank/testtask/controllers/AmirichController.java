package ru.alfabank.testtask.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.alfabank.testtask.services.forex.ForexService;
import ru.alfabank.testtask.services.gifsource.GifService;

@Controller
@RequestMapping("/amirich")
public class AmirichController {

    @Autowired
    ForexService forex;

    @Autowired
    GifService gifSource;

    @GetMapping("/{currency}")
    public String showGifAccordingToWealth(
            @PathVariable("currency") String currency,
            Model model) {
        try {
            String gifUrl;

            if (forex.isHigherThanYesterday(currency)) {
                gifUrl = gifSource.getRichGifUrl();
            }
            else {
                gifUrl = gifSource.getBrokeGifUrl();
            }

            model.addAttribute("gifUrl", gifUrl);
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "amirich";
    }

}
