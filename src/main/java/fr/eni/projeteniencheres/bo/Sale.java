package fr.eni.projeteniencheres.bo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Sale implements Serializable {

    private static final long serialVersionUID = 1L ;
    private long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private float startPrice;
    private float endPrice;
    private boolean status;
    private Address addressWithdrawal ;
    private Article article ;

    public Sale() {
    }
    public Sale(long id, LocalDateTime startDate, LocalDateTime endDate, float startPrice, float endPrice, boolean status, Address addressWithdrawal, Article article) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startPrice = startPrice;
        this.endPrice = endPrice;
        this.status = status;
        this.addressWithdrawal = addressWithdrawal;
        this.article = article;
    }
    public Sale(LocalDateTime startDate, LocalDateTime endDate, float startPrice, float endPrice, boolean status, Address addressWithdrawal, Article article) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.startPrice = startPrice;
        this.endPrice = endPrice;
        this.status = status;
        this.addressWithdrawal = addressWithdrawal;
        this.article = article;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public LocalDateTime getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    public LocalDateTime getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    public float getStartPrice() {
        return startPrice;
    }
    public void setStartPrice(float startPrice) {
        this.startPrice = startPrice;
    }
    public float getEndPrice() {
        return endPrice;
    }
    public void setEndPrice(float endPrice) {
        this.endPrice = endPrice;
    }
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public Address getAddressWithdrawal() {
        return addressWithdrawal;
    }
    public void setAddressWithdrawal(Address addressWithdrawal) {
        this.addressWithdrawal = addressWithdrawal;
    }
    public Article getArticle() {
        return article;
    }
    public void setArticle(Article article) {
        this.article = article;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", startPrice=" + startPrice +
                ", endPrice=" + endPrice +
                ", status=" + status +
                ", addressWithdrawal=" + addressWithdrawal +
                ", article=" + article +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Sale sale)) return false;
        return id == sale.id && Float.compare(startPrice, sale.startPrice) == 0 && Float.compare(endPrice, sale.endPrice) == 0 && status == sale.status && Objects.equals(startDate, sale.startDate) && Objects.equals(endDate, sale.endDate) && Objects.equals(addressWithdrawal, sale.addressWithdrawal) && Objects.equals(article, sale.article);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, endDate, startPrice, endPrice, status, addressWithdrawal, article);
    }
}
