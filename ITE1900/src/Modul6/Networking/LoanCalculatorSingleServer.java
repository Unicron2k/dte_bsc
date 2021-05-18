package Modul6.Networking;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class LoanCalculatorSingleServer extends Application {

    private TextArea ta;

    @Override
    public void start(Stage primaryStage){
        ta = new TextArea();
        Pane pane = new Pane();
        pane.getChildren().addAll(ta);

        Scene scene = new Scene(pane, 548, 177);
        primaryStage.setScene(scene);
        primaryStage.setTitle("LoanCalculator, Server");
        primaryStage.setResizable(true);
        primaryStage.show();

        connectToClient();
    }

    private void connectToClient(){
        new Thread(() -> {
            try{
                ServerSocket serverSocket = new ServerSocket(8000);
                writeToLog("Server started at " + new Date());

                Socket socket = serverSocket.accept();

                DataInputStream fromClient = new DataInputStream(socket.getInputStream());
                DataOutputStream toClient = new DataOutputStream(socket.getOutputStream());
                writeToLog("Connected to client at " + new Date());
                writeToLog("");

                while(true){
                    Loan loan = new Loan(fromClient.readDouble(), fromClient.read(), fromClient.readDouble());
                    writeToLog("Received from client:");
                    writeToLog("Annual Interest Rate: " + loan.getAnnualInterestRate());
                    writeToLog("Number of Years: " + loan.getNumberOfYears());
                    writeToLog("Loan Amount: " + loan.getLoanAmount());
                    writeToLog("");

                    toClient.writeDouble(loan.getMonthlyPayment());
                    toClient.writeDouble(loan.getTotalPayment());
                    writeToLog("Sent to client: ");
                    writeToLog("Monthly Payment: " + loan.getMonthlyPayment());
                    writeToLog("Total Payment: " + loan.getTotalPayment());
                    writeToLog("\n");

                }
            }
            catch(IOException ex){ writeToLog(ex.toString()); }
        }).start();
    }

    private void writeToLog(String str){
        Platform.runLater(()->ta.appendText(str + "\n"));
    }
    public static void main(String[] args){
        launch(args);
    }
}