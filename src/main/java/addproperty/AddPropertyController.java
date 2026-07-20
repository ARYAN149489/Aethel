package addproperty;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import jdbcc.DatabaseConnection;
import myalert.MyAlert;
import emailsender.EmailSender;
import java.sql.ResultSet;

public class AddPropertyController {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCalculateArea;

    @FXML
    private Button btnChoosePropPic1;

    @FXML
    private Button btnChoosePropPic2;

    @FXML
    private Button btnListNow;

    @FXML
    private Button btnClear;

    @FXML
    private ComboBox<String> comboApprovedBy;

    @FXML
    private ComboBox<String> comboDirection;

    @FXML
    private ImageView imgViewPropPic1;

    @FXML
    private ImageView imgViewPropPic2;

    @FXML
    private RadioButton radioAgricultural;

    @FXML
    private RadioButton radioCommercial;

    @FXML
    private RadioButton radioConstructed;

    @FXML
    private RadioButton radioPlot;

    @FXML
    private RadioButton radioResidential;

    @FXML
    private TextField txtArea;

    @FXML
    private TextField txtCity;

    @FXML
    private TextField txtFront;

    @FXML
    private TextField txtLeft;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtMobile;

    @FXML
    private TextField txtOtherInfo;

    @FXML
    private TextField txtPropertyName;

    @FXML
    private TextField txtRear;

    @FXML
    private TextField txtRight;

    @FXML
    private TextField txtSize;

    @FXML
    private TextField txtTotalPrice;

    @FXML
    void doClear(){
        txtMobile.clear();
        txtPropertyName.clear();
        txtArea.clear();
        txtCity.clear();
        txtAddress.clear();
        txtSize.clear();
        txtFront.clear();
        txtRear.clear();
        txtLeft.clear();
        txtRight.clear();
        comboDirection.getSelectionModel().clearSelection();
        radioPlot.setSelected(false);
        radioConstructed.setSelected(false);
        radioAgricultural.setSelected(false);
        radioResidential.setSelected(false);
        radioCommercial.setSelected(false);
        comboApprovedBy.getSelectionModel().clearSelection();
        txtTotalPrice.clear();
        txtOtherInfo.clear();
        imgViewPropPic1.setImage(null);
        imgViewPropPic2.setImage(null);
        fileRef1 = null;
        fileRef2 = null;
    }

