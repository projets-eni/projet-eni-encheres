//package fr.eni.projeteniencheres.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//public class WebSecurityConfig {
//
//    @Bean
//    //gérer l'accès aux ressources et définir les routes qui ne nécessitent aucune connexion
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/", "/inscription", "/connexion", "/css/*", "/images/*").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/films/creer").hasRole("ADMIN")
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form
//                        .loginPage("/connexion")
//                        .loginProcessingUrl("/connexion")
//                        .defaultSuccessUrl("/accueil", true)
//                        .permitAll())
//                .logout(logout -> logout
//                        .logoutUrl("/logout")       // URL à appeler pour déconnecter
//                        .logoutSuccessUrl("/accueil")
//                        .permitAll());
//
//        return http.build();
//    }
//
//    @Bean
//    //Hachage du mot de passe
//    public PasswordEncoder passwordEncoder() {
//        //spring pourra utiliser plusieurs algorithmes
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        //pratique quand on veut tester le mot de passe en clair
////        return NoOpPasswordEncoder.getInstance();
//    }
//}
