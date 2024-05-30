package api.dieti2024.controller;

import api.dieti2024.model.Utente;
import api.dieti2024.repository.UserRepository;
import api.dieti2024.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProvaController {

    @GetMapping("/prova/hello")
    public String hello() {
        return "Hello";
    }

}
