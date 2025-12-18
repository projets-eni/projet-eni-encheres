package fr.eni.projeteniencheres.dal;

import fr.eni.projeteniencheres.bo.Utilisateur;
import net.datafaker.Faker;

public class populateFakeDatas {

    private static final Faker faker = new Faker();
/*
    public static void main(String[] args) {
        SpringApplication.run(ProjetEniEncheresApplication.class, args);



    }
*/

    static public Utilisateur utilisateur() {

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNoUtilisateur(faker.number().numberBetween(1,100));
        utilisateur.setPseudo(faker.internet().username());
        utilisateur.setMotDePasse(faker.internet().password());
        utilisateur.setNom(faker.name().fullName());
        utilisateur.setPrenom(faker.name().firstName());
        utilisateur.setCredit(faker.number().numberBetween(100,10000));
        utilisateur.setEmail(faker.internet().emailAddress());
        utilisateur.setAdministrateur(faker.bool().bool());
        utilisateur.setRue(faker.address().streetAddress());
        utilisateur.setVille(faker.address().city());
        utilisateur.setCodePostal(faker.address().zipCode());
        utilisateur.setTelephone(faker.phoneNumber().phoneNumber());

        return utilisateur;
    }


}
