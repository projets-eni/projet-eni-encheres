package fr.eni.projeteniencheres.dal.interfaces;

import fr.eni.projeteniencheres.bo.Categorie;

import java.util.List;

public interface CategorieRepository {
    List<Categorie> afficherCategories();
    Categorie afficherCategoryParId(int id);
}
