import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class FileIntegrityCheck extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        TextArea taOutput = new TextArea();
        taOutput.setMinHeight(218);
        taOutput.setMaxWidth(528);

        Pane generateHashesPane = generateHashesPane(taOutput);
        Pane verifyHashesPane = verifyHashesPane(taOutput);
        Pane paneMaster = new Pane();

        Button btGenerateHashfile = new Button("Generate hashfile");
        Button btVerifyFiles = new Button("Verify files");

        VBox vbMaster = new VBox();
        HBox hbMaster = new HBox();


        vbMaster.setSpacing(8);
        vbMaster.setPadding(new Insets(8, 8, 8, 8));

        hbMaster.setSpacing(8);

        hbMaster.getChildren().addAll(btGenerateHashfile, btVerifyFiles);

        vbMaster.getChildren().addAll(hbMaster, generateHashesPane, taOutput);
        paneMaster.getChildren().add(vbMaster);

        //Switches to "generate hashes" pane
        btGenerateHashfile.setOnAction(e -> {
            vbMaster.getChildren().remove(verifyHashesPane);
            vbMaster.getChildren().add(1, generateHashesPane);
            primaryStage.setTitle("File Verifier: Generate hashes");

        });

        //Switches to "verify hashes" pane
        btVerifyFiles.setOnAction(e -> {
            vbMaster.getChildren().remove(generateHashesPane);
            vbMaster.getChildren().add(1, verifyHashesPane);
            primaryStage.setTitle("File Verifier: Verify files");

        });

        Scene sMaster = new Scene(paneMaster, 544, 480);
        primaryStage.setScene(sMaster);
        primaryStage.setTitle("File Verifier: Generate Hashes");
        primaryStage.setResizable(true);
        primaryStage.show();

    }

    /**
     * Helper-function. Creates and setups the pane for generating hashes
     * @param taOutput Textarea to print user-feedback to
     * @return Returns a pane-object with UI-elements and corresponding actions relevant for generating hashes
     */
    private Pane generateHashesPane(TextArea taOutput) {

        final FileChooser fileChooser = new FileChooser();
        final DirectoryChooser directoryChooser = new DirectoryChooser();

        TextField tfKeyStoreIn = new TextField();
        TextField tfDirToHashIn = new TextField();
        TextField tfKeyStoreAlias = new TextField();
        TextField tfHashFileOut = new TextField();

        PasswordField pfKeyStorePWD = new PasswordField();

        tfKeyStoreIn.setPromptText("Keystore location");
        tfKeyStoreIn.setMinWidth(468);
        tfDirToHashIn.setPromptText("File/Directory to hash");
        tfDirToHashIn.setMinWidth(468);
        pfKeyStorePWD.setPromptText("Keystore password");
        pfKeyStorePWD.setMinWidth(468);
        tfKeyStoreAlias.setPromptText("Keystore alias");
        tfKeyStoreAlias.setMinWidth(468);
        tfHashFileOut.setPromptText("Hash-file output");
        tfHashFileOut.setMinWidth(468);


        Button btKeyStoreInOpen = new Button("Open");
        Button btDirToHashInOpen = new Button("Open");
        Button btHashFileOutOpen = new Button("Open");
        Button btGenerateHashes = new Button("Generate");

        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        HBox hbox3 = new HBox();
        HBox hbox4 = new HBox();
        HBox hbox5 = new HBox();
        HBox hbox6 = new HBox();

        VBox vbox = new VBox();

        hbox1.setSpacing(8);
        hbox2.setSpacing(8);
        hbox3.setSpacing(8);
        hbox4.setSpacing(8);
        hbox5.setSpacing(8);
        hbox6.setSpacing(8);

        vbox.setSpacing(8);

        hbox1.getChildren().addAll(tfKeyStoreIn, btKeyStoreInOpen);
        hbox2.getChildren().addAll(tfDirToHashIn, btDirToHashInOpen);
        hbox3.getChildren().addAll(pfKeyStorePWD);
        hbox4.getChildren().addAll(tfKeyStoreAlias);
        hbox5.getChildren().addAll(tfHashFileOut, btHashFileOutOpen);
        hbox6.getChildren().addAll(btGenerateHashes);

        vbox.getChildren().addAll(hbox1, hbox2, hbox3, hbox4, hbox5, hbox6);

        // Sets actions for each buttons, refer to corresponding javadoc-comment for each function
        btKeyStoreInOpen.setOnAction(e -> tfKeyStoreIn.setText(openFileDialogue(fileChooser)));
        btDirToHashInOpen.setOnAction(e -> tfDirToHashIn.setText(openDirectoryDialogue(directoryChooser)));
        btHashFileOutOpen.setOnAction(e -> tfHashFileOut.setText(openDirectoryDialogue(directoryChooser)+"hashes.csv"));
        btGenerateHashes.setOnAction(e -> generateHashes(tfKeyStoreIn.getText(), tfDirToHashIn.getText(), pfKeyStorePWD.getText(), tfKeyStoreAlias.getText(), tfHashFileOut.getText(), taOutput));

        return vbox;
    }

    /**
     * Helper-function. Creates and setups the pane for verifying hashes
     * @param taOutput Textarea to print user-feedback to
     * @return Returns a pane-object with UI-elements and corresponding actions relevant for verifying hashes
     */
    private Pane verifyHashesPane(TextArea taOutput) {

        final FileChooser fileChooser = new FileChooser();
        final DirectoryChooser directoryChooser = new DirectoryChooser();

        TextField tfHashFileIn = new TextField();
        TextField tfDirToVerifyIn = new TextField(); //OpenDialogue?
        TextField tfPubKeyIn = new TextField();
        TextField tfSigFileIn = new TextField();

        tfHashFileIn.setPromptText("Hashfile");
        tfHashFileIn.setMinWidth(468);
        tfDirToVerifyIn.setPromptText("File/Directory to verify");
        tfDirToVerifyIn.setMinWidth(468);
        tfPubKeyIn.setPromptText("Public key");
        tfPubKeyIn.setMinWidth(468);
        tfSigFileIn.setPromptText("Signature file");
        tfSigFileIn.setMinWidth(468);

        Button btHashFileInOpen = new Button("Open");
        Button btDirToVerifyInOpen = new Button("Open");
        Button btpubKeyInOpen = new Button("Open");
        Button btSigFileInOpen = new Button("Open");
        Button btVerifyFiles = new Button("Verify");

        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        HBox hbox3 = new HBox();
        HBox hbox4 = new HBox();
        HBox hbox5 = new HBox();

        VBox vbox = new VBox();

        hbox1.setSpacing(8);
        hbox2.setSpacing(8);
        hbox3.setSpacing(8);
        hbox4.setSpacing(8);
        hbox5.setSpacing(8);
        hbox4.setPadding(new Insets(0,0,35,0));

        vbox.setSpacing(8);

        hbox1.getChildren().addAll(tfHashFileIn, btHashFileInOpen);
        hbox2.getChildren().addAll(tfDirToVerifyIn, btDirToVerifyInOpen);
        hbox3.getChildren().addAll(tfPubKeyIn, btpubKeyInOpen);
        hbox4.getChildren().addAll(tfSigFileIn, btSigFileInOpen);
        hbox5.getChildren().addAll(btVerifyFiles);

        vbox.getChildren().addAll(hbox1, hbox2, hbox3, hbox4, hbox5);

        // Sets actions for each buttons, refer to corresponding javadoc-comment for each function
        btHashFileInOpen.setOnAction(e -> tfHashFileIn.setText(openFileDialogue(fileChooser)));
        btDirToVerifyInOpen.setOnAction(e -> tfDirToVerifyIn.setText(openDirectoryDialogue(directoryChooser)));
        btpubKeyInOpen.setOnAction(e -> tfPubKeyIn.setText(openFileDialogue(fileChooser)));
        btSigFileInOpen.setOnAction(e -> tfSigFileIn.setText(openFileDialogue(fileChooser)));
        btVerifyFiles.setOnAction(e -> verifyHashes(tfHashFileIn.getText(), tfDirToVerifyIn.getText(), tfPubKeyIn.getText(), tfSigFileIn.getText(), taOutput));

        return vbox;
    }

    /**
     * Helper-function, lets the user pick a file
     * @param fileChooser File-chooser used to show dialogue. Can be any file-chooser
     * @return String containing the path to the selected file
     */
    private String openFileDialogue(FileChooser fileChooser) {
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            return file.getAbsolutePath();
        }
        return "";
    }

    /**
     * Helper-function, lets the user select where to save hashes.csv
     * @param directoryChooser Directory-chooser used to show dialogue. Can be any directory-chooser
     * @return String containing the path to the selected directory
     */
    private String openDirectoryDialogue(DirectoryChooser directoryChooser) {
        directoryChooser.setTitle("Select where to save hashes.csv");
        File file = directoryChooser.showDialog(null);
        if (file != null) {
            return file.getAbsolutePath() + File.separator;
        }
        return "";
    }

    /**
     * Generates a hash-record containing key-value-pair of filepath-filehash, writes it to a file,
     * creates a signature of the hash-record and writes it to a file, then exports the corresponding public-key
     * @param keyStorePath Path to keystore containing the private-key used to sign the hash-record
     * @param dirToHash Directory to scan and hash
     * @param keyStorePWD Password to the keystore used
     * @param keyStoreAlias Alias/Username for the keystore
     * @param hashFileOutPath Where to save the generated hash-record, signature-file and public-key
     * @param taOutput Text-area to send user-feedback to
     */
    private void generateHashes(String keyStorePath, String dirToHash, String keyStorePWD, String keyStoreAlias, String hashFileOutPath, TextArea taOutput) {
        new Thread(() -> {

            HashMap<String, String> map = new HashMap<>();
            ArrayList<String> list;

            File hashPath = new File(hashFileOutPath);

            // Some checking to ensure correct filename for the hash-record
            if(!hashPath.getName().toLowerCase().equals("hashes.csv")){
                //Not the "correct" file, so lets cosntruct a path to the correct file
                hashPath = new File(hashPath.getAbsolutePath()+File.separator+"hashes.csv");
            } else {
                //Okay, correct file, but let's ensure it has the correct name...
                hashPath = new File(hashPath.getParent()+File.separator+"hashes.csv");
            }

            try {
                // Lets scan the selected directory and make a list of files
                Platform.runLater(()->taOutput.appendText(String.format("Scanning directories, please wait...%n")));
                list = Utils.searchDirectory(new File(dirToHash));
                Platform.runLater(()->taOutput.appendText(String.format("Scanning complete%n")));

                // Iterate through the file-list and hash the files, put the result into a hash-map
                for (String path : list) {
                    Platform.runLater(()->taOutput.appendText(String.format("Hashing \"" + path + "\", please wait....%n")));
                    map.put(path, Utils.getSha256(new File(path)));
                }

                // The we write the hash map to file
                Platform.runLater(()->taOutput.appendText(String.format("Writing hash-record, please wait...%n")));
                Utils.writeHashMapToFile(map, hashPath);
                String finalHashPath = hashPath.getAbsolutePath();
                Platform.runLater(()->taOutput.appendText(String.format("Hash-record successfully written to:   %n\"%s\"%n", finalHashPath)));

            } catch (IOException | NullPointerException ex) {
                Utils.writeErrorLog(String.format("Exception: %s%n", ex.getMessage()));
                Platform.runLater(()->taOutput.appendText(String.format("Exception: %s%n", ex.getMessage())));
            }

            try {
                // Now we create a signature for out hash-map, so it can be verified at a later time
                Platform.runLater(()->taOutput.appendText(String.format("Signing hashes.csv, please wait...%n")));
                if(Utils.sign(hashPath, keyStorePath, keyStorePWD, keyStoreAlias)){
                    Platform.runLater(()->taOutput.appendText(String.format("hashes.csv signed successfully!%n")));
                } else {
                    Platform.runLater(()->taOutput.appendText(String.format("Signing hashes.csv failed!%n")));
                }
            } catch (Exception ex) {
                Utils.writeErrorLog(String.format("Exception: %s%n", ex.getMessage()));
                Platform.runLater(()->taOutput.appendText(String.format("Exception: %s%n", ex.getMessage())));

            }
        }, "GenerateHashes").start();
    }

    /**
     * Verifies the hashes stored in hashes.csv against a scanned file-tree
     * @param pathHashFileIn Hash-record containing key-value pairs of filepaht-filehash
     * @param pathDirToVerify Directory to verify
     * @param pathPubKeyIn Public-key used to verify the hash-record-signature
     * @param pathSigFileIn Signature-file used to verify the hash-record
     * @param taOutput Text-area to send user-feedback to
     */
    private void verifyHashes(String pathHashFileIn, String pathDirToVerify, String pathPubKeyIn, String pathSigFileIn, TextArea taOutput) {
        new Thread(() -> {
            try {

                // Read and verify files
                Platform.runLater(()->taOutput.appendText(String.format("Verifying signature, please wait...%n")));
                //Does signature and hash-map check out?
                if (Utils.verifySignature(new File(pathHashFileIn), new File(pathSigFileIn), new File(pathPubKeyIn))) {
                    Platform.runLater(()->taOutput.appendText(String.format("Signature verified!%nLoading hash-table, please wait...%n")));
                    // Read recorded hash-record
                    HashMap<String, String> map = Utils.readHashMapFromFile(new File(pathHashFileIn));
                    Platform.runLater(()->taOutput.appendText(String.format("Hash-table loaded%nScanning directories, please wait...%n")));
                    // Created a new list of files at selected directory
                    ArrayList<String>list = Utils.searchDirectory(new File(pathDirToVerify));
                    Platform.runLater(()->taOutput.appendText(String.format("Scanning complete%nVerifying files, please wait...%n")));
                    String actualHash = "";
                    double numFailed=0;

                    // Iterate through scanned file-list
                    for (String path : list) {
                        // If the map contains the file
                        if(map.containsKey(path)) {
                            // Hash the file
                            actualHash = Utils.getSha256(new File(path));
                            // and check it against the hash record
                            if (actualHash.equals(map.get(path))) {
                                // If it matches, Continue on to the next or print a verification-message
                                //Platform.runLater(()->taOutput.appendText(String.format("File \"%s\" verified, hash: %s%n", path, map.get(path))));
                                continue;
                            } else {
                                // If not, print an error-message
                                String finalActualHash = actualHash;
                                Platform.runLater(() -> taOutput.appendText(String.format("File \"%s\" failed!%n    Expected: %s%n    Actual:      %s%n", path, map.get(path), finalActualHash)));
                                numFailed++;
                            }
                        } else {
                            // If file isn't in the record, tell the user we have no hash-record for this file
                            Platform.runLater(() -> taOutput.appendText(String.format("No hash-record found for file %s%n", path)));
                        }
                    }

                    // If no files failed, throw a paty!
                    if (numFailed==0){
                        Platform.runLater(()->taOutput.appendText(String.format("All files verified successfully!%n")));
                    } else {
                        // Some files failed, notify the user!
                        double finalNumFailed = numFailed;
                        Platform.runLater(()->taOutput.appendText(String.format("%.0f file%s failed verification!%n", finalNumFailed, finalNumFailed>1?"s":"")));
                    }

                } else {
                    //If signature and hash-record verification fails, tell the user, and don't do anything more. No reason in verifying files against an insecure hash-record
                    Platform.runLater(()->taOutput.appendText(String.format("Signature verification failed! Hash-record have been modified!%n")));
                }
            } catch (Exception ex) {
                Utils.writeErrorLog(String.format("Exception: %s%n", ex.getMessage()));
                Platform.runLater(()->taOutput.appendText(String.format("Exception: %s%n", ex.getMessage())));
            }
        }, "VerifyHashes").start();
    }
}