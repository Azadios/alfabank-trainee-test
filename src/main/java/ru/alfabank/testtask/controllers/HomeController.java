package ru.alfabank.testtask.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String showHomeView() {
        return "home";
    }

    @PostMapping
    public String redirectToAmirich(
            @ModelAttribute("currency") String currency,
            Model model) {
        if (currency.isBlank()) {
            model.addAttribute("errorMessage", "Please, enter code");
            return "home";
        }

        return "redirect:/amirich/" + currency;
    }

}
