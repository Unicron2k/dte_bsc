package bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account(484739470, "John Appleseed", 2000.01);
    }

    //<editor-fold desc="Method tests: withdraw(Double):boolean">
    @Test
    void withdraw_ReturnsFalseIfInputIsNegative() {
        assertFalse(account.withdraw(-0.1));
    }

    @Test
    void withdraw_ReturnsFalseIfInputIsZero() {
        assertFalse(account.withdraw(0));
    }

    @Test
    void withdraw_ReturnsTrueIfInputBelowBalance() {
        assertTrue(account.withdraw(account.getAccountBalance() - 1));
    }

    @Test
    void withdraw_ReturnsTrueIfInputExactlyLikeBalance() {
        assertTrue(account.withdraw(account.getAccountBalance()));
    }

    @Test
    void withdraw_ReturnsFalseIfInputAmountMoreThanBalance() {
        assertFalse(account.withdraw(2000.1));
    }
    //</editor-fold>

    //<editor-fold desc="Method tests: deposit(Double):boolean">
    @Test
    void deposit_ReturnsFalseIfInputIsNegative() {
        assertFalse(account.deposit(-0.1));
    }

    @Test
    void deposit_ReturnsFalseIfInputIsZero() {
        assertFalse(account.deposit(0));
    }

    @Test
    void deposit_ReturnsTrueIfInputIsPositive() {
        assertTrue(account.deposit(0.1));
    }
    //</editor-fold>
}
