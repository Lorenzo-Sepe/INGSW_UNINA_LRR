package api.dieti2024.controller;

import api.dieti2024.dto.auth.CredenzialiUtenteDTO;
import api.dieti2024.exceptions.ApiException;
import api.dieti2024.security.AuthService;
import api.dieti2024.util.ControllerRestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/testhello")
    public String testHello() {
        return "Hello";
    }

    @PostMapping("/signup")
    public ResponseEntity<String> genTokenAfterSingUp(@RequestBody CredenzialiUtenteDTO signUpRequest) {
        try {
            String token = authService.registrazione(signUpRequest);
            return ResponseEntity.ok(token);
        } catch (ApiException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public String GenTokenBylogin(@RequestBody CredenzialiUtenteDTO credenzialiInserite) {
        return (authService.login(credenzialiInserite));
    }

    @PostMapping("/SalvaTest")
    public String salvaTest() {
        authService.saveTest();
        return "Test salvato";
    }
}
