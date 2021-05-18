package Modul1.NewAccount;

public class TestNewAccountOld {
    public static void main(String[] args){

        AccountOld acc = new AccountOld("Bob Kåre", 0001, 3000.00);

        AccountOld.setAnnualInterestRate(5.5);

        //Transaction trans = new Transaction('D', 300, 500, "Test");
        //System.out.printf("%s", trans.toString());

        acc.deposit(300,"Paycheck");
        acc.deposit(1000, "Gift from Dad");
        acc.deposit(30, "Finders fee");

        acc.withdraw(120, "Game");
        acc.withdraw(1256, "Computer hardware");
        acc.withdraw(800, "Spare parts, Car");


        System.out.printf("Name: %s%nAnnual interest rate: %.1f%nCurrent balance: %.2f%n%n%-31s%4s%10s%10s%s%s%n",
                acc.getName(), AccountOld.getAnnualInterestRate(), acc.getBalance(), "Date", "Type", "Ammount", "Balance", "    ", "Description" );
        for (Transaction t : acc.getTransactions()) {
            System.out.println(t);
        }
    }
}
