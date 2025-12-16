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
        return retraitRepository.ajoutRetrait(retrait);
    }
}
