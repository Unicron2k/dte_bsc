package bank;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AppController implements Initializable {
  // Here we connect all GUI elements that have ID with controller
  // Notice how variable names are **EXACTLY** the same as element IDs!
  @FXML
  private TabPane tab_pane_main;

  @FXML
  private Tab tab_accounts;
  @FXML
  private TableView<Account> acc_tv_account_list; // Make table view for showing Account objects
  @FXML
  private TableColumn<Account, Integer> acc_tc_account_id; // Define type of column elements for table view
  @FXML
  private TableColumn<Account, String> acc_tc_account_name;
  @FXML
  private TableColumn<Account, Double> acc_tc_account_balance;
  @FXML
  private TextField acc_tf_first_name;
  @FXML
  private TextField acc_tf_middle_name;
  @FXML
  private TextField acc_tf_last_name;
  @FXML
  private TextField acc_tf_start_balance;
  @FXML
  private Button acc_btn_create_account;

  @FXML
  private Tab tab_add_transfers;
  @FXML
  private TableView<Account> trans_tv_account_from;
  @FXML
  private TableColumn<Account, Integer> trans_tc_from_id;
  @FXML
  private TableColumn<Account, String> trans_tc_from_name;
  @FXML
  private TableColumn<Account, Double> trans_tc_from_balance;
  @FXML
  private TableView<Account> trans_tv_account_to;
  @FXML
  private TableColumn<Account, Integer> trans_tc_to_id;
  @FXML
  private TableColumn<Account, String> trans_tc_to_name;
  @FXML
  private TableColumn<Account, Double> trans_tc_to_balance;
  @FXML
  private TextField trans_tf_transfer_amount;
  @FXML
  private Button trans_btn_transfer_money;

  @FXML
  private Tab tab_execute_transfers;
  @FXML
  private TableView<Transfer> ex_tv_transfer_list;
  @FXML
  private TableColumn<Transfer, Integer> ex_tc_sender_id;
  @FXML
  private TableColumn<Transfer, Integer> ex_tc_receiver_id;
  @FXML
  private TableColumn<Transfer, Double> ex_tc_transfer_amount;
  @FXML
  private Button ex_btn_execute_pending_transfers;

  @FXML
  private Tab tab_error_logs;
  @FXML
  private TextArea err_ta_error_log;
  @FXML
  private Button err_btn_refresh_error_log;

  // An empty public constructor for controller class is **REQUIRED**
  public AppController() {
  }

  // Initialize method is called after all GUI elements have been connected.
  // It can be used to setup some elements with default values or specific event handlers
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    // Following calls connect TableView columns with model class members
    // Notice how they are **EXACTLY** the same as member variables in Account and Transfer!
    acc_tc_account_id.setCellValueFactory(new PropertyValueFactory<>("accountId"));
    acc_tc_account_name.setCellValueFactory(new PropertyValueFactory<>("accountOwner"));
    acc_tc_account_balance.setCellValueFactory(new PropertyValueFactory<>("accountBalance"));
    trans_tc_from_id.setCellValueFactory(new PropertyValueFactory<>("accountId"));
    trans_tc_from_name.setCellValueFactory(new PropertyValueFactory<>("accountOwner"));
    trans_tc_from_balance.setCellValueFactory(new PropertyValueFactory<>("accountBalance"));
    trans_tc_to_id.setCellValueFactory(new PropertyValueFactory<>("accountId"));
    trans_tc_to_name.setCellValueFactory(new PropertyValueFactory<>("accountOwner"));
    trans_tc_to_balance.setCellValueFactory(new PropertyValueFactory<>("accountBalance"));
    ex_tc_sender_id.setCellValueFactory(new PropertyValueFactory<>("senderId"));
    ex_tc_receiver_id.setCellValueFactory(new PropertyValueFactory<>("receiverId"));
    ex_tc_transfer_amount.setCellValueFactory(new PropertyValueFactory<>("transferAmount"));

    // Following code defines a text formatter for number fields, so that they only accept integers and doubles.
    Pattern pattern = Pattern.compile("\\d*|\\d+\\.\\d*");
    TextFormatter<String> formatter1 = new TextFormatter<>(change->pattern.matcher(change.getControlNewText()).matches() ? change : null);
    TextFormatter<String> formatter2 = new TextFormatter<>(change->pattern.matcher(change.getControlNewText()).matches() ? change : null);
    acc_tf_start_balance.setTextFormatter(formatter1);
    trans_tf_transfer_amount.setTextFormatter(formatter2);
  }

  // Example of a button click handler method
  public void createAccountConfirmation() {
    System.out.println("Button Pressed!");

    // Get values from text fields
    Integer id = 400000000 + new Random().nextInt(49999999) * 2;
    String f = acc_tf_first_name.getText().trim();
    String m = acc_tf_middle_name.getText().trim();
    String l = acc_tf_last_name.getText().trim();
    String name = String.format("%s%s %s",f, m.isBlank() || m.isEmpty()? "": " " + m, l);
    Double balance = Math.floor(Double.parseDouble(acc_tf_start_balance.getText()) * 100) / 100;

    // Create a new Account object and add it to the TableView
    acc_tv_account_list.getItems().add(new Account(id, name, balance));

    // Clear text fields
    acc_tf_first_name.clear();
    acc_tf_middle_name.clear();
    acc_tf_last_name.clear();
    acc_tf_start_balance.clear();
  }
}


