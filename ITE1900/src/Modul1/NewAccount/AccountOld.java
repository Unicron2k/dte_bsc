package Modul1.NewAccount;

import java.util.ArrayList;

class AccountOld {
  private ArrayList<Transaction> transactions = new ArrayList<>();
  private int id;
  private double balance;
  private static double annualInterestRate;
  private java.util.Date dateCreated;
  private String name;

  public AccountOld() {
    this("NoName", 0000, 0000);
  }

  public AccountOld(String name, int newId, double newBalance) {
    this.name = name;
    this.id = newId;
    this.balance = newBalance;
    this.dateCreated = new java.util.Date();
  }

  public int getId() {
    return this.id;
  }

  public double getBalance() {
    return balance;
  }

  public static double getAnnualInterestRate() {
    return annualInterestRate;
  }

  public String getName(){ return name; }

  public ArrayList<Transaction> getTransactions(){
    return transactions;
  }

  public void setId(int newId) {
    id = newId;
  }

  public void setBalance(double newBalance) {
    balance = newBalance;
  }

  public static void setAnnualInterestRate(double newAnnualInterestRate) {
    annualInterestRate = newAnnualInterestRate;
  }

  public void setName(String name){ this.name = name; }

  public double getMonthlyInterest() {
    return balance * (annualInterestRate / 1200);
  }

  public java.util.Date getDateCreated() {
    return dateCreated;
  }

  public void withdraw(double amount, String description) {
    balance -= amount;
    transactions.add(new Transaction('W', amount, balance, description));
  }

  public void deposit(double amount, String description) {
    balance += amount;
    transactions.add(new Transaction('D', amount, balance, description));
  }
}
