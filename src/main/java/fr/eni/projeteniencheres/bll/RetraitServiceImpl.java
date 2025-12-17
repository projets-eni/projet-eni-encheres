package fr.eni.projeteniencheres.bll;

import fr.eni.projeteniencheres.bll.interfaces.RetraitService;
import fr.eni.projeteniencheres.bo.Retrait;
import fr.eni.projeteniencheres.dal.interfaces.RetraitRepository;
import org.springframework.stereotype.Service;

@Service
public class RetraitServiceImpl implements RetraitService {

    private RetraitRepository retraitRepository;
    public RetraitServiceImpl(RetraitRepository retraitRepository) {
        this.retraitRepository = retraitRepository;
    }

    @Override
    public Retrait ajoutRetrait(Retrait retrait) {
        // Par défaut, si non renseigné, le retrait se fait à l'adresse du vendeur
        if (retrait.getVille() == null ){
            String rueVendeur = retrait.getRue();
            String cpVendeur = retrait.getCodePostal();
            String villeVendeur = retrait.getVille();
            Retrait retraitALAdresseDuVendeur = new Retrait();
            retraitALAdresseDuVendeur.setRue(rueVendeur);
            retraitALAdresseDuVendeur.setCodePostal(cpVendeur);
            retraitALAdresseDuVendeur.setVille(villeVendeur);
        }
        retrait.setEstRetire(false);
        return retraitRepository.ajoutRetrait(retrait);
    }
}
