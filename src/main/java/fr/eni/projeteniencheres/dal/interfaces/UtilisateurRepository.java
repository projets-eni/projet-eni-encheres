package fr.eni.projeteniencheres.dal.interfaces;

import fr.eni.projeteniencheres.bo.Utilisateur;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository {

    List<Utilisateur> findAllUtilisateurs();
    Utilisateur findUtilisateurById(long no_utilisateur);
    Utilisateur findUtilisateurByPseudo(String pseudo);
    Utilisateur findUtilisateurByEmail(String email);
    Optional<Utilisateur> findUtilisateurByPseudoOrEmail(String pseudo, String email);
    void saveUtilisateur(Utilisateur utilisateur);
    void deleteUtilisateurById(long no_utilisateur);
    Utilisateur updateUtilisateur(Utilisateur utilisateur);
}
