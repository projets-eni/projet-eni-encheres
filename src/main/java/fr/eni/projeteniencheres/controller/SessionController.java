package fr.eni.projeteniencheres.controller;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SessionController {

//    @GetMapping("/api/keepalive")
//    public void keepAlive(HttpSession session) {
//        // ← VIDE ! Spring renvoie 200 OK automatiquement
//    }

    @PostMapping("/keep-alive")
    @ResponseBody
    public void keepAlive(HttpSession session) {
        // Il suffit d'accéder à la session pour la garder active
        session.getId();
    }
}
