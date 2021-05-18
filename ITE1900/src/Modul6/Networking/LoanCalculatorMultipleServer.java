package Modul6.Networking;

import javafx.application.Application;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.Socket;

public class LoanCalculatorMultipleServer extends Application {

    private TextArea ta;
    private int clientNo;


    @Override
    public void start(Stage primaryStage){}

    private void connectToClient(){}

    private void writeToLog(String str){}

    public static void main(String[] args){
        launch(args);
    }

    class HandleAClient implements Runnable{

        private Socket connectToClient;


        public HandleAClient(Socket socket){}

        @Override
        public void run(){}
    }
}
