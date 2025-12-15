package fr.eni.projeteniencheres.bo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Auction  implements Serializable {

    private static final long serialVersionUID = 1L ;
    private Sale sale ;
    private Person seller ;

    private LocalDateTime date ;
    private float amount ;

    public Auction() {
    }
    public Auction(Sale sale, Person seller, LocalDateTime date, float amount) {
        this.sale = sale;
        this.seller = seller;
        this.date = date;
        this.amount = amount;
    }

    public Sale getSale() {
        return sale;
    }
    public void setSale(Sale sale) {
        this.sale = sale;
    }
    public Person getSeller() {
        return seller;
    }
    public void setSeller(Person seller) {
        this.seller = seller;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public float getAmount() {
        return amount;
    }
    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Auction{" +
                "sale=" + sale +
                ", seller=" + seller +
                ", date=" + date +
                ", amount=" + amount +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Auction auction)) return false;
        return Float.compare(amount, auction.amount) == 0 && Objects.equals(sale, auction.sale) && Objects.equals(seller, auction.seller) && Objects.equals(date, auction.date);
    }
    @Override
    public int hashCode() {
        return Objects.hash(sale, seller, date, amount);
    }
}
