package com.payment;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Component
public class Payment {

    String debtorAccount;
    String creditorAccount;
    BigDecimal amount;
    Date date;

    public Payment(String debtorAccount, String creditorAccount, BigDecimal amount, Date date) {
        this.debtorAccount = debtorAccount;
        this.creditorAccount = creditorAccount;
        this.amount = amount;
        this.date = date;
    }

    public Payment() {
    }

    public String getDebtorAccount() {
        return debtorAccount;
    }

    public void setDebtorAccount(String debtorAccount) {
        this.debtorAccount = debtorAccount;
    }

    public String getCreditorAccount() {
        return creditorAccount;
    }

    public void setCreditorAccount(String creditorAccount) {
        this.creditorAccount = creditorAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment)) return false;
        Payment payment = (Payment) o;
        return debtorAccount.equals(payment.debtorAccount) &&
                creditorAccount.equals(payment.creditorAccount) &&
                amount.equals(payment.amount) &&
                date.equals(payment.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(debtorAccount, creditorAccount, amount, date);
    }
}
