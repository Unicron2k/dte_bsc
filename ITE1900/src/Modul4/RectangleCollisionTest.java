package Modul4;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javafx.scene.shape.Rectangle;

public class RectangleCollisionTest extends Application {

    @Override
    public void start(Stage primaryStage) {

        Pane pane = new Pane();
        Scene scene = new Scene(pane,400, 300);

        primaryStage.setTitle("Rectangle collision test");
        primaryStage.setScene(scene);

        Rectangle rectLarge = new Rectangle(40,40);
        Rectangle rectSmall = new Rectangle(30,30);
        Label label = new Label("No Collision.");

        rectLarge.setStyle("-fx-stroke-width: 1px; -fx-stroke: black; -fx-fill: transparent;");
        rectLarge.setX(50);
        rectLarge.setY(50);

        rectSmall.setStyle("-fx-stroke-width: 1px; -fx-stroke: black; -fx-fill: transparent;");
        rectSmall.setX(100);
        rectSmall.setY(100);

        pane.getChildren().addAll(label, rectLarge, rectSmall);

        primaryStage.show();

        pane.requestFocus();
        pane.setOnKeyPressed(e -> {

            switch(e.getCode()){
                case UP:
                    rectLarge.setY(rectLarge.getY()-10);
                    break;
                case DOWN:
                    rectLarge.setY(rectLarge.getY()+10);
                    break;
                case LEFT:
                    rectLarge.setX(rectLarge.getX()-10);
                    break;
                case RIGHT:
                    rectLarge.setX(rectLarge.getX()+10);
                    break;
                case W:
                    rectSmall.setY(rectSmall.getY()-10);
                    break;
                case S:
                    rectSmall.setY(rectSmall.getY()+10);
                    break;
                case A:
                    rectSmall.setX(rectSmall.getX()-10);
                    break;
                case D:
                    rectSmall.setX(rectSmall.getX()+10);
                    break;
                default:
                    break;
            }

            if (contains(rectLarge, rectSmall)){
                label.setText("Large rectangle contains Small rectangle");
            } else if (overlaps(rectLarge, rectSmall)){
                label.setText("Rectangles overlaps");
            } else {
                label.setText("No collision");
            }


        });

    }
    /** Returns true if r1 contains r2 */
    private static boolean contains(Rectangle r1, Rectangle r2) {
    // Four corner points in r2
        double x1 = r2.getX();
        double y1 = r2.getY();
        double x2 = x1 + r2.getWidth();
        double y2 = y1;
        double x3 = x1;
        double y3 = y1 + r2.getHeight();
        double x4 = x1 + r2.getWidth();
        double y4 = y1 + r2.getHeight();

        return r1.contains(x1, y1) && r1.contains(x2, y2) && r1.contains(x3, y3) && r1.contains(x4, y4);
    }

    /** Returns true if r1 overlaps r2 */
    private static boolean overlaps(Rectangle r1, Rectangle r2) {
    // Four corner points in r2
        double r1xCenter = r1.getX() + r1.getWidth() / 2;
        double r2xCenter = r2.getX() + r2.getWidth() / 2;
        double r1yCenter = r1.getY() + r1.getHeight() / 2;
        double r2yCenter = r2.getY() + r2.getHeight() / 2;

        return Math.abs(r1xCenter - r2xCenter) <= (r1.getWidth() + r2.getWidth()) / 2 &&
                Math.abs(r1yCenter - r2yCenter) <= (r1.getHeight() + r2.getHeight()) / 2;
    }

    public static void main(String[] args){
        launch(args);
    }
}
