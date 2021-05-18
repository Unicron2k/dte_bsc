package Modul6.BankThreads;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bank {
    private static final int TO_STRING_FREQUENCY=5000;
    private static final int TEST_FREQUENCY=1000;
    private Lock lock = new ReentrantLock();
    private long transactionCount;
    private int deviationCount;
    private int initialBalance;
    private Account[] accounts;
    private boolean debug;
    private int testCount;

    public Bank(int accountAmount, int initialBalance, boolean debug) {
        this.initialBalance=initialBalance;
        this.debug=debug;
        this.accounts = new Account[accountAmount];
        for(int i=0; i<accountAmount; i++){
            accounts[i]=new Account(accountAmount, initialBalance);
        }

    }
    public void transfer(int from, int to, int amount) {
        lock.lock();

        accounts[from].withdraw(amount);
        accounts[to].deposit(amount);
        transactionCount++;
        if (transactionCount % TEST_FREQUENCY == 0){ test(); }
        if (transactionCount % TO_STRING_FREQUENCY == 0){ System.out.println(toString()); }

        lock.unlock();
    }

    public void test() {
        this.testCount++;
        int sum = 0;
        for (Account account : accounts){
            sum += account.getBalance();
        }
        if(sum!=accounts.length*initialBalance){
            deviationCount++;
        }
        System.out.println(toString() + ", sum: " + sum);

    }

    @Override
    public String toString(){
        String string;
        if(debug){
            string=String.format("Debug: numberOfAccounts %s, transactionCount %d, deviations %d, errorPercentage %f, testCount %d",
                    numberOfAccounts(), getTransactionCount(), getDeviationCount(), getErrorPercentage(), this.testCount);
        }
        else{
            string = String.format("Transactions: %d, tests performed: %d", getTransactionCount(), this.testCount);
        }
        return string;
    }

    int numberOfAccounts(){
        return accounts.length;
    }
    long getTransactionCount(){
        return transactionCount;
    }
    int getDeviationCount(){
        return deviationCount;
    }
    double getErrorPercentage(){
        int sum=0;
        int total = numberOfAccounts()*initialBalance;
        for (Account account : accounts) {
            sum += account.getBalance();
        }
        return 100.0-(100.0/total)*sum;
    }
}