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
     * Méthode pour trouver un utilisateurAffichageDTO par son pseudo
     * utilisation de la méthode findUtilisateurByPseudo puis mapping vers le DTO
     * @param pseudo
     * @return utilisateurAffichagedto
     */
    @Override
    public UtilisateurAffichageDTO findUtilisateurAffichageByPseudo(String pseudo) {
        Utilisateur utilisateur = findUtilisateurByPseudo(pseudo);
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
        //uniformiser le numéro de téléphone en BDD
        String telephoneNormalise = dto.getTelephone().replaceAll("[ .-]", "");
        utilisateur.setTelephone(telephoneNormalise);
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
    @Transactional
    public void deleteUtilisateurById(long no_utilisateur) {
        utilisateurRepository.deleteUtilisateurById(no_utilisateur);
    }

    @Override
    public Utilisateur updateUtilisateur(Utilisateur utilisateur) {
        utilisateurRepository.saveUtilisateur(utilisateur);
        return utilisateurRepository.findUtilisateurById(utilisateur.getNoUtilisateur());
    }

    @Override
    public UtilisateurFormDTO getUtilisateurFormByPseudo(String pseudo) {

        Utilisateur utilisateur = utilisateurRepository.findUtilisateurByPseudo(pseudo);

        UtilisateurFormDTO dto = new UtilisateurFormDTO();
        //je récupère les données objet de la BDD et je les convertis en DTO pour pré remplir le formulaire
        dto.setPseudo(utilisateur.getPseudo());
        dto.setNom(utilisateur.getNom());
        dto.setPrenom(utilisateur.getPrenom());
        dto.setTelephone(utilisateur.getTelephone());
        dto.setEmail(utilisateur.getEmail());
        dto.setRue(utilisateur.getRue());
        dto.setCodePostal(utilisateur.getCodePostal());
        dto.setVille(utilisateur.getVille());

        //pour des raisons de sécurité, je n'affiche pas le mot de passe
        dto.setMotDePasse(null);
        dto.setConfirmationMotDePasse(null);

        return dto;
    }

    @Override
    public void updateUtilisateur(String pseudo, UtilisateurFormDTO dto) {
        Utilisateur utilisateur = utilisateurRepository.findUtilisateurByPseudo(pseudo);

        //pour des raisons de cohérence, je ne permets pas à l'utilisateur de modifier le pseudo, celui-ci est unique
        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setTelephone(dto.getTelephone());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setRue(dto.getRue());
        utilisateur.setCodePostal(dto.getCodePostal());
        utilisateur.setVille(dto.getVille());

        if (dto.getMotDePasse() != null && !dto.getMotDePasse().isBlank()) {
            utilisateur.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        }

        utilisateurRepository.saveUtilisateur(utilisateur);

    }

}
