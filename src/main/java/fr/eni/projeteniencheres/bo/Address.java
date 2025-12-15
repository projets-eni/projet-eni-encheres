package fr.eni.projeteniencheres.bo;

import java.io.Serializable;
import java.util.Objects;

public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id;
    private String street;
    private String zipCode;
    private String city;

    public Address() {
    }

    public Address(long id, String street, String zipCode, String city) {
        this.id = id;
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
    }

    public Address(String street, String zipCode, String city) {
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return id == address.id && Objects.equals(street, address.street) && Objects.equals(zipCode, address.zipCode) && Objects.equals(city, address.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, street, zipCode, city);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
