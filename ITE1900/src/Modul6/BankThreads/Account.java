package Modul6.BankThreads;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private int balance;
    private int accountNumber;
    Lock lock = new ReentrantLock();
    Condition lockCondition = lock.newCondition();

    public Account(int accountNumber, int balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    void withdraw(int amount) {
        try {
            while (!(balance >= amount)) {
                lockCondition.await();
                }
            lock.lock();
            balance -= amount;
            lock.unlock();
        }
        catch(InterruptedException ex){
            ex.printStackTrace();
        }
    }

    void deposit(int amount) {
        balance += amount;
    }

    int getAccountNumber() {
        return accountNumber;
    }
    public int getBalance() {
        return balance;
    }
    Lock getLock(){
        return lock;
    }
}