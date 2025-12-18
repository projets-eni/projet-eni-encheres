package fr.eni.projeteniencheres.bll.interfaces;

import fr.eni.projeteniencheres.bo.Utilisateur;
import fr.eni.projeteniencheres.dto.UtilisateurAffichageDTO;
import fr.eni.projeteniencheres.dto.UtilisateurFormDTO;

import java.util.List;

public interface UtilisateurService {

    List<Utilisateur> findAllUtilisateurs();
    Utilisateur findUtilisateurById(long no_utilisateur);
    UtilisateurAffichageDTO findUtilisateurAffichageById(long no_utilisateur);
    Utilisateur findUtilisateurByPseudo(String pseudo);
    Utilisateur findUtilisateurByEmail(String email);
    void createUtilisateur(UtilisateurFormDTO dto);
    void deleteUtilisateurById(long no_utilisateur);
    Utilisateur updateUtilisateur(Utilisateur utilisateur);
}
