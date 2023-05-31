/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Admin;

import View.ViewManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import Model.Account;
import Model.DB;
import Model.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author blal
 */
public class AccountsManagmentController implements Initializable {

    public static Account selectedAccountToUpdate;
    public static Stage updateStage;
    @FXML
    private Button usersManagmentPageBtn;
    @FXML
    private Button accountsPageBtn;
    @FXML
    private Button operationsPageBtn;
    @FXML
    private Button createNewAccountrBtn;
    @FXML
    private Button showAllAccountsBtn;
    @FXML
    private Button updateSelectedAccountBtn;
    @FXML
    private Button deleteSelectedAccountBtn;
    @FXML
    private Button searchAccountBtn;
    @FXML
    private TextField accontSearchTF;
    private TableColumn<Account, Integer> idTC;
    private TableColumn<Account, Integer> accountNumberTC;
    private TableColumn<Account, String> usernameTC;
    private TableColumn<Account, String> currencyTC;
    private TableColumn<Account, Double> balanceTC;
    private TableColumn<Account, String> creationDateTC;
    private TableView<Account> tableView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

//        idTC.setCellValueFactory(new PropertyValueFactory("id"));
//        accountNumberTC.setCellValueFactory(new PropertyValueFactory("account_number"));
//        usernameTC.setCellValueFactory(new PropertyValueFactory("username"));
//        currencyTC.setCellValueFactory(new PropertyValueFactory("currency"));
//        balanceTC.setCellValueFactory(new PropertyValueFactory("balance"));
//        creationDateTC.setCellValueFactory(new PropertyValueFactory("creation_date"));
    }

    @FXML
    private void showUsersManagmentPage(ActionEvent event) {
        ViewManager.adminPage.changeSceneToUsersManagment();

    }

    @FXML
    private void showAccountsPage(ActionEvent event) {
    }

    @FXML
    private void showOperationsPage(ActionEvent event) {
    }

    @FXML
    private void showAccountCreationPage(ActionEvent event) {
        ViewManager.adminPage.changeSceneToCreateAccount();

    }

    @FXML
    private void showAllAccounts(ActionEvent event) throws SQLException {
        ObservableList<Account> accountsList
                = FXCollections.observableArrayList(Account.getAllAccounts());

        tableView.setItems(accountsList);
    }

    @FXML
    private void updateSelectedAccount(ActionEvent event) throws IOException {
        if (tableView.getSelectionModel().getSelectedItem() != null) {

            selectedAccountToUpdate = tableView.getSelectionModel().getSelectedItem();

            FXMLLoader loaderUpdate = new FXMLLoader(getClass().getResource("/View/AdminFXML/UpdateAccountPage.fxml"));
            Parent rootUpdate = loaderUpdate.load();
            Scene updateAccountScene = new Scene(rootUpdate);

            updateStage = new Stage();
            updateStage.setScene(updateAccountScene);
            updateStage.setTitle("Update Account " + selectedAccountToUpdate.getUsername());
            updateStage.show();

        } else {
            Alert warnAlert = new Alert(Alert.AlertType.WARNING);
            warnAlert.setTitle("Select An Account");
            warnAlert.setContentText("Please Select An Account From The Table View");
            warnAlert.show();
        }
    }

    @FXML
    private void deleteSelectedAccount(ActionEvent event) {
        if (tableView.getSelectionModel().getSelectedItem() != null) {

            Account selectedAccount = tableView.getSelectionModel().getSelectedItem();
            Alert deleteConfirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            deleteConfirmAlert.setTitle("Delete Account");
            deleteConfirmAlert.setContentText("Are You Sure You Want To Delete This Account?");
            deleteConfirmAlert.showAndWait().ifPresent(respnse -> {

                try {
                    selectedAccount.delete();
                } catch (SQLException ex) {
                    Logger.getLogger(AccountsManagmentController.class.getName()).log(Level.SEVERE, null, ex);
                }
                Alert deleteSuccessAlert = new Alert(Alert.AlertType.INFORMATION);
                deleteSuccessAlert.setTitle("User Deleted !!!");
                deleteSuccessAlert.setContentText("User Deleted Successfully !!!");
                deleteSuccessAlert.show();

            });
        } else {
            Alert warnAlert = new Alert(Alert.AlertType.WARNING);
            warnAlert.setTitle("Select An Account");
            warnAlert.setContentText("Please Select An Account From The Table View");
            warnAlert.show();

        }
    }

    @FXML
    private void searchForAnAccount(ActionEvent event) throws SQLException {
        Connection c = DB.getInstance().getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts where account_number=?";
        ps = c.prepareStatement(sql);
        ps.setInt(1, Integer.parseInt(accontSearchTF.getText()));
        rs = ps.executeQuery();
        while (rs.next()) {
            Account account = new Account(rs.getInt(2), rs.getInt(3), rs.getString(4),
                    rs.getString(5), rs.getDouble(6), rs.getString(7));
            account.setId(rs.getInt(1));
            accounts.add(account);
        }
        if (ps != null) {
            ps.close();
        }
        c.close();
        ObservableList<Account> accountsByAccountNumber
                = FXCollections.observableArrayList(accounts);

        tableView.setItems(accountsByAccountNumber);

    }

}
