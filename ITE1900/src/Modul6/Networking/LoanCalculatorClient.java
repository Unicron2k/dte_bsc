package Modul6.Networking;

import Helper.Helper;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class LoanCalculatorClient extends Application {
    private TextField tfAnnualInterestRate;
    private TextField tfNumOfYears;
    private TextField tfLoanAmount;
    private Button btSubmit;
    private TextArea ta;
    private DataOutputStream osToServer = null;
    private DataInputStream isFromServer = null;

    @Override
    public void start(Stage primaryStage){
        setUpAndDisplayPrimaryStage(primaryStage);
        connectToServer();

        btSubmit.setOnAction(e-> contactServerForResults());
    }

    private GridPane getGridPane(){
        GridPane pane = new GridPane();

        initializeTextFields();

        btSubmit = new Button("Submit");
        ta = new TextArea();

        Label annIntRate = new Label("Annual Interest Rate:");
        Label numYears = new Label("Number of Years:");
        Label lnAmount = new Label("Loan Amount:");

        GridPane.setConstraints(annIntRate,0,0);
        GridPane.setConstraints(tfAnnualInterestRate, 1,0);

        GridPane.setConstraints(numYears,0,1);
        GridPane.setConstraints(tfNumOfYears,1,1);
        GridPane.setConstraints(btSubmit,2,1);

        GridPane.setConstraints(lnAmount,0,2);
        GridPane.setConstraints(tfLoanAmount, 1,2);

        GridPane.setConstraints(ta, 0,3, 3,1);

        pane.setHgap(4);
        pane.setVgap(4);
        pane.setStyle("-fx-padding: 4;");

        pane.getChildren().addAll(annIntRate, tfAnnualInterestRate, numYears, tfNumOfYears, btSubmit, lnAmount, tfLoanAmount, ta);

        return pane;
    }

    private void initializeTextFields(){
        tfAnnualInterestRate = new TextField();
        Helper.restrictTextFieldInput(tfAnnualInterestRate, Helper.POSITIVE_DECIMAL_REGEX);

        tfNumOfYears = new TextField();
        Helper.restrictTextFieldInput(tfNumOfYears, Helper.POSITIVE_INTEGER_REGEX);

        tfLoanAmount = new TextField();
        Helper.restrictTextFieldInput(tfLoanAmount, Helper.POSITIVE_DECIMAL_REGEX);
    }

    private void setUpAndDisplayPrimaryStage(Stage stage){

        Scene scene = new Scene(getGridPane(), 392, 200);
        stage.setScene(scene);
        stage.setTitle("LoanCalculator, Client");
        stage.setResizable(true);
        stage.show();
    }

    private void connectToServer(){
        try {
            Socket socket = new Socket("localhost", 8000);
            isFromServer = new DataInputStream(socket.getInputStream());
            osToServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException ex){
            writeToLog(ex.toString());
        }
    }

    private void contactServerForResults(){
        try {
            double annIntRate = Double.parseDouble(tfAnnualInterestRate.getText().trim());
            int numYears = Integer.parseInt(tfNumOfYears.getText().trim());
            double loanAmount = Double.parseDouble(tfLoanAmount.getText().trim());

            osToServer.writeDouble(annIntRate);
            osToServer.write(numYears);
            osToServer.writeDouble(loanAmount);

            writeToLog("Annual Interest Rate: " + annIntRate);
            writeToLog("Number of Years: " + numYears);
            writeToLog("Loan Amount: " + loanAmount);

            writeToLog("Monthly Payment: " + isFromServer.readDouble());
            writeToLog("Total Payment: " + isFromServer.readDouble());
            writeToLog("\n");
        }
        catch (IOException ex){ writeToLog(ex.toString()); }
        catch (NumberFormatException ex){ writeToLog("Invalid data entered!"); }
        catch (java.lang.NullPointerException ex){ writeToLog(ex.toString() + " (are you connected to the server?)"); }
    }

    private void writeToLog(String str){
        Platform.runLater(()->ta.appendText(str + "\n"));
    }

    public static void main(String[] args){
        launch(args);
    }
}
