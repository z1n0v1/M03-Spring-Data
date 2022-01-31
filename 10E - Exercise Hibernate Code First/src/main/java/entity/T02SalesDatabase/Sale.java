package entity.T02SalesDatabase;

import entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Sale extends BaseEntity {
    private Product product;
    private Customer customer;
    private StoreLocation storeLocation;
    private LocalDateTime date;

    @ManyToOne
    public StoreLocation getStoreLocation() {
        return storeLocation;
    }

    @ManyToOne
    public Customer getCustomer() {
        return customer;
    }

    @ManyToOne
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Column(name = "store_location_id")
    public void setStoreLocation(StoreLocation storeLocation) {
        this.storeLocation = storeLocation;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
