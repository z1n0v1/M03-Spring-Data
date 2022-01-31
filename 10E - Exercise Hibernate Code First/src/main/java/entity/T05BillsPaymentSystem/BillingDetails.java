package entity.T05BillsPaymentSystem;

import entity.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "billing_details")
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BillingDetails extends BaseEntity {
    private String number;
    private User owner;

    @Column(name = "number", nullable = false)
    public String getNumber() {
        return number;
    }

    public void setNumber(String  number) {
        this.number = number;
    }

    @OneToOne
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
