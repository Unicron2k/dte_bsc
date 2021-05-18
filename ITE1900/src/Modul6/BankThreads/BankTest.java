package Modul6.BankThreads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BankTest {
    private static final boolean DEBUG = false;
    private static final int INITIAL_BALANCE = 10000;
    private static final int ACCOUNT_AMOUNT =10;



    public static void main(String[] args) {
        Bank bank = new Bank(ACCOUNT_AMOUNT, INITIAL_BALANCE, DEBUG);
        int i;

        ExecutorService pool = Executors.newFixedThreadPool(ACCOUNT_AMOUNT);

        for (i = 0; i < ACCOUNT_AMOUNT; i++) {
            pool.execute(new AccountThreads(bank, i, INITIAL_BALANCE, DEBUG));
        }
        pool.shutdown();
    }
}