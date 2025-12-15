package fr.eni.projeteniencheres.bo;

import java.util.List;
import java.util.Objects;

public class User extends Person{

    private static final long serialVersionUID = 1L;
    private String userName;
    private String email;
    private String password;
    private boolean admin;
    private boolean active;
    private List<Auction> auctions;

    public User() {
    }

    public User(long id, String firstName, String lastName, String phone, long credit, Address address, String userName, String email, String password, boolean admin, boolean active) {
        super(id, firstName, lastName, phone, credit, address);
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.admin = admin;
        this.active = active;
    }

    public User(String firstName, String lastName, String phone, long credit, Address address, String userName, String email, String password, boolean admin, boolean active) {
        super(firstName, lastName, phone, credit, address);
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.admin = admin;
        this.active = active;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return admin == user.admin && active == user.active && Objects.equals(userName, user.userName) && Objects.equals(email, user.email) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userName, email, password, admin, active);
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", admin=" + admin +
                ", active=" + active +
                '}';
    }
}
