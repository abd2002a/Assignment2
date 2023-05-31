/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller.Admin;

import Model.Account;
import Model.DB;
import Model.User;
import View.ViewManager;
import com.mysql.cj.Query;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import static jdk.nashorn.internal.runtime.Debug.id;

/**
 * FXML Controller class
 *
 * @author PC
 */
public class CreateAccountController implements Initializable {

    @FXML
    private Button usersManagmentPageBtn;
    @FXML
    private Button accountsPageBtn;
    @FXML
    private Button operationsPageBtn;
    @FXML
    private Button saveNewAccountBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private TextField accountNumberTF;
    @FXML
    private TextField usernameTF;
    @FXML
    private TextField currencyTF;
    @FXML
    private TextField balanceTF;
    @FXML
    private DatePicker datePicker;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void showUsersManagmentPage(ActionEvent event) {
        ViewManager.adminPage.changeSceneToUsersManagment();

    }

    @FXML
    private void showAccountsPage(ActionEvent event) {
        ViewManager.adminPage.changeSceneToAccountsManagment();
    }

    @FXML
    private void showOperationsPage(ActionEvent event) {

    }

    @FXML
    private void saveNewAccount(ActionEvent event) throws SQLException {
        int accountNumber = Integer.parseInt(accountNumberTF.getText());
        String username = usernameTF.getText();
        String currency = currencyTF.getText();
        double balance = Double.parseDouble(balanceTF.getText());
        String creationDate = datePicker.getValue().toString();
        int user_id = 0;
// getUserId
        Connection c = DB.getInstance().getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        ps = c.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            if (rs.getString(2).equals(username)) {
                user_id = rs.getInt(1);
            } 
        }
        // getUserId//
        if(user_id != 0){
        Account account = new Account(user_id, accountNumber, username, currency, balance, creationDate);
        account.save();    
        ViewManager.adminPage.changeSceneToAccountsManagment();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Account Inserted");
        alert.setContentText("Account Inserted");
        alert.showAndWait();

        }
        
       
    }

    @FXML
    private void cancelAccountCreation(ActionEvent event) {
        ViewManager.adminPage.changeSceneToAccountsManagment();
    }

}
