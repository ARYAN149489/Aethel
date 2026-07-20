package updatedeal;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import jdbcc.DatabaseConnection;
import myalert.MyAlert;

public class UpdateDealController {

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
    private Button btnDeleteDeal;

    @FXML
    private Button btnFetchDeals;

    @FXML
    private Button btnUpdateDeal;

    @FXML
    private ComboBox<Integer> comboDealId;

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
            MyAlert.alertMsg("Please fill Final Amount, My Commission, Advance Commission, and Advance Amount.", Alert.AlertType.WARNING, "Incomplete Calculation Fields", "Input Error");
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
            MyAlert.alertMsg("Please enter valid numeric values for amounts and commissions.", Alert.AlertType.ERROR, "Invalid Number Format", "Calculation Error");
            return false;
        }
    }

    @FXML
    void clearAll(ActionEvent event) {
        comboDealId.getSelectionModel().clearSelection();
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
    void fetchOngoingDeals(ActionEvent event) {
        try{
            comboDealId.getItems().clear();
            PreparedStatement pst = con.prepareStatement("select DealId from Deals where deal_status = ?");
            pst.setString(1, "Ongoing");
            ResultSet res = pst.executeQuery();
            ObservableList<Integer> ids = FXCollections.observableArrayList();
            while(res.next()){
                ids.add(res.getInt("DealId"));
            }
            if(!ids.isEmpty()){
                comboDealId.setItems(ids);
            } else{
                comboDealId.setItems(null);
                // System.out.println("No ongoing deals found...");
                MyAlert.alertMsg("No ongoing deals currently found in the database.", Alert.AlertType.INFORMATION, "No Active Deals", "Information");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            MyAlert.alertMsg("Error fetching ongoing deals: " + e.getMessage(), Alert.AlertType.ERROR, "Fetch Failed", "Database Error");
        }
    }

    @FXML
    void fetchDealDetails(ActionEvent event) {
        Integer dealId = comboDealId.getSelectionModel().getSelectedItem();
        if(dealId == null)
            return;
        try{
            PreparedStatement pst = con.prepareStatement("select * from Deals where DealId = ?");
            pst.setInt(1, dealId);
            ResultSet res = pst.executeQuery();
            if(res.next()){
                txtBuyerMobile.setText(res.getString("buyer_mobile"));
                txtBuyerName.setText(res.getString("buyer_name"));
                txtSellerMobile.setText(res.getString("seller_mobile"));
                txtSellerName.setText(res.getString("seller_name"));

                comboPropertyName.getItems().setAll(res.getString("property_name"));
                comboPropertyName.getSelectionModel().select(res.getString("property_name"));

                txtFinalAmount.setText(String.valueOf(res.getFloat("final_amount")));
                txtMyCommission.setText(String.valueOf(res.getFloat("my_commission")));
                txtAdvCommission.setText(String.valueOf(res.getFloat("adv_commission")));
                txtAdvAmount.setText(String.valueOf(res.getFloat("adv_amount")));
                txtAmountLeft.setText(String.valueOf(res.getFloat("amount_left")));
                txtCommissionLeft.setText(String.valueOf(res.getFloat("commission_left")));

                java.sql.Date advDate = res.getDate("adv_given_date");
                dateAdvGiven.setValue(advDate != null ? advDate.toLocalDate() : null);

                java.sql.Date regDate = res.getDate("registry_date");
                dateRegistry.setValue(regDate != null ? regDate.toLocalDate() : null);

                txtOtherInfo.setText(res.getString("other_info"));

                String status = res.getString("deal_status");
                if("Completed".equals(status))
                    radioCompleted.setSelected(true);
                else
                    radioOngoing.setSelected(true);
            } else{
                // System.out.println("No deal found for selected Deal ID");
                MyAlert.alertMsg("No deal record found for selected Deal ID.", Alert.AlertType.WARNING, "Deal Not Found", "Search Result");
                clearAll(null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            MyAlert.alertMsg("Error retrieving deal details: " + e.getMessage(), Alert.AlertType.ERROR, "Fetch Error", "Database Error");
        }
    }

    @FXML
    void updateDeal(ActionEvent event) {
        Integer dealId = comboDealId.getSelectionModel().getSelectedItem();
        if(dealId == null){
            // System.out.println("Select a Deal ID first....");
            MyAlert.alertMsg("Please select a Deal ID from the dropdown list.", Alert.AlertType.WARNING, "Selection Required", "Validation Error");
            return;
        }
        if(radioOngoing.isSelected() == false && radioCompleted.isSelected() == false){
            // System.out.println("Deal status not specified....");
            MyAlert.alertMsg("Please specify deal status (Ongoing or Completed).", Alert.AlertType.WARNING, "Status Required", "Validation Error");
            return;
        }
        try{
            PreparedStatement pst = con.prepareStatement(
                    "update Deals set final_amount = ?, my_commission = ?, amount_left = ?, " +
                            "commission_left = ?, registry_date = ?, other_info = ?, deal_status = ?, " +
                            "updated_at = current_date where DealId = ?"
            );
            pst.setFloat(1, Float.parseFloat(txtFinalAmount.getText()));
            pst.setFloat(2, Float.parseFloat(txtMyCommission.getText()));
            pst.setFloat(3, Float.parseFloat(txtAmountLeft.getText()));
            pst.setFloat(4, Float.parseFloat(txtCommissionLeft.getText()));

            if(dateRegistry.getValue() != null)
                pst.setDate(5, java.sql.Date.valueOf(dateRegistry.getValue()));
            else
                pst.setDate(5, null);

            pst.setString(6, txtOtherInfo.getText());

            String status = radioCompleted.isSelected() ? "Completed" : "Ongoing";
            pst.setString(7, status);

            pst.setInt(8, dealId);

            pst.executeUpdate();
            // System.out.println("Deal Updated Successfully...");
            MyAlert.alertMsg("Deal record updated successfully!", Alert.AlertType.INFORMATION, "Deal Updated", "Success");

            if(status.equals("Completed")){
                PreparedStatement pstDel = con.prepareStatement("delete from Properties where mobileNumber = ? AND prop_name = ?");
                pstDel.setString(1, txtSellerMobile.getText());
                pstDel.setString(2, comboPropertyName.getSelectionModel().getSelectedItem());
                pstDel.executeUpdate();
                // System.out.println("Deal completed - property removed from listings...");
                MyAlert.alertMsg("Deal marked as Completed! The associated property has been removed from active property listings.", Alert.AlertType.INFORMATION, "Property Removed", "Deal Finalized");
            }

            fetchOngoingDeals(null);
        } catch (SQLException e) {
            e.printStackTrace();
            MyAlert.alertMsg("Failed to update deal: " + e.getMessage(), Alert.AlertType.ERROR, "Update Failed", "Database Error");
        } catch (NumberFormatException e) {
            MyAlert.alertMsg("Please ensure numerical fields contain valid numbers.", Alert.AlertType.ERROR, "Invalid Input", "Validation Error");
        }
    }

    @FXML
    void cancelDeal(ActionEvent event) {
        Integer dealId = comboDealId.getSelectionModel().getSelectedItem();
        if(dealId == null){
            // System.out.println("Select a Deal ID first....");
            MyAlert.alertMsg("Please select a Deal ID to cancel.", Alert.AlertType.WARNING, "Selection Required", "Validation Error");
            return;
        }
        try{
            PreparedStatement pst = con.prepareStatement("update Deals set deal_status = ? where DealId = ?");
            pst.setString(1, "Cancelled");
            pst.setInt(2, dealId);
            pst.executeUpdate();
            // System.out.println("Deal Cancelled Successfully...");
            MyAlert.alertMsg("Deal status updated to Cancelled.", Alert.AlertType.INFORMATION, "Deal Cancelled", "Success");
            clearAll(null);
            fetchOngoingDeals(null);
        } catch (SQLException e) {
            e.printStackTrace();
            MyAlert.alertMsg("Failed to cancel deal: " + e.getMessage(), Alert.AlertType.ERROR, "Cancellation Failed", "Database Error");
        }
    }

    @FXML
    void initialize() {
        assert btnCalculate != null : "fx:id=\"btnCalculate\" was not injected: check your FXML file 'UpdateDeal.fxml'.";
        assert btnClr != null : "fx:id=\"btnClr\" was not injected: check your FXML file 'UpdateDeal.fxml'.";
        assert btnDeleteDeal != null : "fx:id=\"btnDeleteDeal\" was not injected: check your FXML file 'UpdateDeal.fxml'.";
        assert btnFetchDeals != null : "fx:id=\"btnFetchDeals\" was not injected: check your FXML file 'UpdateDeal.fxml'.";
        assert btnUpdateDeal != null : "fx:id=\"btnUpdateDeal\" was not injected: check your FXML file 'UpdateDeal.fxml'.";
        assert comboDealId != null : "fx:id=\"comboDealId\" was not injected: check your FXML file 'UpdateDeal.fxml'.";
        assert comboPropertyName != null : "fx:id=\"comboPropertyName\" was not injected: check your FXML file 'UpdateDeal.fxml'.";
        assert dateAdvGiven != null : "fx:id=\"dateAdvGiven\" was not injected: check your FXML file 'UpdateDeal.fxml'.";
        assert dateRegistry != null : "fx:id=\"dateRegistry\" was not injected: check your FXML file 'UpdateDeal.fxml'.";
        assert radioCompleted != null : "fx:id=\"radioCompleted\" was not injected: check your FXML file 'UpdateDeal.fxml'.";
        assert radioOngoing != null : "fx:id=\"radioOngoing\" was not injected: check your FXML file 'UpdateDeal.fxml'.";
        assert txtAdvAmount != null : "fx:id=\"txtAdvAmount\" was not injected: check your FXML file 'UpdateDeal.fxml'.";
        assert txtAdvCommission != null : "fx:id=\"txtAdvCommission\" was not injected: check your FXML file 'UpdateDeal.fxml'.";
        assert txtAmountLeft != null : "fx:id=\"txtAmountLeft\" was not injected: check your FXML file 'UpdateDeal.fxml'.";
        assert txtBuyerMobile != null : "fx:id=\"txtBuyerMobile\" was not injected: check your FXML file 'UpdateDeal.fxml'.";
        assert txtBuyerName != null : "fx:id=\"txtBuyerName\" was not injected: check your FXML file 'UpdateDeal.fxml'.";
        assert txtCommissionLeft != null : "fx:id=\"txtCommissionLeft\" was not injected: check your FXML file 'UpdateDeal.fxml'.";
        assert txtFinalAmount != null : "fx:id=\"txtFinalAmount\" was not injected: check your FXML file 'UpdateDeal.fxml'.";
        assert txtMyCommission != null : "fx:id=\"txtMyCommission\" was not injected: check your FXML file 'UpdateDeal.fxml'.";
        assert txtOtherInfo != null : "fx:id=\"txtOtherInfo\" was not injected: check your FXML file 'UpdateDeal.fxml'.";
        assert txtSellerMobile != null : "fx:id=\"txtSellerMobile\" was not injected: check your FXML file 'UpdateDeal.fxml'.";
        assert txtSellerName != null : "fx:id=\"txtSellerName\" was not injected: check your FXML file 'UpdateDeal.fxml'.";

        doConnect();
        fetchOngoingDeals(null);
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