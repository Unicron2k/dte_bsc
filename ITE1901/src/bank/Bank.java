package bank;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Bank {

    private static TransferWriter transferWriter;
    private static TransferReader transferReader = new TransferReader();
    private static BufferedWriter bwErrorWriter;
    private static BufferedWriter bwTransferWriter;
    private static BufferedReader brTransferReader;

    private static Map<Integer, Account> accounts = new HashMap<>();

    static final String TRANSFER_FILE_NAME = "src/Transfer File.csv";
    static final String ERROR_FILE_NAME = "src/Error Log File.txt";
    private static final int NUMBER_OF_INPUTS_FOR_USER = 5;

    public Bank() {
        try {
            //brTransferReader = new BufferedReader();
            bwTransferWriter = new BufferedWriter(new FileWriter(Bank.TRANSFER_FILE_NAME, true));
            bwErrorWriter = new BufferedWriter(new FileWriter(Bank.ERROR_FILE_NAME, true));

            transferWriter = new TransferWriter(bwTransferWriter, bwErrorWriter);
        } catch(IOException ex) {
            System.out.println("Could not initilize file I/O: " + ex.getMessage());
            System.exit(-1);
        }

    }

    /**
     * Creates a new account and puts it in the HashMap 'accounts'
     * @param accountHolder String for owner of this account
     * @param amount Initial balance for the new account
     * @return boolean stating success of Account creation
     */
    public Boolean createAccount(String accountHolder, double amount) {
        if (checkValidAccountHolderName(accountHolder) && amount >= 0) {
            Account account = new Account(generateAccountNumber(), accountHolder, amount);
            accounts.put(account.getAccountNumber(), account);
            return true;
        } else
            return false;
    }

    /**
     * Generate Transfer object and write it to transfer file
     * @param sourceAccountNumber Account who sends
     * @param destinationAccountNumber Account who receives
     * @param amount Amount to send
     * @return boolean stating if transfer was successful
     * @throws IOException IOException when writing transfer
     */
    public boolean newTransfer(int sourceAccountNumber, int destinationAccountNumber, double amount) throws IOException {
        if (amount == 0 || amount < 0)
            return false;
        if (!checkIfAccountNumberUnique(sourceAccountNumber) || !checkIfAccountNumberUnique(destinationAccountNumber)) {
            // Write to ERROR_FILE_NAME which was non-existent
            writeToErrorLogFile(getNonExistentAccountNumbersAsString(sourceAccountNumber, destinationAccountNumber), new Object() {}.getClass().getEnclosingMethod().getName(), getClassName(), null);
            return false;
        }

        Account accountFrom = getAccountByID(sourceAccountNumber);
        Account accountTo = getAccountByID(destinationAccountNumber);

        assert accountFrom != null;
        if (accountFrom.getAccountBalance() >= amount) {
            Transfer transfer = new Transfer(accountFrom, accountTo, amount);
            // Write transfer to TRANSFER_FILE_NAME and return true for success
            return transferWriter.writeTransfer(transfer);
        }
        return false;
    }

    /**
     * Reads all transfers from TRANSFER_FILE_NAME and executes all transfers
     * @return Boolean stating success or not
     */
    public boolean executePendingTransfers() {
        // Fetch all Transfer objects from ERROR_FILE_NAME and put it in a List
        List<Transfer> transfersFromTransferFile = transferReader.readTransfers();

        // Traverse all Transfers in List
        for (int i = 0; i < transfersFromTransferFile.size(); i++) {
            Transfer transfer = transfersFromTransferFile.get(i);
            Account accountFrom = transfersFromTransferFile.get(i).getAccountFrom();
            Account accountTo = transfersFromTransferFile.get(i).getAccountFrom();

            // Execute transfers, write to ERROR_FILE_NAME if insufficiency in funds
            // TODO: Current method works on newly created Account objects, need to work on HashMap 'accounts' !!!!
            if (accountFrom.getAccountBalance() >= transfer.getAmount()) {
                executeTransferInHashMap(accountFrom, accountTo, transfer.getAmount());

                return true;
            } else
                writeToErrorLogFile(String.format("Transfer #%s was not executed!! accountNumber %s did not have sufficient funds", i + 1, accountFrom.getAccountNumber()), new Object() {}.getClass().getEnclosingMethod().getName(), getClassName(), null); // TODO: Ensure 1-based indexing is OK for visual representation
        }
        return false;
    }

    /**
     * Return specific Account Object
     * @param accountId Find Account by specific id
     * @return Found Account or NULL for no result
     */
    public static Account getAccountByID(int accountId) {
        // Traverse HashMap accounts and find specific Account by accountId
        for (Map.Entry<Integer, Account> object : accounts.entrySet()) {
            Account account = object.getValue();
            if (account.getAccountNumber() == accountId)
                return account;
        }
        return null;
    }


    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        Bank bank = new Bank();
        System.out.println("\t\t\t\tUiT Bank INC");
        System.out.println("\tCopyright 1998-2019 UiT Incorporation");
        System.out.println("\t\t\t\t -Server 1-\n");
        System.out.println("Terminal manufactured by Andy");

        // TODO: Clean inputs for error/exceptions of user inputs
        while (true) {
            try {
                System.out.print(getDefaultMenu());
                int userChoice = input.nextInt();
                printUserInputFields(bank, userChoice);
            } catch (InputMismatchException ex) {
                System.out.println("Invalid input, try again!\n\n");
            }
        }
    }

    /**
     * Prints user choices and fetches input, used to simplify main method
     * @param bank Bank used in the session
     * @param userChoice Users current input to be printed out
     */
    private static void printUserInputFields(Bank bank, int userChoice) {
        Scanner input = new Scanner(System.in);
        switch (userChoice) {
            case 1:
                try {
                    System.out.print("\nName for user (2 or 3 words): ");
                    String accountHolder = input.nextLine();
                    //input.nextLine();
                    System.out.print("Start balance for user (balance>0): ");
                    double accountBalance = input.nextDouble();
                    if (bank.createAccount(accountHolder, accountBalance))
                        System.out.printf("\t-> Success! Account with name \"%s\" created with an initial balance of %s\n\n", accountHolder, accountBalance);
                    else
                        System.out.print("\t-> Account not created, check your inputs!\n\n");
                } catch (InputMismatchException ex) {
                    System.out.println("Invalid input, Account creation aborted!!\n\n");
                }
                break;
            case 2:
                try {
                    System.out.print("\nAccount number to transfer from: ");
                    int sourceAccountNumber = input.nextInt();
                    System.out.print("Account number to transfer to: ");
                    int destinationAccountNumber = input.nextInt();
                    System.out.print("Amount to transfer: ");
                    double amount = input.nextDouble();
                    if (bank.newTransfer(sourceAccountNumber, destinationAccountNumber, amount))
                        System.out.printf("\t -> Success! A pending transfer of %.2f from #%s to #%s was created.\n\n", amount, sourceAccountNumber, destinationAccountNumber);
                    else
                        System.out.print("\t-> Transfer not made! Please check the Error log for further information!\n\n");
                } catch (InputMismatchException | IOException ex) {
                    System.out.println("Invalid input, transfer aborted!!\n\n");
                }
                break;
            case 3:
                System.out.printf("\n|%6s|%6s|%6s|\n", "Account holder", "Number", "Balance");
                for (Account account : getAllAccounts())
                    System.out.printf("|%6s|%6s|%6s|\n", account.getAccountHolder(), account.getAccountNumber(), account.getAccountBalance());
                System.out.println("");
                break;
            case 4:
                if (bank.executePendingTransfers())
                    System.out.print("All pending transfers executed...\n\n");
                else
                    System.out.print("Could not execute pending transfers, check Error log!\n\n");
                break;
            case 5:
                if (bank.executePendingTransfers()) {
                    System.out.print("All pending transfers executed, exiting program with code 0...");
                    System.exit(0);
                } else
                    System.out.print("Could not execute pending transfer, check Error log!\n\n");
                break;

             // Testing
            case -1:
                List<Transfer> pendingTransfers = transferReader.readTransfers();
                for (Transfer i : pendingTransfers)
                    System.out.println(i.getAmount());

            default:
                System.out.printf("%s is an invalid input, please check your inputs!", userChoice);
        }
    }

    private static String getDefaultMenu() {
        StringBuilder output = new StringBuilder("________________________________________________________\n");
        output.append("Which Bank Entry would you like to access?\n");
        output.append("1) - Create new account\n");
        output.append("2) - Transfer between two account numbers\n");
        output.append("3) - Print all Accounts\n");
        output.append("4) - Execute all pending transfers\n");
        output.append("5) - Exit program and execute all pending transfers\n");

        output.append(String.format(" --> Enter [%s] -> [%s]: ", 1, Bank.NUMBER_OF_INPUTS_FOR_USER));
        return output.toString();
    }












            //////////////////////////
            // Helper methods below //
            //////////////////////////

    /**
     * Check if account name is valid
     *      String not empty
     *      String not null
     *      Only 2 or 3 words
     * @param accountHolder String to check requirements
     * @return Boolean stating whether string meets requirements
     */
    static boolean checkValidAccountHolderName(String accountHolder) {
        if (accountHolder == null)
            return false;
        if (accountHolder.isEmpty())
            return false;

        String trim = accountHolder.trim();
        int wordCount = trim.split("\\s+").length;
        return wordCount > 1 && wordCount < 4;
    }

    /**
     * Generate random unique 9-digit int which starts with 4 and is an even number
     * @return Randomly generated number
     */
    private static int generateAccountNumber() {
        int randomAccountNumber = 400000000 + new Random().nextInt(49999999) * 2;
        if (!accounts.containsKey(randomAccountNumber))
            return randomAccountNumber;
        else
            return generateAccountNumber();
    }

    /**
     * Returns true if given accountNumber is unique
     * @param accountNumber Account.accountNumber to search for
     * @return boolean stating whether Account.accountNumber exists or not
     */
    private static boolean checkIfAccountNumberUnique(int accountNumber) {
        return accounts.containsKey(accountNumber);
    }

    /**
     * Return current class name as String
     * @return Class name as String
     */
    private String getClassName() {
        return this.getClass().getName();
    }

    /**
     * Returns which account number(s) is non-existing as a fancy String
     * @param sourceAccountNumber accountNumber which sends
     * @param destinationAccountNumber accountNumber which receives
     * @return The generated String stating which Account.accountNumber !exist
     */
    private String getNonExistentAccountNumbersAsString(int sourceAccountNumber, int destinationAccountNumber) {
        StringBuilder output = new StringBuilder("Account number");
        if (!checkIfAccountNumberUnique(sourceAccountNumber) && !checkIfAccountNumberUnique(destinationAccountNumber))
            output.append(String.format("s #%s and #%s", sourceAccountNumber, destinationAccountNumber));
        else if (!checkIfAccountNumberUnique(sourceAccountNumber))
            output.append(String.format(" #%s", sourceAccountNumber));
        else if (!checkIfAccountNumberUnique(destinationAccountNumber))
            output.append(String.format(" #%s", destinationAccountNumber));
        output.append(" does not exist => Transfer not executed!");
        return output.toString();

        /*StringBuilder output = new StringBuilder("Account number");
        if (!accounts.containsKey(sourceAccountNumber) && !accounts.containsKey(destinationAccountNumber))
            output.append(String.format("s #%s and #%s", sourceAccountNumber, destinationAccountNumber));
        else if (!accounts.containsKey(sourceAccountNumber))
            output.append(String.format(" #%s", sourceAccountNumber));
        else if (!accounts.containsKey(destinationAccountNumber))
            output.append(String.format(" #%s", destinationAccountNumber));
        output.append(" does not exist => Transfer not executed!");
        return output.toString();*/

    }

    /**
     * Returns which Account(s) are non-existing as a fancy String
     * @param accountFrom   Account that sends money
     * @param accountTo     Account that receives money
     * @return The generated String stating which are NULL
     */
    private String getNonExistentAccountsAsString(Account accountFrom, Account accountTo) {
        StringBuilder output = new StringBuilder("Account ");
        if (accountFrom == null && accountTo == null)
            output.append("s accountFrom and accountTo");
        else if (accountFrom == null)
            output.append(" accountFrom");
        else if (accountTo == null)
            output.append(" accountTo");
        output.append(" does not exist => Transfer not stored to pending transfers!");
        return output.toString();
    }

    /**
     * Return all Accounts in Bank as array of Objects
     * @return All Accounts in an array or
     */
    private static List<Account> getAllAccounts() {
        List<Account> accountsList = new ArrayList<>();
        for (Map.Entry<Integer, Account> object : accounts.entrySet())
            accountsList.add(object.getValue());
        return accountsList;
    }

    private static boolean executeTransferInHashMap(Account accountFrom, Account accountTo, double amount) {
        for (Map.Entry<Integer, Account> object : accounts.entrySet()) {
            if (object.getValue().equals(accountFrom))
                object.getValue().setAccountBalance(object.getValue().getAccountBalance() - amount);
            else if (object.getValue().equals(accountTo))
                object.getValue().setAccountBalance(object.getValue().getAccountBalance() + amount);
        }

        // TODO: Need to fix this method. both accounts need to get their balance changed. Use deposit and withdraw!
        return false;
    }

    /**
     * Writes given error message as String to Error log file
     *      This method is to be accessed by all classes in package 'bank' wanting to write to errors
     * @param exception Exception to write to file
     */
    static void writeToErrorLogFile(String basicReason, String methodName, String className, Exception exception) {
        StringBuilder output_ = new StringBuilder(String.format("Error\n --> Time of occurrence: %s\n --> %s !\n --> Reason: ", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(Calendar.getInstance().getTime()), basicReason));
        if (exception != null)
            output_.append(String.format("Exception in method %s in %s.java \n --> Thrown exception: %s\n\n", methodName, className, exception));
        else
            output_.append(String.format("%s in method %s in %s.java\n\n", basicReason, methodName, className));

        // 2. parameter for FileWriter will tell errorWriter to append to the File. And if File !exist it will create it
        try (FileWriter fileWriter = new FileWriter(ERROR_FILE_NAME, true);
             BufferedWriter bw = new BufferedWriter(fileWriter)) {

            bw.write(output_.toString());
        } catch (IOException ex) {
            System.err.format("FATAL ERROR!\n --> Time of occurrence: %s\n --> Could not write to \"%s\" !\n --> Reason: IOException in method %s in %s.java \n --> Thrown exception: %s\n\n"
                    , new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(Calendar.getInstance().getTime()), ERROR_FILE_NAME, new Object() {}.getClass().getEnclosingMethod().getName(), "Bank", ex);
        }
    }
}