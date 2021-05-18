package Modul5;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NumConversion extends Application{

    //Lånt fra Helper.java
    public static final String HEXADECIMAL_REGEX = "[0-9A-F]+";
    public static final String INTEGER_REGEX = "([-+]|[-+]?\\d+)";
    public static final String BINARY_REGEX = "[01]+";

    @Override
    public void start(Stage primaryStage){
        VBox vBox = new VBox();
        HBox hBox = new HBox();

        vBox.setSpacing(4);
        vBox.setPadding(new Insets(4,4,4,4));
        hBox.setSpacing(4);

        TextField input = new TextField();


        Button btDecToBin = new Button("Decimal to binary");
        Button btDecToHex = new Button("Decimal to hex");
        Button btBinToDec = new Button("Binary to Decimal");
        Button btHexToDec = new Button("Hex to decimal");

        TextField output = new TextField();
        output.setEditable(false);
        output.setMinWidth(242);

        hBox.getChildren().addAll(btDecToBin, btDecToHex, btBinToDec, btHexToDec);
        vBox.getChildren().addAll(hBox, input, output);

        Scene scene = new Scene(vBox, 490,97);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Number converter");
        primaryStage.show();

        btDecToBin.setOnAction(e -> {
            if (input.getText().matches(INTEGER_REGEX)) {
                int num = Integer.parseInt(input.getText());
                output.setText(dec2Bin(num));
            } else {
                output.setText(input.getText() + " : Invalid integer number!");
            }
        });

        btDecToHex.setOnAction(e -> {
            if (input.getText().matches(INTEGER_REGEX)) {
                int num = Integer.parseInt(input.getText());
                output.setText(dec2Hex(num));
            } else {
                output.setText(input.getText() + " : Invalid integer number!");
            }
        });

        btBinToDec.setOnAction(e -> {
            if (input.getText().matches(BINARY_REGEX)) {
                output.setText(String.format("%d", bin2Dec(input.getText())));
            } else {
                output.setText(input.getText() + " : Invalid binary number!");
            }
        });

        btHexToDec.setOnAction(e -> {
            if (input.getText().matches(HEXADECIMAL_REGEX)) {
                output.setText(String.format("%d", hex2Dec(input.getText())));
            } else {
                output.setText(input.getText() + " : Invalid hexadecimal number!");
            }
        });

    }

    private String dec2Bin(int value){
        return value<=0 ? "" : dec2Bin(value/2) + value%2;
    }
    private String dec2Hex(int value){
        return value<=0 ? "" : dec2Hex(value/16) + "0123456789ABCDEF".charAt(value%16);
    }

    //Hjelpemetode
    private int bin2Dec(String string){
        return bin2Dec(string, 0, string.length() - 1);
    }
    //Hovedmetode
    private int bin2Dec(String string, int low, int high){
        return high < low ? 0 : bin2Dec(string, low, high - 1) * 2 + (string.charAt(high) - '0');
    }

    //Hjelpemetode
    private int hex2Dec(String string) {
        return hex2Dec(string, 0, string.length() - 1);
    }
    //Hovedmetode
    private int hex2Dec (String string,int low, int high){
        return high<low ? 0 : hex2Dec(string, low, high-1) * 16 + "0123456789ABCDEF".indexOf(string.charAt(high));
    }

    public static void main(String[] args){
        launch(args);
    }
}
