package fr.eni.projeteniencheres.bll.interfaces;

import fr.eni.projeteniencheres.bo.Utilisateur;

import java.util.List;

public interface UtilisateurService {

    List<Utilisateur> findAllUtilisateurs();
    Utilisateur findUtilisateurById(long no_utilisateur);
    Utilisateur findUtilisateurByPseudo(String pseudo);
    Utilisateur findUtilisateurByEmail(String email);
    void createUtilisateur(Utilisateur utilisateur);
}
