package Modul6.BankThreads;

import java.util.Random;
import static java.lang.Thread.sleep;


public class AccountThreads implements Runnable{
    private Bank bank;
    private boolean debug;
    private int accountIndex;
    private int maxTransferAmount;
    Random random = new Random();

    AccountThreads(Bank bank, int accountIndex, int maxTransferAmount, boolean debug){
        this.debug=debug;
        this.accountIndex=accountIndex;
        this.maxTransferAmount = maxTransferAmount;
        this.bank=bank;
    }

    @Override
    public void run(){
        try {
            final int REPS=1000;
            while (!Thread.interrupted()) {
                for (int i = 0; i < REPS; i++) {
                    int toAccount = ( int ) (bank.numberOfAccounts() * Math.random());
                    int amount = ( int ) (maxTransferAmount * Math.random() / REPS);
                    bank.transfer(accountIndex, toAccount, amount);

                    if(debug)
                        sleep(15);
                    else
                        sleep(2);
                }
            }
        } catch (InterruptedException ignored) {
        }
    }
}