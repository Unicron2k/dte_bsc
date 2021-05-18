
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

/**
 * <h1>ChatServer class.</h1>
 * <br>
 * Class that connects users and let's them communicate with each other
 *
 * @author Daniel Aaron Salwerowicz
 * @version 1.0
 * @since 2018-04-16
 */
public class ChatServer extends Application {
  /**
   * {@link ArrayList} holding {@link ObjectOutputStream} to clients
   */
  private static ArrayList<ObjectOutputStream> outputToUsers = new ArrayList<>();

  /**
   * {@link TextArea} displaying server log
   */
  private TextArea taServer = new TextArea();

  /**
   * Constant variable used to limit number of clients connected to server
   */
  private static final int MAX_USERS = 2;

  /**
   * {@link ServerSocket} used to let clients communicate with each other
   */
  private ServerSocket serverSocket;

  /**
   * Boolean determining whether or not the server is accepting new users
   */
  private boolean acceptingNewUsers;

  /**
   * Counts number of users connected to server
   */
  private int connectedUsers;

  /**
   * Determines port number that server uses
   */
  private static final int port = 1313;

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Start method that sets up background processes and displays primary stage with GUI
   */
  @Override
  public void start(Stage primaryStage) {
    initializePrimaryStage(primaryStage);

    // Starts new thread for accepting new users
    new Thread(() -> {
      try {
        serverSocket = new ServerSocket(port);
        writeToLog("Server started at socket " + port);

        acceptUsers();
      } catch (IOException e) {
        writeToLog('\n' + e.toString());
      }
    }).start();
  }

  /**
   * Sets up primary stage with some design and handles window closing
   *
   * @param primaryStage Stage sent in from {@link ChatServer#start(Stage)} method
   */
  private void initializePrimaryStage(Stage primaryStage) {
    taServer.setWrapText(true);
    taServer.setEditable(false);

    Scene scene = new Scene(taServer, 600, 200);
    primaryStage.setTitle("Chat Server");
    primaryStage.setResizable(true);
    primaryStage.setScene(scene);
    primaryStage.show();

    // Stop program from running in background
    primaryStage.setOnCloseRequest(t -> {
      Platform.exit();
      System.exit(0);
    });
  }

  /**
   * Accepts new users as long as the server doesn't crash or the number of connected users doesn't exceed {@link ChatServer#MAX_USERS}
   */
  private void acceptUsers() {
    writeToLog("Accepting users");
    acceptingNewUsers = true;

    while (true) {
      // If too much users start rejecting them
      if (connectedUsers >= MAX_USERS) {
        refuseNewUsers();
        break;
      }

      try {
        // Wait for new user to connect
        Socket userSocket = serverSocket.accept();
        // Start new thread for new user
        new Thread(new AddUserToChat(userSocket)).start();
      } catch (Exception e) {
        writeToLog('\n' + e.toString());
      }
    }

  }

  /**
   * Refuses new users as long as the server doesn't crash and {@link ChatServer#acceptingNewUsers} is false
   */
  private void refuseNewUsers() {
    writeToLog("Maximum user capacity reached.");
    acceptingNewUsers = false;

    while (!acceptingNewUsers) {
      try {
        // Let user connect
        Socket user = serverSocket.accept();
        ObjectOutputStream out = new ObjectOutputStream(user.getOutputStream());

        // But quickly refuse them connection and drop the socket
        out.writeUTF("Sorry but we've reached the maximum user capacity.\nTry again later.");
        out.flush();

        Thread.sleep(1000);

        out.close();
      } catch (Exception e) {
        writeToLog('\n' + e.toString());
      }
    }
  }

  /**
   * Writes message to all {@link ObjectOutputStream} in {@link ChatServer#outputToUsers}
   *
   * @param message Message to be written to connected users
   */
  private void writeToAll(String message) {
    for (ObjectOutputStream outputStream : outputToUsers) {
      try {
        // Write to user
        outputStream.writeUTF(message);
        // Flush data down the output stream
        outputStream.flush();
      } catch (IOException e) {
        writeToLog('\n' + e.toString());
      }
    }
  }

  /**
   * Uses {@link Platform#runLater(Runnable)} method to update {@link TextArea} with a given message
   *
   * @param message Message to be displayed in TextArea
   */
  private void writeToLog(String message) {
    Platform.runLater(() -> taServer.appendText(String.format("%tF %1$tT: %s%n", new Date(), message)));
  }

  /**
   * <h1>AddUserToChat class.</h1>
   * <br>
   * Class that establishes client connection, reads messages from user and removes them if necessary
   *
   * @author Daniel Aaron Salwerowicz
   * @version 1.0
   * @since 2018-04-16
   */
  private class AddUserToChat implements Runnable {
    private ObjectInputStream fromUser;
    private ObjectOutputStream toUser;
    private String username;
    private Socket userSocket;

    /**
     * Instantiates a new instance of class
     *
     * @param userSocket the user socket
     */
    public AddUserToChat(Socket userSocket) {
      this.userSocket = userSocket;
      connectedUsers++;
    }

    /**
     * Calls method to {@link AddUserToChat#establishUserConnection()}, {@link AddUserToChat#readMessagesFromUser()}, and {@link AddUserToChat#removeUser()} when they disconnect
     */
    @Override
    public void run() {
      try {
        establishUserConnection();
        readMessagesFromUser();
      } catch (Exception e) {
        removeUser();
      }
    }

    /**
     * Connects user to server
     *
     * @throws IOException if {@link ObjectInputStream#readUTF()} encounters an error
     */
    private void establishUserConnection() throws IOException {
      // Get input and output streams from socket
      toUser = new ObjectOutputStream(userSocket.getOutputStream());
      fromUser = new ObjectInputStream(userSocket.getInputStream());

      // Read and save username and save OOS to user in outputToUsers in ChatServer class
      username = fromUser.readUTF();
      outputToUsers.add(toUser);

      writeToLog(username + " joined the chat.");
      writeToAll(username + " joined the chat.");
    }

    /**
     * Removes user from server
     */
    private void removeUser() {
      // Decrease user counter and remove OOS to user
      connectedUsers--;
      outputToUsers.remove(toUser);

      writeToLog(username + " left the chat.");
      writeToAll(username + " left the chat.");

      // If server doesn't accept new users, start accepting them once again
      if (!acceptingNewUsers) acceptUsers();
    }

    /**
     * Continually read messages from user
     *
     * @throws IOException if {@link ObjectInputStream#readUTF()} encounters an error
     */
    private void readMessagesFromUser() throws IOException {
      while (true)
        writeToAll(String.format("%s wrote: %s", username, fromUser.readUTF()));
    }
  }
}