package bank;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
    primaryStage.setTitle("UiT Bank INC");
    primaryStage.setScene(new Scene(root, 600, 600));
    primaryStage.show();
  }
}
