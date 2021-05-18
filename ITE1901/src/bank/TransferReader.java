package bank;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TransferReader {
    private BufferedReader transferReader;
    // private BufferedWriter errorWriter;

    /**
     * Read all Transfer Objects from the transfer file
     *
     * @return All transfers in the file as an ArrayList containing transfers
     */
    public List<Transfer> readTransfers() {
        List<Transfer> pendingTransfers = new ArrayList<>();

        // With some help from: https://www.tutorialkart.com/java/read-contents-file-line-line-using-bufferedreader-java/
        String line = "";
        try {
            transferReader = new BufferedReader(new FileReader(Bank.TRANSFER_FILE_NAME));

            // Read all lines in TRANSFER_FILE_NAME
            while ((line = transferReader.readLine()) != null) {
                // Fetch all Accounts
                int accountFrom = Integer.parseInt(line.split(",")[0]);
                int accountTo = Integer.parseInt(line.split(",")[1]);
                double amount = Double.parseDouble(line.split(",")[2]);

                // TODO: Maybe compress next 4 lines into 1 line?
                //String date = line.split(",")[3];
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                calendar.setTime(simpleDateFormat.parse(line.split(",")[3]));

                // Create new Transfer object with its 3rd. constructor and add it to ArrayList 'pendingTransfers'
                pendingTransfers.add(new Transfer(Bank.getAccountByID(accountFrom), Bank.getAccountByID(accountTo), amount, calendar));
            }
        } catch (IOException | ParseException ex) {
            Bank.writeToErrorLogFile(String.format("Could not read from \"%s\"", Bank.TRANSFER_FILE_NAME), new Object() {}.getClass().getEnclosingMethod().getName(), this.getClass().getName(), ex);
        }
        return pendingTransfers;
    }
}