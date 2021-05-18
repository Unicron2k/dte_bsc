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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class FindWordInFile extends Application {

    @Override
    public void start(Stage primaryStage) {

        Pane pane = getPane();

        Scene scene = new Scene(pane, 700, 360);//640 360
        primaryStage.setScene(scene);
        primaryStage.setTitle("Word search");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Pane getPane() {

        TextField tfFilePathIn = new TextField();
        TextField tfWordIn = new TextField();
        TextArea taOutput = new TextArea();


        tfFilePathIn.setPromptText("Filepath");
        tfWordIn.setPromptText("Word");

        tfFilePathIn.setMinWidth(440);
        tfWordIn.setMinWidth(100);
        taOutput.setPrefSize(684, 310);//624 310

        taOutput.setEditable(false);

        Button btSearch = new Button("Search");

        HBox hbox = new HBox();
        VBox vbox = new VBox();

        hbox.setSpacing(8);
        vbox.setSpacing(8);
        vbox.setPadding(new Insets(8, 8, 8, 8));

        hbox.getChildren().addAll(tfFilePathIn, tfWordIn, btSearch);
        vbox.getChildren().addAll(hbox, taOutput);

        btSearch.setOnAction(e -> {
            File filePath = new File(tfFilePathIn.getText());
            String word = tfWordIn.getText();
            search(filePath, word, taOutput);
        });

        return vbox;
    }

    private void search(File file, String word, TextArea output){

        double[] results= {0, 0, 0};
        if (!file.exists()) {
            output.appendText(String.format("Invalid filepath: %s%n", file.getAbsolutePath()));
        } else if(word.isEmpty()){
            output.appendText("Enter a word to search for.");
        } else {
            output.appendText(String.format("Search start%n-------------------------%n"));
            searchDirectories(file, word, output, results);
            if(results[2]==0) {
                output.appendText(String.format("Could not find \"%s\"", word));
            } else {
                output.appendText(String.format("-------------------------%nSearch end:%nSearched %.0f directories, %.0f files. Found %.0f occurences of \"%s\"%n%n%n",
                        results[0], results[1], results[2], word));
            }
        }
    }

    private void searchDirectories(File file, String word, TextArea output, double[] results){
        if(file.isDirectory()){
            results[0]++;
            try{
                for(File newFile : file.listFiles()){
                    //Recursive call
                    searchDirectories(newFile, word, output, results);
                }
            }
            catch (NullPointerException ex){ output.appendText("NullPointerException: " + ex.getMessage()); }
        } else {
            results[1]++;
            searchFile(file, word, output, results);
        }
    }

    private void searchFile(File file, String word, TextArea output, double[] results){
        try(Scanner input = new Scanner(file)){
            double count=0;
            String line;
            while(input.hasNext()) {
                count++;
                line = input.nextLine();
                if(line.contains(word)){
                    output.appendText(String.format("%s : line %.0f : %s%n" ,file.getAbsolutePath(), count, line));
                    results[2]++;
                }
            }
        }
        catch (FileNotFoundException ex) { output.appendText(String.format("File not found exception: %s%n", ex.getMessage())); }
    }

    public static void main(String[] args){
        launch(args);
    }
}
