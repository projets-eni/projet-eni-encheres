package fr.eni.projeteniencheres.controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SessionController {

    @GetMapping("/api/keepalive")
    public void keepAlive(HttpSession session) {
        // ← VIDE ! Spring renvoie 200 OK automatiquement
    }
}
