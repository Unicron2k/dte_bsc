package Modul5;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

import java.io.*;

public class EncryptFile extends Application {

    @Override
    public void start(Stage primaryStage) {

        Pane pane = getPane();

        Scene scene = new Scene(pane, 560, 114);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Encrypt/Decrypt file");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Pane getPane() {

        TextField tfFileIn = new TextField();
        TextField tfFileOut = new TextField();
        TextField tfOutput = new TextField();

        tfFileIn.setPromptText("Input file");
        tfFileIn.setMinWidth(468);

        tfFileOut.setPromptText("Output file");
        tfFileOut.setMinWidth(468);

        tfOutput.setEditable(false);

        Button btEncrypt = new Button("Encrypt");
        Button btDecrypt = new Button("Decrypt");

        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        VBox vbox = new VBox();

        hbox1.setSpacing(8);
        hbox2.setSpacing(8);

        vbox.setSpacing(8);
        vbox.setPadding(new Insets(8, 8, 8, 8));

        hbox1.getChildren().addAll(tfFileIn, btEncrypt);
        hbox2.getChildren().addAll(tfFileOut, btDecrypt);

        vbox.getChildren().addAll(hbox1, hbox2, tfOutput);

        btEncrypt.setOnAction(e->crypt(tfFileIn, tfFileOut, tfOutput, false));
        btDecrypt.setOnAction(e->crypt(tfFileIn, tfFileOut, tfOutput, true));

        return vbox;
    }

    public void crypt(TextField tfFileIn, TextField tfFileOut, TextField tfOutput, boolean decrypt){
        try(BufferedInputStream input = new BufferedInputStream(new FileInputStream(tfFileIn.getText()));
                BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(tfFileOut.getText()))){

            int data=0;
            while((data=input.read())!=-1) {
                output.write(decrypt?(data-5):(data+5));
            }

            tfOutput.setText(decrypt?"File decrypted!":"File encrypted!");
        }
        catch (IOException ex){
            tfOutput.setText("Could not open file " + ex.getMessage());
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}
