package fr.eni.projeteniencheres.bo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public abstract class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id;
    private String firstName;
    private String lastName;
    private String phone;
    private long credit;
    private Address address;

    public Person() {
    }

    public Person(long id, String firstName, String lastName, String phone, long credit, Address address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.credit = credit;
        this.address = address;
    }

    public Person(String firstName, String lastName, String phone, long credit, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.credit = credit;
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getCredit() {
        return credit;
    }

    public void setCredit(long credit) {
        this.credit = credit;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id && credit == person.credit && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(phone, person.phone) && Objects.equals(address, person.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, phone, credit, address);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", credit=" + credit +
                ", address=" + address +
                '}';
    }
}
