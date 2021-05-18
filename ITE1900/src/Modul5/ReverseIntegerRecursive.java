package Modul5;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

//import static Test.Helper.*;

public class ReverseIntegerRecursive extends Application{

    @Override
    public void start(Stage primaryStage){
        VBox vBox = new VBox();
        HBox hBox = new HBox();

        vBox.setSpacing(4);
        vBox.setPadding(new Insets(4,4,4,4));
        hBox.setSpacing(4);

        TextField input = new TextField("Enter a positive integer");
        input.setMinWidth(174);
        //If we are to use helper.java
        //restrictTextFieldInput(input, POSITIVE_INTEGER_REGEX);

        Button btReverse = new Button("Reverse");

        TextField output = new TextField();
        output.setEditable(false);
        output.setMinWidth(242);

        hBox.getChildren().addAll(input, btReverse);
        vBox.getChildren().addAll(hBox, output);

        Scene scene = new Scene(vBox, 254,66);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Reverse integer recursively");
        primaryStage.show();

        btReverse.setOnAction(e ->{
            if(!input.getText().isEmpty()) {
                try {
                    int num = Integer.parseInt(input.getText());

                    output.setText(
                            (num>0)
                                    ? String.format("%d", reverseInteger(num))
                                    : input.getText() + ": Invalid integer!"
                    );
                }
                catch(NumberFormatException ex){
                    output.setText(input.getText() + ": Invalid integer!");
                }
            }
        });
    }

    public int reverseInteger(int value){
        if(value<10) {
            return value;
        }
        else {
            //Due to this using ints, it will fail at a sufficiently high number.
            return (value%10 * (int)Math.pow(10, (int)Math.log10(value))) + reverseInteger(value/10);
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}