package com.payment;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PaymentRepositoryImpl implements PaymentRepository {

    public static final String COMMA = ",";
    final static Logger logger = Logger.getLogger(PaymentRepositoryImpl.class);
    private List<Payment> payments;
    private Map<File, List<Payment>> paymentsForFiles = new HashMap();


    @Override
    public void initialize(File inputCsv) {
        try {
            if (!paymentsForFiles.containsKey(inputCsv)) {
                InputStream inputFS = new FileInputStream(inputCsv);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
                this.payments = br.lines().map(mapToItem).collect(Collectors.toList());
                paymentsForFiles.put(inputCsv, this.payments);
                br.close();
            } else {
                this.payments = paymentsForFiles.get(inputCsv);
            }
        } catch (Exception e) {
            logger.error("Wrong input file.");
            if (payments != null) {
                payments.clear();
            }
            else{
                payments = new ArrayList<>();
            }
        }
    }

    private Function<String, Payment> mapToItem = (line) -> {
        String[] p = line.split(COMMA);
        Payment payment = new Payment();
        payment.setDebtorAccount(p[0]);
        payment.setCreditorAccount(p[1]);
        payment.setAmount(new BigDecimal(p[2]));
        payment.setDate(convertStringToDate(p[3]));
        return payment;
    };

    private Date convertStringToDate(String date) {

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date parse = formatter.parse(date);
            return parse;
        } catch (ParseException e) {
            logger.error("Wrong date format in csv file");
            return null;
        }
    }

    @Override
    @Cacheable
    public BigDecimal sumByCreditorAccount(String creditorAccount) {

        return this.payments.stream().filter(p -> p.getCreditorAccount().equals(creditorAccount))
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Cacheable
    public BigDecimal sumByAccounts(String creditorAccount, String debtorAccount) {
        return this.payments.stream().filter(p -> p.getCreditorAccount().equals(creditorAccount))
                .filter(p -> p.getDebtorAccount().equals(debtorAccount))
                .map(Payment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Cacheable
    public BigDecimal sumByMonth(int monthNumber) {
        return this.payments.stream().filter(payment -> convertToLocalDateTime(payment.getDate()).getMonthValue() == monthNumber)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return LocalDateTime.ofInstant(
                dateToConvert.toInstant(), ZoneId.systemDefault());
    }

    @Override
    public List<String> listUniqueDebtorAccounts() {
        return this.payments.stream().map(p -> String.valueOf(p.debtorAccount))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public boolean contains(Payment payment) {
        return this.payments.contains(payment);
    }

    @Override
    @Cacheable
    public List<Payment> getPaymentsForDebtor(String debtorAccount) {
        return this.payments.stream().filter(p -> p.getDebtorAccount().equals(debtorAccount)).collect(Collectors.toList());
    }

}
