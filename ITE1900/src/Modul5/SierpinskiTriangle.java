package Modul5;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;


public class SierpinskiTriangle extends Application {

    @Override
    public void start(Stage primaryStage) {

        Pane pane = getPane();

        Scene scene = new Scene(pane, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sierpinski Triangle");
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    private Pane getPane() {

        TextField tfOrder = new TextField();
        Label lbl = new Label("Enter an order: ");
        Group triangles = new Group();

        HBox input = new HBox();
        HBox output = new HBox();
        VBox vbox = new VBox();

        tfOrder.setMaxWidth(60);

        input.setSpacing(8);
        input.setAlignment(Pos.CENTER);

        output.setMinSize(640, 430);
        output.setMaxSize(640, 430);
        output.setAlignment(Pos.CENTER);

        vbox.setSpacing(8);

        input.getChildren().addAll(lbl, tfOrder);
        output.getChildren().add(triangles);
        vbox.getChildren().addAll(output, input);

        tfOrder.setOnKeyPressed(e -> {
            if(e.getCode()== KeyCode.ENTER){
                if(tfOrder.getText().matches("([+]|[+]?\\d+)")) {
                    triangles.getChildren().clear();

                    Point2D p1 = new Point2D(output.getWidth()/2, 0);
                    Point2D p2 = new Point2D(0, output.getHeight());
                    Point2D p3 = new Point2D(output.getWidth(), output.getHeight());
                    sierpinski(triangles, output, Integer.parseInt(tfOrder.getText()), p1, p2, p3);
                }

            }
        });

        return vbox;
    }


    //Helpfull link:
    //https://stackoverflow.com/questions/36109551/sierpinski-triangle
    //This helped me understand 1 method of how to draw a sierpinski triangle:
    //Take base-shape, subdivide, reduce level by 1, repeat until level meets the base-case
    public static void sierpinski(Group triangles, HBox box, int levels, Point2D p1, Point2D p2, Point2D p3) {
        //Shouldn't it really be 1? so at level 1, we have 1 triangle, the "Base shape"?
        if (levels <=0) {

            Polygon tri = new Polygon();
            tri.getPoints().addAll(
                    new Double[]{
                    p1.getX(), p1.getY(),
                    p2.getX(), p3.getY(),
                    p3.getX(), p3.getY()});

            tri.setFill(Color.RED);
            tri.setStyle("-fx-stroke: black");
            triangles.getChildren().add(tri);
        } else {

            //finds middle points of current triangle, effectively subdividing it into 3(4?) triangles
            Point2D p12 = centrePoint(p1, p2);
            Point2D p23 = centrePoint(p2, p3);
            Point2D p31 = centrePoint(p3, p1);

            //The order of these recursive calls will determine which triangle is drawn first,
            //and further subdivide the triangles.
            sierpinski(triangles, box, levels - 1, p1, p12, p31);
            sierpinski(triangles, box, levels - 1, p12, p2, p23);
            sierpinski(triangles, box,levels - 1, p31, p23, p3);
        }
    }

    private static Point2D centrePoint(Point2D p1, Point2D p2) {
        return new Point2D((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
    }

    public static void main(String[] args){
        launch(args);
    }
}




