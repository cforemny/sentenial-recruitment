package com.payment;

import org.springframework.stereotype.Repository;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PaymentRepository {

    void initialize(File inputCsv);

    BigDecimal sumByCreditorAccount(String creditorAccount);

    //return a sum of transactions executed between given accounts
    BigDecimal sumByAccounts(String creditoAccount, String debtorAccount);

    // returns a total amount for a given month 1-Jan, 2-Feb...
    BigDecimal sumByMonth(int monthNumber);

    List<String> listUniqueDebtorAccounts();

    boolean contains(Payment payment);

    List<Payment> getPaymentsForDebtor(String debtorAccount);

}
