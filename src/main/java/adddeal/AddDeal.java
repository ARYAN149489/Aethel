package adddeal;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import jdbcc.DatabaseConnection;
import myalert.MyAlert;
import javafx.scene.control.ScrollPane;

public class AddDeal {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCalculate;

    @FXML
    private Button btnClr;

    @FXML
    private Button btnFetchBuyer;

    @FXML
    private Button btnFetchSeller;

    @FXML
    private Button btnSaveDeal;

    @FXML
    private ComboBox<String> comboPropertyName;

    @FXML
    private DatePicker dateAdvGiven;

    @FXML
    private DatePicker dateRegistry;

    @FXML
    private RadioButton radioCompleted;

    @FXML
    private RadioButton radioOngoing;

    @FXML
    private TextField txtAdvAmount;

    @FXML
    private TextField txtAdvCommission;

    @FXML
    private TextField txtAmountLeft;

    @FXML
    private TextField txtBuyerMobile;

    @FXML
    private TextField txtBuyerName;

    @FXML
    private TextField txtCommissionLeft;

    @FXML
    private TextField txtFinalAmount;

    @FXML
    private TextField txtMyCommission;

    @FXML
    private TextField txtOtherInfo;

    @FXML
    private TextField txtSellerMobile;

    @FXML
    private TextField txtSellerName;

    @FXML
    boolean calculateRemaining(ActionEvent event) {
        String finalAmt = txtFinalAmount.getText();
        String myComm = txtMyCommission.getText();
        String advComm = txtAdvCommission.getText();
        String advAmt = txtAdvAmount.getText();
        if(finalAmt.isEmpty() || myComm.isEmpty() || advComm.isEmpty() || advAmt.isEmpty()){
            // System.out.println("Enter fields correctly");
            MyAlert.alertMsg("Please fill Final Amount, My Commission, Advance Commission, and Advance Amount to calculate remaining figures.", Alert.AlertType.WARNING, "Incomplete Deal Calculation Fields", "Input Error");
            return false;
        }
        try{
            Double finalAmount = Double.parseDouble(finalAmt);
            Double myCommission = Double.parseDouble(myComm);
            Double advCommission = Double.parseDouble(advComm);
            Double advAmount = Double.parseDouble(advAmt);

            Double amountLeft = finalAmount - advAmount;
            Double commissionLeft = myCommission - advCommission;

            txtAmountLeft.setText(String.valueOf(amountLeft));
            txtCommissionLeft.setText(String.valueOf(commissionLeft));
            return true;
        } catch (RuntimeException e){
            // System.out.println("Invalid values to calculate remaining amount/commission");
            MyAlert.alertMsg("Please enter valid numeric values for deal amounts and commission.", Alert.AlertType.ERROR, "Invalid Number Format", "Calculation Error");
            return false;
        }
    }

    @FXML
    void clearAll(ActionEvent event) {
        txtBuyerMobile.clear();
        txtBuyerName.clear();
        txtSellerName.clear();
        txtSellerMobile.clear();
        comboPropertyName.getItems().clear();
        txtFinalAmount.clear();
        txtMyCommission.clear();
        txtAdvAmount.clear();
        txtAdvCommission.clear();
        txtAmountLeft.clear();
        txtCommissionLeft.clear();
        dateAdvGiven.setValue(null);
        dateRegistry.setValue(null);
        txtOtherInfo.clear();
        radioOngoing.setSelected(false);
        radioCompleted.setSelected(false);
    }

