package dev.hafil.demospringboot.controller;

import dev.hafil.demospringboot.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class HomeController {
    @GetMapping("/")
    public String homePage(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return "Hello "+user.getUsername();
    }
}
