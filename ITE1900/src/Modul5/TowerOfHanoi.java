package Modul5;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

public class TowerOfHanoi extends Application {

    @Override
    public void start(Stage primaryStage) {

        Pane pane = getPane();

        Scene scene = new Scene(pane, 560, 360);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tower of Hanoi");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Pane getPane() {

        TextField tfNumMovesIn = new TextField();
        TextArea taOutput = new TextArea();

        tfNumMovesIn.setPromptText("Number of disks");
        tfNumMovesIn.setMinWidth(446);

        taOutput.setEditable(false);
        taOutput.setPrefSize(552, 310);

        Button btFindMoves = new Button("Find moves");

        HBox hbox = new HBox();
        VBox vbox = new VBox();

        hbox.setSpacing(8);
        vbox.setSpacing(8);
        vbox.setPadding(new Insets(8, 8, 8, 8));

        hbox.getChildren().addAll(tfNumMovesIn, btFindMoves);
        vbox.getChildren().addAll(hbox, taOutput);

        btFindMoves.setOnAction(e -> {
            towerOfHanoi(tfNumMovesIn.getText(), taOutput);
        });

        return vbox;
    }

    private void towerOfHanoi(String numDisks, TextArea output){
        int disks=0;
        //Arrays are passed by reference, and is the easiest way to count a recursive function
        int[] numMoves={0};

        if(numDisks.matches("([-+]|[-+]?\\d+)")){
            disks=Integer.parseInt(numDisks);
        } else {
            output.setText("Invalid number of disks!");
            return;
        }
        output.clear();
        output.appendText(String.format("Moves are:%n------------------%n"));
        moveDisks(disks, 'A', 'B', 'C', output, numMoves);
        output.appendText(String.format("------------------%n%nTotal number of moves/Call to the method: %d", numMoves[0]));
    }

    private void moveDisks(int disks, char fromTower, char toTower, char auxTower, TextArea output, int[] numMoves){
        numMoves[0]++;
        if (disks == 1)
            output.appendText(String.format("Move %3d: Move disk %d from %c to %c%n", numMoves[0], disks, fromTower, toTower));
        else {
            moveDisks(disks - 1, fromTower, auxTower, toTower, output, numMoves);
            output.appendText(String.format("Move %3d:  Move disk %d from %c to %c%n", numMoves[0], disks, fromTower, toTower));
            moveDisks(disks - 1, auxTower, toTower, fromTower, output, numMoves);
        }
    }


    public static void main(String[] args){
        launch(args);
    }
}
