package fr.eni.projeteniencheres.bll;

import fr.eni.projeteniencheres.bll.interfaces.UtilisateurService;
import fr.eni.projeteniencheres.bo.Utilisateur;
import fr.eni.projeteniencheres.dal.interfaces.UtilisateurRepository;
import fr.eni.projeteniencheres.dto.UtilisateurAffichageDTO;
import fr.eni.projeteniencheres.dto.UtilisateurFormDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Utilisateur> findAllUtilisateurs() {
        return utilisateurRepository.findAllUtilisateurs();
    }

    @Override
    public Utilisateur findUtilisateurById(long no_utilisateur) {
        return utilisateurRepository.findUtilisateurById(no_utilisateur);
    }

    /**
     * Méthode pour trouver un utilisateurAffichageDTO par son id
     * utilisation de la méthode findUtilisateurById puis mapping vers le DTO
     * @param no_utilisateur
     * @return utilisateurAffichagedto
     */
    @Override
    public UtilisateurAffichageDTO findUtilisateurAffichageById(long no_utilisateur) {
        Utilisateur utilisateur = findUtilisateurById(no_utilisateur);
        UtilisateurAffichageDTO utilisateurAffichagedto = new UtilisateurAffichageDTO();
        BeanUtils.copyProperties(utilisateur, utilisateurAffichagedto);
        return utilisateurAffichagedto;
    }

    @Override
    public Utilisateur findUtilisateurByPseudo(String pseudo) {
        return utilisateurRepository.findUtilisateurByPseudo(pseudo);
    }

    @Override
    public Utilisateur findUtilisateurByEmail(String email) {
        return utilisateurRepository.findUtilisateurByEmail(email);
    }

    @Transactional
    @Override
    public void createUtilisateur(UtilisateurFormDTO dto) {

        Utilisateur utilisateur = new Utilisateur();

        //mapping DTO -> entité
        utilisateur.setPseudo(dto.getPseudo());
        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setTelephone(dto.getTelephone());
        utilisateur.setRue(dto.getRue());
        utilisateur.setCodePostal(dto.getCodePostal());
        utilisateur.setVille(dto.getVille());
        utilisateur.setMotDePasse(dto.getMotDePasse());

        //règles métier
        utilisateur.setCredit(100L);
        utilisateur.setAdministrateur(false);

        //Sécurité : encodage du mot de passe avant d'arriver en BDD
        utilisateur.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));

        utilisateurRepository.saveUtilisateur(utilisateur);
    }

    @Override
    public void deleteUtilisateurById(long no_utilisateur) {
        utilisateurRepository.deleteUtilisateurById(no_utilisateur);
    }

    @Override
    public Utilisateur updateUtilisateur(Utilisateur utilisateur) {
        utilisateurRepository.saveUtilisateur(utilisateur);
        return utilisateurRepository.findUtilisateurById(utilisateur.getNoUtilisateur());
    }
}
