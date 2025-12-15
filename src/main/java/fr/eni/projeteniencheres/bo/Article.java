package fr.eni.projeteniencheres.bo;

import java.io.Serializable;
import java.util.Objects;

public class Article implements Serializable {

    private static final long serialVersionUID = 1L ;
    private long id;
    private String name ;
    private String description ;
    private Category category ;

    public Article() {
    }
    public Article(long id, String name, String description, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
    }
    public Article(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Article article)) return false;
        return id == article.id && Objects.equals(name, article.name) && Objects.equals(description, article.description) && Objects.equals(category, article.category);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, category);
    }
}
