package com.batch.extract.gkelabbackend;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
// @CrossOrigin est utile pour les tests locaux mais sera géré par notre proxy Nginx dans K8s
@CrossOrigin(origins = "http://localhost:3000")
public class HelloController {

    @GetMapping("/hello")
    public Map<String, String> sayHello() {
        return Map.of("message", "Bonjour depuis le Backend Spring Boot sur GKE ! 🎉");
    }
}
