package fr.eni.projeteniencheres.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    //gérer l'accès aux ressources et définir les routes qui ne nécessitent aucune connexion
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/encheres", "/inscription", "/connexion", "/css/**", "/js/**", "/images/*").permitAll()
                        .requestMatchers("/profil/*", "/vente/creer", "/vente/*").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/connexion")
//                        .loginProcessingUrl("/connexion")
                        .usernameParameter("pseudo")
                        .passwordParameter("motDePasse")
                        .defaultSuccessUrl("/encheres", true)
                        .permitAll())
                .rememberMe(rememberMe -> rememberMe
                        .key("uniqueAndSecret").userDetailsService(userDetailsService))
                .logout(logout -> logout
                        .deleteCookies("JSESSIONID")
                        .logoutUrl("/logout")       // URL à appeler pour déconnecter
                        .logoutSuccessUrl("/encheres")
                        .permitAll());

        return http.build();
    }

    @Bean
    //Hachage du mot de passe
    public PasswordEncoder passwordEncoder() {
        //spring pourra utiliser plusieurs algorithmes
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        //pratique quand on veut tester le mot de passe en clair
//        return NoOpPasswordEncoder.getInstance();
    }
}
