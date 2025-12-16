package fr.eni.projeteniencheres.dal;

import fr.eni.projeteniencheres.bo.Categorie;
import fr.eni.projeteniencheres.dal.interfaces.CategorieRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategorieRepositoryImpl implements CategorieRepository {

    @Override
    public List<Categorie> afficherCategories() {
        return List.of();
    }

    @Override
    public Categorie afficherCategoryParId(int id) {
        return null;
    }
}
