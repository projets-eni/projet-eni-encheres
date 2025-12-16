package fr.eni.projeteniencheres.bll;

import fr.eni.projeteniencheres.bll.interfaces.CategorieService;
import fr.eni.projeteniencheres.bo.Categorie;
import fr.eni.projeteniencheres.dal.interfaces.CategorieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategorieServiceImpl implements CategorieService {

    private final CategorieRepository categorieRepository;

    public CategorieServiceImpl(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    @Override
    public List<Categorie> afficherCategories() {
        return categorieRepository.afficherCategories();
    }

    @Override
    public Categorie afficherCategoryParId(int id) {
        return categorieRepository.afficherCategoryParId(id);
    }
}
