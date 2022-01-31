package entity.T05BillsPaymentSystem;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "bank_accounts")
public class BankAccount extends BillingDetails {
    private String bankName;
    private String swift;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getSwift() {
        return swift;
    }

    public void setSwift(String swift) {
        this.swift = swift;
    }
}
