/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controller.Admin;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import Model.Account;
import Model.DB;
import Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author PC
 */
public class UpdateAccountPageController implements Initializable {

    private Account oldAccount;
    @FXML
    private Button updateAccountBtn;
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

        this.oldAccount = Controller.Admin.AccountsManagmentController.selectedAccountToUpdate;

        accountNumberTF.setText(oldAccount.getAccount_number() + "");
        usernameTF.setText(oldAccount.getUsername());
        currencyTF.setText(oldAccount.getCurrency());
        balanceTF.setText(oldAccount.getBalance() + "");
        datePicker.getEditor().setText(oldAccount.getCreation_date());
    }

    @FXML
    private void updateAccount(ActionEvent event) throws SQLException {
        int accountNumber = Integer.parseInt(accountNumberTF.getText());
        String username = usernameTF.getText();
        String currency = currencyTF.getText();
        double balance = Double.parseDouble(balanceTF.getText());
        String creationDate = "";
        try {

            creationDate = datePicker.getValue().toString();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Select A Date");
            alert.setContentText("Please Select A Date");
            alert.showAndWait();
        }

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
            if (rs.getString(2).equalsIgnoreCase(username)) {
                user_id = rs.getInt(1);
            }
        }

        Account newAccount = new Account(user_id, accountNumber, username, currency, balance, creationDate);
        newAccount.setId(oldAccount.getId());

        newAccount.update();
        Controller.Admin.AccountsManagmentController.updateStage.close();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Account Updated");
        alert.setContentText("Account Updated Successfully !!!");
        alert.showAndWait();

    }

    @FXML
    private void cancelAccountUpdate(ActionEvent event) {
        Controller.Admin.AccountsManagmentController.updateStage.close();

    }

}
