package Modul4;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class RotateTextCircle extends Application {

    @Override
    public void start(Stage primaryStage) {

        final int RADIUS = 100;
        int degPerLetter = 0;

        String str = "Rotating text with JavaFX!";
        Pane pane = new Pane();
        Scene scene = new Scene(pane,400, 300);

        primaryStage.setTitle("Rotate text around circle");
        primaryStage.setScene(scene);

        Parameters params = getParameters();
        List<String> args = params.getRaw();
        if(!args.isEmpty()) {
            str = args.get(0);
        }

        Circle circle = new Circle();
        circle.radiusProperty().bind(pane.heightProperty().divide(3));
        circle.setStyle("-fx-fill: green; -fx-stroke-width: 2px; -fx-stroke: black;");
        circle.centerXProperty().bind(pane.widthProperty().divide(2));
        circle.centerYProperty().bind(pane.heightProperty().divide(2));

        pane.getChildren().addAll(circle);

        degPerLetter=360/str.length();
        for(int i=0; i<str.length(); i++){
            Text t = new Text(String.valueOf(str.charAt(i)));
            t.xProperty().bind(circle.centerXProperty().subtract(5));
            t.yProperty().bind(circle.centerYProperty());
            t.yProperty().bind(circle.radiusProperty().divide(2).subtract(2));


            Rotate rot = new Rotate();
            rot.pivotXProperty().bind(circle.centerXProperty());
            rot.pivotYProperty().bind(circle.centerYProperty());
            rot.setAngle(degPerLetter*i);

            t.getTransforms().add(rot);

            pane.getChildren().add(t);
        }

        primaryStage.show();

        Timeline animation = new Timeline(new KeyFrame(Duration.millis(17), e -> {
            for (Node n : pane.getChildren()) {
                if(n instanceof Text){
                    if(n.getTransforms().get(0) instanceof Rotate){
                        ((Rotate) n.getTransforms().get(0)).setAngle(((Rotate)n.getTransforms().get(0)).getAngle()-1);
                    }
                }
            }
        }));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.setRate(0.4);
        animation.play(); // Start animation
    }


    public static void main(String[] args){
        launch(args);
    }
}
