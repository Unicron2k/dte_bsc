package Modul4;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.util.Duration;

public class Pendulum extends Application {
    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        PendulumPane pane = new PendulumPane();  // du må lage klassen PendulumPane

        Scene scene = new Scene(pane, 300, 300);
        primaryStage.setTitle("Pendulum"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        Timeline animation = new Timeline(new KeyFrame(Duration.millis(100), e -> pane.next()));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play(); // Start animation

        pane.setOnMouseClicked(e -> {
            if(animation.getStatus().equals(Animation.Status.PAUSED)) {
                animation.play();
            } else {
                animation.pause();
            }
        });

        pane.requestFocus();
        pane.setOnKeyPressed(e -> {
            double rate=animation.getRate();

            if (e.getCode().equals(KeyCode.UP)) {
                if(rate<1){
                    rate+=0.1;
                } else {
                    rate++;
                }
                if(rate>10){
                    rate=10;
                }
            }
            if (e.getCode().equals(KeyCode.DOWN)) {
                if(rate>1){
                    rate-=1;
                } else {
                    rate-=0.1;
                }
                if(rate<0.1){
                    rate=0.1;
                }
            }

            animation.setRate(rate);
        });
    }
}

class PendulumPane extends Pane {

    private int pendulumRadius = 250, angle=120, angleDir=1;

    private Line line = new Line();
    private Circle anchor = new Circle(5);
    private Circle ball = new Circle(15);

    public PendulumPane() {
        anchor.centerXProperty().bind(this.widthProperty().divide(2));
        anchor.setCenterY(30);

        ball.setCenterX(anchor.getCenterX()+pendulumRadius*Math.cos(Math.toRadians(angle)));
        ball.setCenterY(anchor.getCenterY()+pendulumRadius+pendulumRadius*Math.sin(Math.toRadians(angle)));

        line.startXProperty().bind(anchor.centerXProperty());
        line.startYProperty().bind(anchor.centerYProperty());
        line.endXProperty().bind(ball.centerXProperty());
        line.endYProperty().bind(ball.centerYProperty());

        line.setStrokeWidth(3);

        getChildren().addAll(line, anchor, ball);
        next();
    }

    public void next() {
        if(angle > 120 || angle < 60){
            angleDir*=-1;
        }
        if(angleDir==-1){
            angle++;
        } else if(angleDir==1) {
            angle--;
        }
        ball.setCenterX(anchor.getCenterX()+pendulumRadius*Math.cos(Math.toRadians(angle)));
        ball.setCenterY(anchor.getCenterY()+pendulumRadius*Math.sin(Math.toRadians(angle)));
    }
}