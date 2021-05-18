package bank;

public class Account {
  private Integer accountNumber;
  private String accountHolder;
  private Double accountBalance; // Static here

  public Account(Integer accountNumber, String accountHolder, Double accountBalance) {
    this.accountNumber = accountNumber;
    this.accountHolder = accountHolder;
    this.accountBalance = accountBalance;
  }


  // TODO: Return values for withdraw():boolean and deposit():boolean are never used
  public boolean withdraw(double amount) {
    if (amount <= 0)
      return false;

    if (amount < accountBalance) {
      accountBalance -= amount;
      return true;
    } else
      return false;
  }

  public boolean deposit(double amount) {
    if (amount > 0) {
      accountBalance += amount;
      return true;
    } else
      return false;
  }


    /* Getters */
    Integer getAccountNumber() {
        return accountNumber;
    }

    String getAccountHolder() {
        return accountHolder;
    }

    Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double amount) {
        this.accountBalance = amount;
    }
}
