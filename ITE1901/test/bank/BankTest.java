package bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class BankTest {
    private Bank bank;
    private Account account;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        account = new Account(1412345678, "Andy", 25000.0);
    }

    @Test
    void createAccount_SuccessfullyCreatesAnAccount() {
        assertTrue(bank.createAccount("Andy", 25000));
    }

    @Test
    void checkValidAccountHolderName_ReturnsTrueForValidAccountHolderName() {
        assertTrue(Bank.checkValidAccountHolderName("aaa"));
        assertTrue(Bank.checkValidAccountHolderName("Andy"));
    }

    @Test
    void checkValidAccountHolderName_ReturnsFalseForInvalidAccountHolderName() {
        assertFalse(Bank.checkValidAccountHolderName("sdasdasd"));
        assertFalse(Bank.checkValidAccountHolderName("dasdasd"));
    }
}
