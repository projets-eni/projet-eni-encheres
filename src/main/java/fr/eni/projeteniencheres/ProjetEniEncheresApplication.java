package fr.eni.projeteniencheres;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProjetEniEncheresApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjetEniEncheresApplication.class, args);
    }

}
