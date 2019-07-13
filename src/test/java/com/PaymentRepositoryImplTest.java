package com;

import com.payment.Payment;
import com.payment.PaymentRepository;
import com.payment.PaymentRepositoryImpl;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class PaymentRepositoryImplTest {

    private PaymentRepository paymentRepository;
    public static final String WORKING_DIRECTORY = System.getProperty("user.dir") + "\\src\\test\\java\\com";

    @Before
    public void setUp() {
        this.paymentRepository = new PaymentRepositoryImpl();
    }

    @Test
    public void initializeTest() {
        //given
        File file = new File(WORKING_DIRECTORY + "\\src\\test\\java\\com\\payment.csv");
        //when
        paymentRepository.initialize(file);
    }

    @Test
    public void initializeWrongFileTest() {
        //given
        File file = new File(WORKING_DIRECTORY + "\\src\\test\\java\\com\\incorrect_payment.csv");
        //when
        paymentRepository.initialize(file);
    }

    @Test
    public void sumByCreditorAccountTest() {
        //given
        File file = new File(WORKING_DIRECTORY + "\\payment.csv");
        String creditorAccount = "4567 0000 0000 0000 1111";
        //when
        paymentRepository.initialize(file);
        BigDecimal result = paymentRepository.sumByCreditorAccount(creditorAccount);
        //then
        assert result.intValue() == 4750;
    }

    @Test
    public void sumByCreditorAccountWrongFileTest() {
        //given
        File file = new File(WORKING_DIRECTORY + "\\incorrect_payment.csv");
        String creditorAccount = "4567 0000 0000 0000 1111";
        //when
        paymentRepository.initialize(file);
        BigDecimal result = paymentRepository.sumByCreditorAccount(creditorAccount);
        //then
        assert result.intValue() == 0;
    }

    @Test
    public void sumByAccountsTest() {
        //given
        File file = new File(WORKING_DIRECTORY + "\\payment.csv");
        String creditorAccount = "4567 0000 0000 0000 1111";
        String debtorAccount = "1234 0000 4567 0000 5555";
        //when
        paymentRepository.initialize(file);
        BigDecimal result = paymentRepository.sumByAccounts(creditorAccount, debtorAccount);
        //then
        assert result.intValue() == 1900;
    }

    @Test
    public void sumByMonthTest() {
        //given
        File file = new File(WORKING_DIRECTORY + "\\payment.csv");
        int monthNumber = 1;
        //when
        paymentRepository.initialize(file);
        BigDecimal result = paymentRepository.sumByMonth(monthNumber);
        //then
        assert result.intValue() == 3800;
    }

    @Test
    public void listUniqueDebtorAccountsTest() {
        //given
        File file = new File(WORKING_DIRECTORY + "\\three_unique_debtors_accounts.csv");
        String firstAccount = "1234 0000 4567 0000 5555";
        String secondAccount = "1234 5555 4567 0000 5555";
        //when
        paymentRepository.initialize(file);
        List<String> result = paymentRepository.listUniqueDebtorAccounts();
        //then
        assert result.size() == 3;
        assert result.get(0).equals(firstAccount);
        assert result.get(1).equals(secondAccount);
    }

    @Test
    public void containsTest() {
        //given
        File file = new File(WORKING_DIRECTORY + "\\payment.csv");
        Payment payment = new Payment("1234 0000 4567 0000 5555", "4567 0000 0000 0000 1111", new BigDecimal("950"), new Date("01/07/2019"));
        //when
        paymentRepository.initialize(file);
        boolean result = paymentRepository.contains(payment);
        //then
        assert result;
    }

    @Test
    public void getPaymentsForDebtorTest() {
        //given
        File file = new File(WORKING_DIRECTORY + "\\payment.csv");
        String debtorAccount = "1234 0000 4567 0000 5555";
        Payment payment = new Payment("1234 0000 4567 0000 5555", "4567 0000 0000 0000 1111", new BigDecimal("950"), new Date("01/07/2019"));
        //when
        paymentRepository.initialize(file);
        List<Payment> result = paymentRepository.getPaymentsForDebtor(debtorAccount);
        //then
        assert result.size() == 3;
        assert result.get(0).equals(payment);
    }
}