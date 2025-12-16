package fr.eni.projeteniencheres.dal.interfaces;

import fr.eni.projeteniencheres.bo.Utilisateur;

import java.util.List;

public interface UtilisateurRepository {

    List<Utilisateur> findAllUtilisateurs();
    Utilisateur findUtilisateurById(long no_utilisateur);
    Utilisateur findUtilisateurByPseudo(String pseudo);
    Utilisateur findUtilisateurByEmail(String email);
    void saveUtilisateur(Utilisateur utilisateur);
}
