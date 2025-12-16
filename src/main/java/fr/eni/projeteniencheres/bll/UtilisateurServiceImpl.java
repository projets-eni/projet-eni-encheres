package fr.eni.projeteniencheres.bll;

import fr.eni.projeteniencheres.bll.interfaces.UtilisateurService;
import fr.eni.projeteniencheres.bo.Utilisateur;
import fr.eni.projeteniencheres.dal.interfaces.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    private UtilisateurRepository utilisateurRepository;

    @Autowired
    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public List<Utilisateur> findAllUtilisateurs() {
        return utilisateurRepository.findAllUtilisateurs();
    }

    @Override
    public Utilisateur findUtilisateurById(long no_utilisateur) {
        return utilisateurRepository.findUtilisateurById(no_utilisateur);
    }

    @Override
    public Utilisateur findUtilisateurByPseudo(String pseudo) {
        return null;
    }

    @Override
    public Utilisateur findUtilisateurByEmail(String email) {
        return null;
    }

    @Override
    public void createUtilisateur(Utilisateur utilisateur) {
        utilisateur.setAdministrateur(false);
        utilisateurRepository.saveUtilisateur(utilisateur);
    }
}
