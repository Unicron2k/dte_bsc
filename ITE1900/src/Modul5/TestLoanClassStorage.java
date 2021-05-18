package Modul5;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;

public class TestLoanClassStorage extends Application {


    //Lånt fra Helper.java
    public static final String POSITIVE_DECIMAL_REGEX = "([+]|[+]?\\d+(\\.|\\.\\d+)?)$";
    public static final String POSITIVE_INTEGER_REGEX = "([+]|[+]?\\d+)";

    @Override
    public void start(Stage primaryStage) {

        Pane pane = getPane();

        Scene scene = new Scene(pane, 533, 385);
        primaryStage.setScene(scene);
        primaryStage.setTitle("LoanStorage Objects");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Pane getPane() {

        ArrayList<LoanStorage> loanStorageObjects = new ArrayList<>();
        ArrayList<Integer> loansToSave = new ArrayList<>();

        TextField tfAnnIntRate = new TextField();
        TextField tfNumYears = new TextField();
        TextField tfLoanAmmount = new TextField();
        TextField tfLoanNo = new TextField();

        TextArea taOutput = new TextArea(String.format("LoanStorage No.   |    Total Ammount   |    Interest Rate    |    Years   |    Monthly Ammount%n"));

        Button btAddLoan = new Button("Add New LoanStorage");
        Button btSaveAll = new Button("Save All Loans");
        Button btSaveLoan = new Button("Add to List");

        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        VBox vbox = new VBox();

        tfAnnIntRate.setPromptText("Annual Interest Rate");
        tfNumYears.setPromptText("Number of Years");
        tfLoanAmmount.setPromptText("LoanStorage Ammount");
        tfLoanNo.setPromptText("LoanStorage Number to Save");

        btAddLoan.setPrefWidth(116);
        btSaveAll.setPrefWidth(116);
        btSaveLoan.setPrefWidth(100);

        taOutput.setPrefSize(217,300);

        hbox1.setSpacing(8);
        hbox2.setSpacing(8);

        vbox.setSpacing(8);
        vbox.setPadding(new Insets(8, 8, 8, 8));

        hbox1.getChildren().addAll(tfAnnIntRate, tfNumYears, tfLoanAmmount);
        hbox2.getChildren().addAll(btAddLoan, btSaveAll, btSaveLoan, tfLoanNo);

        vbox.getChildren().addAll(hbox1, hbox2, taOutput);

        loadLoans(loanStorageObjects);
        displayLoadedLoans(taOutput, loanStorageObjects);

        btAddLoan.setOnAction(e -> newLoan(tfAnnIntRate, tfNumYears, tfLoanAmmount, taOutput, loanStorageObjects));
        btSaveAll.setOnAction(e -> saveLoans(taOutput, btSaveAll, loanStorageObjects, loansToSave));
        btSaveLoan.setOnAction(e -> saveLoan(tfLoanNo, btSaveAll, loansToSave));

        return vbox;
    }

    //A function of interest
    private void loadLoans(ArrayList<LoanStorage> loanStorageObjects){
        try (ObjectInputStream objInStream = new ObjectInputStream(new FileInputStream("objects.dat"))) {
            while (true) {
                loanStorageObjects.add((LoanStorage) objInStream.readObject());
            }
        } catch (EOFException ex){
            //We'll just write exception-messages to console for now...
            System.out.println("End of File reached, read " + loanStorageObjects.size() + " object" + (loanStorageObjects.size()>1?"s.":"."));
        } catch (IOException ex) {
            System.out.println("Could not open file " + ex.getMessage());
        } catch(ClassNotFoundException ex){
            System.out.println("Could not read object from file: " + ex.getMessage());
        }
    }

    private void displayLoadedLoans(TextArea taOutput, ArrayList<LoanStorage> loanStorageObjects) {
        for (int i = 0; i < loanStorageObjects.size(); i++) {
            //Does not provide the best formatting, but oh well...
            taOutput.appendText(String.format("%15d | %23.2f | %23.2f | %13d | %22.2f%n",
                    i+1, loanStorageObjects.get(i).getLoanAmount(), loanStorageObjects.get(i).getAnnualInterestRate(), loanStorageObjects.get(i).getNumberOfYears(), loanStorageObjects.get(i).getLoanAmount()));
        }
    }

    private void newLoan(TextField tfAnnIntRate, TextField tfNumYears, TextField tfLoanAmmount, TextArea taOutput, ArrayList<LoanStorage> loanStorageObjects){
        double annualInterestRate = 0;
        int numberOfYears = 0;
        double loanAmount = 0;
        LoanStorage tempLoanStorage;


        if(tfAnnIntRate.getText().matches(POSITIVE_DECIMAL_REGEX)){
            annualInterestRate = Double.parseDouble(tfAnnIntRate.getText());
        }else{
            tfAnnIntRate.setText("Not a positive number!");
        }
        if(tfNumYears.getText().matches(POSITIVE_INTEGER_REGEX)){
            numberOfYears = Integer.parseInt(tfNumYears.getText());
        }else{
            tfNumYears.setText("Not a positive integer!");
        }
        if(tfLoanAmmount.getText().matches(POSITIVE_DECIMAL_REGEX)){
            loanAmount = Double.parseDouble(tfLoanAmmount.getText());
        }else{
            tfLoanAmmount.setText("Not a positive number!");
        }
        if (annualInterestRate>0 && numberOfYears>0 && loanAmount>0) {
            tempLoanStorage = new LoanStorage(annualInterestRate, numberOfYears, loanAmount);
            loanStorageObjects.add(tempLoanStorage);
            //Does not provide the best formatting, but oh well...
            taOutput.appendText(String.format("%15d | %23.2f | %23.2f | %13d | %22.2f%n",
                    loanStorageObjects.size(), tempLoanStorage.getLoanAmount(), tempLoanStorage.getAnnualInterestRate(), tempLoanStorage.getNumberOfYears(), tempLoanStorage.getLoanAmount()));
        }
    }

    private void saveLoan(TextField tfLoanNo, Button btSaveAll, ArrayList<Integer> loansToSave){
        if(tfLoanNo.getText().matches(POSITIVE_INTEGER_REGEX)) {
            int numLoan = Integer.parseInt(tfLoanNo.getText())-1;
            if(numLoan>-1) {
                loansToSave.add(numLoan);
                tfLoanNo.clear();
                btSaveAll.setText("Save List");
            } else {
                tfLoanNo.setText("Not a valid loan!");
            }
        } else {
            tfLoanNo.setText("Not a positive integer!");
        }
    }

    //Also a function of interest
    private void saveLoans(TextArea taOutput, Button btSaveAll, ArrayList<LoanStorage> loanStorageObjects, ArrayList<Integer> loansToSave) {
        if (!loanStorageObjects.isEmpty()) {
            try (ObjectOutputStream objOutStream = new ObjectOutputStream(new FileOutputStream("objects.dat"))) {
                //If we have a list of loans to save, save them instead
                if(!loansToSave.isEmpty()){
                    taOutput.appendText("Loans saved: ");
                    for(int i : loansToSave){
                        objOutStream.writeObject(loanStorageObjects.get(i));
                        taOutput.appendText(String.format("%d,",i+1));
                    }
                    taOutput.appendText(String.format("%n"));
                    btSaveAll.setText("Save All Loans");
                } else {
                    for (LoanStorage loanStorage : loanStorageObjects) {
                        objOutStream.writeObject(loanStorage);
                    }
                    taOutput.appendText(String.format("Loans saved!%n"));
                }
            } catch (IOException ex) {
                //We'll just write exception-messages to console for now...
                System.out.println("Could not open file " + ex.getMessage());
            }
        } else {
            taOutput.appendText(String.format("No loans to save...%n"));
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}

class LoanStorage implements Serializable{
    private double annualInterestRate;
    private int numberOfYears;
    private double loanAmount;
    private java.util.Date loanDate;
    /** Default constructor */
    public LoanStorage() {
        this(2.5, 1, 1000);
    }
    /** Construct a loan with specified annual interest rate,
     number of years and loan amount
     */
    public LoanStorage(double annualInterestRate, int numberOfYears,
                       double loanAmount) {
        this.annualInterestRate = annualInterestRate;
        this.numberOfYears = numberOfYears;
        this.loanAmount = loanAmount;
        loanDate = new java.util.Date();
    }
    /** Return annualInterestRate */
    public double getAnnualInterestRate() {
        return annualInterestRate;
    }
    /** Set a new annualInterestRate */
    public void setAnnualInterestRate(double annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }
    /** Return numberOfYears */
    public int getNumberOfYears() {
        return numberOfYears;
    }
    /** Set a new numberOfYears */
    public void setNumberOfYears(int numberOfYears) {
        this.numberOfYears = numberOfYears;
    }
    /** Return loanAmount */
    public double getLoanAmount() {
        return loanAmount;
    }
    /** Set a newloanAmount */
    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }
    /** Find monthly payment */
    public double getMonthlyPayment() {
        double monthlyInterestRate = annualInterestRate / 1200;
        double monthlyPayment = loanAmount * monthlyInterestRate / (1 -
                (Math.pow(1 / (1 + monthlyInterestRate), numberOfYears * 12)));
        return monthlyPayment;
    }
    /** Find total payment */
    public double getTotalPayment() {
        double totalPayment = getMonthlyPayment() * numberOfYears * 12;
        return totalPayment;
    }
    /** Return loan date */
    public java.util.Date getLoanDate() {
        return loanDate;
    }
}