    @FXML
    void fetchBuyerName(ActionEvent event) {
        try{
            PreparedStatement pst = con.prepareStatement("select name from Customers where mobileNumber = ? AND type IN (?, ?)");
            pst.setString(1, txtBuyerMobile.getText());
            pst.setString(2, "Both");
            pst.setString(3, "Buyer");
            ResultSet res = pst.executeQuery();
            if(res.next()){
                txtBuyerName.setText(res.getString("name"));
            } else{
                // System.out.println("No user found...");
                MyAlert.alertMsg("No registered buyer found matching mobile number: " + txtBuyerMobile.getText(), Alert.AlertType.WARNING, "Buyer Not Found", "Search Result");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            MyAlert.alertMsg("Error fetching buyer: " + e.getMessage(), Alert.AlertType.ERROR, "Search Error", "Database Error");
        }
    }

    @FXML
    void fetchSellerNameAndProperties(ActionEvent event) {
        try{
            comboPropertyName.getItems().clear();
            PreparedStatement pst = con.prepareStatement("select name from Customers where mobileNumber = ? AND type IN (?, ?)");
            pst.setString(1, txtSellerMobile.getText());
            pst.setString(2, "Both");
            pst.setString(3, "Seller");
            ResultSet res = pst.executeQuery();
            if(res.next()){
                txtSellerName.setText(res.getString("name"));
            } else{
                // System.out.println("No user found...");
                MyAlert.alertMsg("No registered seller found matching mobile number: " + txtSellerMobile.getText(), Alert.AlertType.WARNING, "Seller Not Found", "Search Result");
                return;
            }
            pst = con.prepareStatement("select prop_name from Properties where mobileNumber = ?");
            pst.setString(1, txtSellerMobile.getText());
            res = pst.executeQuery();
            if(!res.next()){
                // System.out.println("No Property found...");
                MyAlert.alertMsg("No property listings found for seller mobile number: " + txtSellerMobile.getText(), Alert.AlertType.WARNING, "No Properties Found", "Search Result");
                return;
            }
            while(res.next()){
                comboPropertyName.getItems().add(res.getString("prop_name"));
            }
            // System.out.println("Properties loaded successfully....");
        } catch (SQLException e) {
            e.printStackTrace();
            MyAlert.alertMsg("Error fetching seller details: " + e.getMessage(), Alert.AlertType.ERROR, "Search Error", "Database Error");
        }
    }

    @FXML
    void fillFinalAmount(ActionEvent event) {
        String propName = comboPropertyName.getSelectionModel().getSelectedItem();
        String mobile = txtSellerMobile.getText();
        try{
            PreparedStatement pst = con.prepareStatement("select price_demanded from Properties where mobileNumber = ? AND prop_name = ?");
            pst.setString(1, mobile);
            pst.setString(2, propName);
            ResultSet res = pst.executeQuery();
            if(res.next()){
                txtFinalAmount.setText(res.getString("price_demanded"));
            } else{
                // System.out.println("Unable to fetch amount");
                MyAlert.alertMsg("Unable to fetch price for selected property.", Alert.AlertType.WARNING, "Price Fetch Failed", "Error");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            MyAlert.alertMsg("Error fetching property price: " + e.getMessage(), Alert.AlertType.ERROR, "Database Error", "Error");
        }
    }

    @FXML
    void saveDeal(ActionEvent event) {
        if(radioOngoing.isSelected() == false && radioCompleted.isSelected() == false){
            // System.out.println("Deal status not specified....");
            MyAlert.alertMsg("Please select deal status (Ongoing or Completed).", Alert.AlertType.WARNING, "Status Required", "Validation Error");
            return;
        }

        try{
            PreparedStatement pst = con.prepareStatement(
                    "insert into Deals (buyer_mobile, buyer_name, seller_mobile, seller_name, " +
                            "property_name, final_amount, my_commission, adv_commission, adv_amount, " +
                            "amount_left, commission_left, adv_given_date, registry_date, other_info, deal_status) " +
                            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );

            pst.setString(1, txtBuyerMobile.getText());
            pst.setString(2, txtBuyerName.getText());
            pst.setString(3, txtSellerMobile.getText());
            pst.setString(4, txtSellerName.getText());
            pst.setString(5, comboPropertyName.getValue());
            pst.setFloat(6, Float.parseFloat(txtFinalAmount.getText()));
            pst.setFloat(7, Float.parseFloat(txtMyCommission.getText()));
            pst.setFloat(8, Float.parseFloat(txtAdvCommission.getText()));
            pst.setFloat(9, Float.parseFloat(txtAdvAmount.getText()));
            pst.setFloat(10, Float.parseFloat(txtAmountLeft.getText()));
            pst.setFloat(11, Float.parseFloat(txtCommissionLeft.getText()));

            if(dateAdvGiven.getValue() != null)
                pst.setDate(12, java.sql.Date.valueOf(dateAdvGiven.getValue()));
            else
                pst.setDate(12, null);

            if(dateRegistry.getValue() != null)
                pst.setDate(13, java.sql.Date.valueOf(dateRegistry.getValue()));
            else
                pst.setDate(13, null);

            pst.setString(14, txtOtherInfo.getText());

            if(radioOngoing.isSelected()) pst.setString(15, "Ongoing");
            else pst.setString(15, "Completed");

            pst.executeUpdate();
            // System.out.println("Deal Saved Successfully");
            MyAlert.alertMsg("Deal saved successfully!", Alert.AlertType.INFORMATION, "Deal Registered", "Success");
        } catch (SQLException e) {
            e.printStackTrace();
            MyAlert.alertMsg("Failed to save deal record: " + e.getMessage(), Alert.AlertType.ERROR, "Save Failed", "Database Error");
        } catch (NumberFormatException | NullPointerException e) {
            MyAlert.alertMsg("Please ensure all numerical fields and calculations are filled accurately.", Alert.AlertType.ERROR, "Invalid Input", "Validation Error");
        }
    }

    @FXML
    void initialize() {
        assert btnCalculate != null : "fx:id=\"btnCalculate\" was not injected: check your FXML file 'ManageDeal.fxml'.";
        assert btnClr != null : "fx:id=\"btnClr\" was not injected: check your FXML file 'ManageDeal.fxml'.";
        assert btnFetchBuyer != null : "fx:id=\"btnFetchBuyer\" was not injected: check your FXML file 'ManageDeal.fxml'.";
        assert btnFetchSeller != null : "fx:id=\"btnFetchSeller\" was not injected: check your FXML file 'ManageDeal.fxml'.";
        assert btnSaveDeal != null : "fx:id=\"btnSaveDeal\" was not injected: check your FXML file 'ManageDeal.fxml'.";
        assert comboPropertyName != null : "fx:id=\"comboPropertyName\" was not injected: check your FXML file 'ManageDeal.fxml'.";
        assert dateAdvGiven != null : "fx:id=\"dateAdvGiven\" was not injected: check your FXML file 'ManageDeal.fxml'.";
        assert dateRegistry != null : "fx:id=\"dateRegistry\" was not injected: check your FXML file 'ManageDeal.fxml'.";
        assert radioCompleted != null : "fx:id=\"radioCompleted\" was not injected: check your FXML file 'ManageDeal.fxml'.";
        assert radioOngoing != null : "fx:id=\"radioOngoing\" was not injected: check your FXML file 'ManageDeal.fxml'.";
        assert txtAdvAmount != null : "fx:id=\"txtAdvAmount\" was not injected: check your FXML file 'ManageDeal.fxml'.";
        assert txtAdvCommission != null : "fx:id=\"txtAdvCommission\" was not injected: check your FXML file 'ManageDeal.fxml'.";
        assert txtAmountLeft != null : "fx:id=\"txtAmountLeft\" was not injected: check your FXML file 'ManageDeal.fxml'.";
        assert txtBuyerMobile != null : "fx:id=\"txtBuyerMobile\" was not injected: check your FXML file 'ManageDeal.fxml'.";
        assert txtBuyerName != null : "fx:id=\"txtBuyerName\" was not injected: check your FXML file 'ManageDeal.fxml'.";
        assert txtCommissionLeft != null : "fx:id=\"txtCommissionLeft\" was not injected: check your FXML file 'ManageDeal.fxml'.";
        assert txtFinalAmount != null : "fx:id=\"txtFinalAmount\" was not injected: check your FXML file 'ManageDeal.fxml'.";
        assert txtMyCommission != null : "fx:id=\"txtMyCommission\" was not injected: check your FXML file 'ManageDeal.fxml'.";
        assert txtOtherInfo != null : "fx:id=\"txtOtherInfo\" was not injected: check your FXML file 'ManageDeal.fxml'.";
        assert txtSellerMobile != null : "fx:id=\"txtSellerMobile\" was not injected: check your FXML file 'ManageDeal.fxml'.";
        assert txtSellerName != null : "fx:id=\"txtSellerName\" was not injected: check your FXML file 'ManageDeal.fxml'.";
        doConnect();
    }
    Connection con;

    void doConnect(){
        con = DatabaseConnection.doConnectToDb();
        if(con == null){
            // System.out.println("Database Connection Error....");
            MyAlert.alertMsg("Could not connect to MySQL database.", Alert.AlertType.ERROR, "Database Connection Failed", "Connection Error");
        } else{
            System.out.println("Database Connected Successfully...");
        }
    }

}