    @FXML
    boolean calculateArea(ActionEvent event) {
        String front = txtFront.getText();
        String back = txtRear.getText();
        String left = txtLeft.getText();
        String right = txtRight.getText();
        if(front.isEmpty() || back.isEmpty() || left.isEmpty() || right.isEmpty()){
            // System.out.println("Enter fields correctly");
            MyAlert.alertMsg("Please fill all property dimension fields (Front, Rear, Left, Right) to calculate size.", Alert.AlertType.WARNING, "Incomplete Dimensions", "Input Required");
            return false;
        }
        try{
            Double fLen = Double.parseDouble(front);
            Double bLen = Double.parseDouble(back);
            Double lLen = Double.parseDouble(left);
            Double rLen = Double.parseDouble(right);
            Double s = (fLen + bLen + lLen + rLen)/ 2;
            Double area = Math.sqrt((s - fLen)*(s - bLen)*(s - lLen)*(s -rLen));
            txtSize.setText(String.valueOf(area));
            return true;
        } catch (RuntimeException e){
            // System.out.println("Invalid values to calculate area");
            MyAlert.alertMsg("Please enter valid numerical values for property dimensions.", Alert.AlertType.ERROR, "Invalid Number Format", "Calculation Error");
            return false;
        }

    }
    File fileRef1 = null,  fileRef2 = null;
    @FXML
    void doChoosePropPic1(ActionEvent event) {
        try{
            FileChooser chooser = new FileChooser();

            chooser.setTitle("Select Profile picture");
            chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.jpg", "*.png"));
            fileRef1 = chooser.showOpenDialog(null);
            imgViewPropPic1.setImage(new Image(new FileInputStream(fileRef1)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doChoosePropPic2(ActionEvent event) {
        try{
            FileChooser chooser = new FileChooser();

            chooser.setTitle("Select Profile picture");
            chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.jpg", "*.png"));
            fileRef2 = chooser.showOpenDialog(null);
            imgViewPropPic2.setImage(new Image(new FileInputStream(fileRef2)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doListNow(ActionEvent event) {

        if(!calculateArea(null))
            return;

        try{
            PreparedStatement pst = con.prepareStatement("insert into Properties values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, current_date, current_date)");
            pst.setString(1, txtMobile.getText());
            pst.setString(2, txtPropertyName.getText());
            pst.setString(3, txtAddress.getText());
            pst.setString(4, txtArea.getText());
            pst.setString(5, txtCity.getText());
            pst.setFloat(6, Float.parseFloat(txtSize.getText()));
            pst.setFloat(7, Float.parseFloat(txtFront.getText()));
            pst.setFloat(8, Float.parseFloat(txtRear.getText()));
            pst.setFloat(9, Float.parseFloat(txtLeft.getText()));
            pst.setFloat(10, Float.parseFloat(txtRight.getText()));
            pst.setString(11, comboDirection.getSelectionModel().getSelectedItem());
            if(radioCommercial.isSelected()) pst.setString(12, "Commercial");
            else if(radioResidential.isSelected()) pst.setString(12, "Residential");
            else if(radioAgricultural.isSelected()) pst.setString(12, "Agriculture");
            else{
                // System.out.println("Usage type not specified....");
                MyAlert.alertMsg("Please select a property usage type (Commercial, Residential, or Agriculture).", Alert.AlertType.WARNING, "Usage Type Required", "Validation Warning");
                return;
            }
            if(radioPlot.isSelected()) pst.setString(13, "Plot");
            else if(radioConstructed.isSelected()) pst.setString(13, "Constructed");
            else{
                // System.out.println("Status type not specified....");
                MyAlert.alertMsg("Please select a land status type (Plot or Constructed).", Alert.AlertType.WARNING, "Status Type Required", "Validation Warning");
                return;
            }
            pst.setString(14, comboApprovedBy.getSelectionModel().getSelectedItem());
            pst.setFloat(15, Float.parseFloat(txtTotalPrice.getText()));
            pst.setString(16, txtOtherInfo.getText());

            File fileimg1 = new File(fileRef1.getAbsolutePath());
            InputStream inpt = new FileInputStream(fileimg1);
            pst.setBinaryStream(17, (InputStream) inpt, (int)fileimg1.length());
            if(fileRef2 != null) {
                File fileimg2 = new File(fileRef2.getAbsolutePath());
                InputStream inpt2 = new FileInputStream(fileimg2);
                pst.setBinaryStream(18, (InputStream) inpt2, (int) fileimg2.length());
            } else{
                pst.setBinaryStream(18, null, 0);
            }

            pst.executeUpdate();
            
            try {
                PreparedStatement pstCust = con.prepareStatement("select name, email from Customers where mobileNumber = ?");
                pstCust.setString(1, txtMobile.getText());
                ResultSet resCust = pstCust.executeQuery();
                if (resCust.next()) {
                    String sellerName = resCust.getString("name");
                    String sellerEmail = resCust.getString("email");
                    String usageType = radioCommercial.isSelected() ? "Commercial" : (radioResidential.isSelected() ? "Residential" : "Agriculture");
                    String statusType = radioPlot.isSelected() ? "Plot" : "Constructed";
                    EmailSender.sendPropertyEmailAsync(
                        "Created",
                        sellerEmail,
                        sellerName,
                        txtMobile.getText(),
                        txtPropertyName.getText(),
                        txtAddress.getText(),
                        txtArea.getText(),
                        txtCity.getText(),
                        txtSize.getText(),
                        txtFront.getText(),
                        txtRear.getText(),
                        txtLeft.getText(),
                        txtRight.getText(),
                        comboDirection.getSelectionModel().getSelectedItem(),
                        usageType,
                        statusType,
                        comboApprovedBy.getSelectionModel().getSelectedItem(),
                        txtTotalPrice.getText(),
                        txtOtherInfo.getText()
                    );
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // System.out.println("Added Successfully");
            MyAlert.alertMsg("Property listed successfully!", Alert.AlertType.INFORMATION, "Property Added", "Success");
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
            MyAlert.alertMsg("Failed to add property listing. Please ensure seller mobile number is valid and picture 1 is uploaded. Error: " + e.getMessage(), Alert.AlertType.ERROR, "Save Failed", "Error");
        } catch (NullPointerException e) {
            MyAlert.alertMsg("Please ensure property picture 1 and mandatory selections are filled.", Alert.AlertType.ERROR, "Incomplete Data", "Validation Error");
        }
    }

    @FXML
    void initialize() {
        assert btnCalculateArea != null : "fx:id=\"btnCalculateArea\" was not injected: check your FXML file 'AddProperty.fxml'.";
        assert btnChoosePropPic1 != null : "fx:id=\"btnChoosePropPic1\" was not injected: check your FXML file 'AddProperty.fxml'.";
        assert btnChoosePropPic2 != null : "fx:id=\"btnChoosePropPic2\" was not injected: check your FXML file 'AddProperty.fxml'.";
        assert btnListNow != null : "fx:id=\"btnListNow\" was not injected: check your FXML file 'AddProperty.fxml'.";
        assert comboApprovedBy != null : "fx:id=\"comboApprovedBy\" was not injected: check your FXML file 'AddProperty.fxml'.";
        assert comboDirection != null : "fx:id=\"comboDirection\" was not injected: check your FXML file 'AddProperty.fxml'.";
        assert imgViewPropPic1 != null : "fx:id=\"imgViewPropPic1\" was not injected: check your FXML file 'AddProperty.fxml'.";
        assert imgViewPropPic2 != null : "fx:id=\"imgViewPropPic2\" was not injected: check your FXML file 'AddProperty.fxml'.";
        assert radioAgricultural != null : "fx:id=\"radioAgricultural\" was not injected: check your FXML file 'AddProperty.fxml'.";
        assert radioCommercial != null : "fx:id=\"radioCommercial\" was not injected: check your FXML file 'AddProperty.fxml'.";
        assert radioConstructed != null : "fx:id=\"radioConstructed\" was not injected: check your FXML file 'AddProperty.fxml'.";
        assert radioPlot != null : "fx:id=\"radioPlot\" was not injected: check your FXML file 'AddProperty.fxml'.";
        assert radioResidential != null : "fx:id=\"radioResidential\" was not injected: check your FXML file 'AddProperty.fxml'.";
        assert txtArea != null : "fx:id=\"txtArea\" was not injected: check your FXML file 'AddProperty.fxml'.";
        assert txtCity != null : "fx:id=\"txtCity\" was not injected: check your FXML file 'AddProperty.fxml'.";
        assert txtFront != null : "fx:id=\"txtFront\" was not injected: check your FXML file 'AddProperty.fxml'.";
        assert txtLeft != null : "fx:id=\"txtLeft\" was not injected: check your FXML file 'AddProperty.fxml'.";
        assert txtAddress != null : "fx:id=\"txtAddress\" was not injected: check your FXML file 'AddProperty.fxml'.";
        assert txtMobile != null : "fx:id=\"txtMobile\" was not injected: check your FXML file 'AddProperty.fxml'.";
        assert txtOtherInfo != null : "fx:id=\"txtOtherInfo\" was not injected: check your FXML file 'AddProperty.fxml'.";
        assert txtPropertyName != null : "fx:id=\"txtPropertyName\" was not injected: check your FXML file 'AddProperty.fxml'.";
        assert txtRear != null : "fx:id=\"txtRear\" was not injected: check your FXML file 'AddProperty.fxml'.";
        assert txtRight != null : "fx:id=\"txtRight\" was not injected: check your FXML file 'AddProperty.fxml'.";
        assert txtSize != null : "fx:id=\"txtSize\" was not injected: check your FXML file 'AddProperty.fxml'.";
        assert txtTotalPrice != null : "fx:id=\"txtTotalPrice\" was not injected: check your FXML file 'AddProperty.fxml'.";

        ToggleGroup propertyTypeGroup = new ToggleGroup();
        radioCommercial.setToggleGroup(propertyTypeGroup);
        radioResidential.setToggleGroup(propertyTypeGroup);
        radioAgricultural.setToggleGroup(propertyTypeGroup);

        ToggleGroup landTypeGroup = new ToggleGroup();
        radioPlot.setToggleGroup(landTypeGroup);
        radioConstructed.setToggleGroup(landTypeGroup);

        String[] dirs = {"North", "East", "South", "West", "North-West", "North-East", "South-West", "South-East"};
        comboDirection.getItems().addAll(dirs);

        String[] approved = {"PUDA", "DTCP", "UDA"};
        comboApprovedBy.getItems().addAll(approved);

        doConnect();

    }
    Connection con;

    public void doConnect(){
        con = DatabaseConnection.doConnectToDb();
        if(con == null){
            // System.out.println("Database Connection Error....");
            MyAlert.alertMsg("Could not connect to MySQL database. Please verify database server.", Alert.AlertType.ERROR, "Database Connection Failed", "Connection Error");
        } else{
            System.out.println("Database Connected Successfully...");
        }
    }

}
