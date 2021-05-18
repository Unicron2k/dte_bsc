package Modul1.NewAccount;

import java.util.Date;

public class Transaction {

    private java.util.Date date; //Transaction date.
    private char type; //W-Withdraw/D-Deposit
    private double amount;
    private double balance;
    private String description;

    public Transaction(char type, double amount, double balance, String description) {
        this.date = new Date();
        this.type = type;
        this.amount = amount;
        this.balance = balance;
        this.description = description;
    }
    /*
    public void setDescription(String description) {this.description = description;}
    public void setDate(Date date) {this.date = date;}
    public void setType(char type) {this.type = type;}
    public void setAmount(double amount) {this.amount = amount;}
    public void setBalance(double balance) {this.balance = balance;}
    */

    public Date getDate() {
        return date;
    }

    public char getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalance() {
        return balance;
    }

    public String getDescription() {
        return description;
    }
    @Override
    public String toString() {
        return String.format("%-30s %4C %9.2f %9.2f %s %s", getDate(), getType(), getAmount(), getBalance(), "  ", getDescription());
    }
}