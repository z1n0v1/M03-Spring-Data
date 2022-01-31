package entity.T05BillsPaymentSystem;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "credit_cards")
public class CreditCard extends BillingDetails {
    private String cardType;
    private Integer expirationMonth;
    private Integer expirationYear;

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Integer getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(Integer expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public Integer getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(Integer expirationYear) {
        this.expirationYear = expirationYear;
    }
}
