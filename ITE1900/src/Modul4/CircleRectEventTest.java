package Modul4;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class CircleRectEventTest extends Application {

    @Override
    public void start(Stage primaryStage){

        final int RECT_SIZE=30;
        final int CIR_SIZE=15;

        BorderPane bPane = new BorderPane();

        VBox vbTop=new VBox();
        VBox vbRight=new VBox();
        HBox hbBottom=new HBox();
        VBox vbLeft=new VBox();
        Pane spCenter = new Pane();

        Rectangle rect = new Rectangle(RECT_SIZE, RECT_SIZE);
        Circle cir = new Circle(CIR_SIZE);

        Button btnRed = new Button("Rød");
        Button btnBlue = new Button("Blå");
        Button btnRotate = new Button("Roter");
        Button btnFirkant = new Button("Firkant");
        Button btnSirkel = new Button("Sirkel");

        Label rectLabel = new Label("Ingen figur");
        Label cirLabel = new Label("");

        rect.setStyle("-fx-fill: red;");
        cir.setStyle("-fx-fill: blue;");

        btnBlue.setOnAction(e -> {
            int numElements = spCenter.getChildren().size();
            if(numElements>=1) {
                (spCenter.getChildren().get(numElements - 1)).setStyle("-fx-fill: blue;");
            }
        });

        btnRed.setOnAction(e -> {
            int numElements = spCenter.getChildren().size();
            if(numElements>=1) {
                (spCenter.getChildren().get(numElements - 1)).setStyle("-fx-fill: Red;");
            }
        });
        String.format("something %f.2", someNumber);

        btnRotate.setOnAction(e -> {
            rect.setRotate(rect.getRotate()+45);
        });

        btnFirkant.setOnAction(e -> {
            spCenter.getChildren().remove(rect);
            rect.setX((int)(7+Math.random()*(spCenter.getWidth()-RECT_SIZE-14)));
            rect.setY((int)(7+Math.random()*(spCenter.getHeight()-RECT_SIZE-14)));
            rectLabel.setText("Firkant, X: " + rect.getX() + " Y: " + rect.getY());

            spCenter.getChildren().add(rect);
        });

        btnSirkel.setOnAction(e -> {
            spCenter.getChildren().remove(cir);
            cir.setCenterX((int)(CIR_SIZE+Math.random()*(spCenter.getWidth()-2*CIR_SIZE)));
            cir.setCenterY((int)(CIR_SIZE+Math.random()*(spCenter.getHeight()-2*CIR_SIZE)));
            cirLabel.setText("Sirkel, X: " + cir.getCenterX() + " Y: " + cir.getCenterY());

            spCenter.getChildren().add(cir);
        });

        rect.setOnMouseClicked(e ->{
            if(spCenter.getChildren().get(0) instanceof Rectangle){
                spCenter.getChildren().get(0).toFront();
            }
        });

        cir.setOnMouseClicked(e ->{
            if(spCenter.getChildren().get(0) instanceof Circle){
                spCenter.getChildren().get(0).toFront();
            }
        });

        //Crate frame around Center
        spCenter.setStyle("-fx-border-color: black;");

        //Create stuff on top
        vbTop.setAlignment(Pos.CENTER);
        vbTop.getChildren().addAll(rectLabel, cirLabel);

        //Create stuff on left side
        vbLeft.setAlignment(Pos.CENTER);
        vbLeft.getChildren().addAll(btnRed, btnBlue);

        //Create stuff on right side
        vbRight.setAlignment(Pos.CENTER);
        vbRight.getChildren().add(btnRotate);

        //create stuff on the bottom
        hbBottom.setAlignment(Pos.CENTER);
        hbBottom.getChildren().addAll(btnFirkant, btnSirkel);


        bPane.setTop(vbTop);
        bPane.setRight(vbRight);
        bPane.setBottom(hbBottom);
        bPane.setLeft(vbLeft);
        bPane.setCenter(spCenter);

        Scene scene = new Scene(bPane, 500,500);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
