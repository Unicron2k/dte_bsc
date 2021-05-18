package Modul4;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class ChessBoardBindings extends Application {

    @Override
    public void start(Stage primaryStage){

        final int BOARD_SIZE=800, SQUARE_SIZE=BOARD_SIZE/8;
        Pane pane = new Pane();

        for(int y=0; y<8; y++){
            for(int x=0; x<8; x++) {

                javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle(SQUARE_SIZE * x, SQUARE_SIZE * y, SQUARE_SIZE, SQUARE_SIZE);

                rect.heightProperty().bind(pane.heightProperty().divide(8));
                rect.widthProperty().bind(pane.widthProperty().divide(8));
                rect.xProperty().bind(pane.widthProperty().divide(8).multiply(x));
                rect.yProperty().bind(pane.heightProperty().divide(8).multiply(y));

                if(x%2==0) {
                    if (y % 2 == 0){
                        rect.setFill(javafx.scene.paint.Color.WHITE);
                    }else {
                        rect.setFill(javafx.scene.paint.Color.BLACK);
                    }
                }else{
                    if (y % 2 == 0) {
                        rect.setFill(javafx.scene.paint.Color.BLACK);
                    }else {
                        rect.setFill(javafx.scene.paint.Color.WHITE);
                    }
                }
                pane.getChildren().add(rect);
            }
        }

        Scene scene = new Scene(pane, BOARD_SIZE, BOARD_SIZE);
        primaryStage.setTitle("Chessboard with bindings");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();

    }

    public static void main(String[] args){
        launch(args);
    }
}
