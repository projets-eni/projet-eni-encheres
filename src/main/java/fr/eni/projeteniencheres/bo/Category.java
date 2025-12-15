package fr.eni.projeteniencheres.bo;

import java.io.Serializable;
import java.util.Objects;

public class Category implements Serializable {

    private static final long serialVersionUID = 1L ;
    private long id ;
    private String label ;

    public Category() {
    }
    public Category(long id, String label) {
        this.id = id;
        this.label = label;
    }
    public Category(String label) {
        this.label = label;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", label='" + label + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Category category)) return false;
        return id == category.id && Objects.equals(label, category.label);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, label);
    }
}
