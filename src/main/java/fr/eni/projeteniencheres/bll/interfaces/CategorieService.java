package fr.eni.projeteniencheres.bll.interfaces;

import fr.eni.projeteniencheres.bo.Categorie;

import java.util.List;

public interface CategorieService {
    List<Categorie> afficherCategories();
    Categorie afficherCategoryParId(int id);
}
