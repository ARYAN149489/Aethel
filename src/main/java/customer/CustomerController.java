package customer;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import jdbcc.DatabaseConnection;
import myalert.MyAlert;
import emailsender.EmailSender;

public class CustomerController {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> comboType;

    @FXML
    private ImageView prev1;

    @FXML
    private ImageView prev2;

    @FXML
    private ImageView prev3;

    @FXML
    private TextField txtAddr;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtMob;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtCity;

    private File fileRef1;
    private File fileRef2;
    private File fileRef3;

    @FXML
    private void doBrowseImg1() {
        try{
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select Profile Picture");
            chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.jpg", "*.png"));
            fileRef1 = chooser.showOpenDialog(null);
            prev1.setImage(new Image(new FileInputStream(fileRef1)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void doBrowseImg2() {
        try{
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select Profile Picture");
            chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.jpg", "*.png"));
            fileRef2 = chooser.showOpenDialog(null);
            prev2.setImage(new Image(new FileInputStream(fileRef2)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void doBrowseImg3() {
        try{
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select Profile Picture");
            chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Images", "*.jpg", "*.png"));
            fileRef3 = chooser.showOpenDialog(null);
            prev3.setImage(new Image(new FileInputStream(fileRef3)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doClear(ActionEvent event) {
        txtMob.setText(null);
        txtName.setText(null);
        txtAddr.setText(null);
        txtCity.setText(null);
        txtEmail.setText(null);
        prev1.setImage(null);
        prev2.setImage(null);
        prev3.setImage(null);
        comboType.getSelectionModel().select(0);
    }

    @FXML
    void doSave(ActionEvent event) {
        if(comboType.getSelectionModel().getSelectedItem() == "Select"){
            // System.out.println("Select customer type");
            MyAlert.alertMsg("Please select a valid customer type (Buyer, Seller, or Both) from the dropdown.", Alert.AlertType.WARNING, "Selection Required", "Validation Error");
            return;
        }
        try{
            PreparedStatement pst = con.prepareStatement("insert into Customers values (?, ?, ?, ?, ?, ?, ?, ?, ?, current_date, current_date)"); // created, lastUpdated
            pst.setString(1, txtMob.getText());
            pst.setString(2, txtName.getText());
            pst.setString(3, txtAddr.getText());
            pst.setString(4, txtCity.getText());
            pst.setString(5, txtEmail.getText());
            pst.setString(6, comboType.getValue());

            File fileimg = new File(fileRef1.getAbsolutePath());
            FileInputStream strm = new FileInputStream(fileimg);
            pst.setBinaryStream(7, (InputStream) strm, (int)fileimg.length());

            fileimg = new File(fileRef2.getAbsolutePath());
            strm = new FileInputStream(fileimg);
            pst.setBinaryStream(8, (InputStream) strm, (int)fileimg.length());

            fileimg = new File(fileRef3.getAbsolutePath());
            strm = new FileInputStream(fileimg);
            pst.setBinaryStream(9, (InputStream) strm, (int)fileimg.length());

            pst.executeUpdate();
            EmailSender.sendCustomerEmailAsync("Registration", txtEmail.getText(), txtName.getText(), txtMob.getText(), comboType.getValue(), txtAddr.getText(), txtCity.getText());
            // System.out.println("Record Saved Successfully");
            MyAlert.alertMsg("Customer record saved successfully!", Alert.AlertType.INFORMATION, "Registration Successful", "Success");

        } catch (SQLException e) {
            e.printStackTrace();
            MyAlert.alertMsg("Failed to save customer record. Please check if mobile number is unique. Error: " + e.getMessage(), Alert.AlertType.ERROR, "Save Failed", "Database Error");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            MyAlert.alertMsg("Please select all three required images (Profile, Aadhar Front, Aadhar Back).", Alert.AlertType.ERROR, "Missing Images", "File Error");
        } catch (NullPointerException e) {
            MyAlert.alertMsg("Please ensure all customer fields and required images are selected.", Alert.AlertType.ERROR, "Incomplete Details", "Validation Error");
        }
    }

    @FXML
    void doUpdate(ActionEvent event) {
        if(comboType.getSelectionModel().getSelectedItem() == "Select"){
            // System.out.println("Select customer type");
            MyAlert.alertMsg("Please select a valid customer type (Buyer, Seller, or Both).", Alert.AlertType.WARNING, "Selection Required", "Validation Error");
            return;
        }
        try{
            PreparedStatement pst = con.prepareStatement("update Customers set name = ?, address = ?, city = ?, email = ?, type = ?, profilePic = ?, aadharFrontPic = ?, aadharBackPic = ?, lastUpdatedAt = current_date where mobileNumber = ?");
            pst.setString(9, txtMob.getText());
            pst.setString(1, txtName.getText());
            pst.setString(2, txtAddr.getText());
            pst.setString(3, txtCity.getText());
            pst.setString(4, txtEmail.getText());
            pst.setString(5, comboType.getValue());

            File fileimg = new File(fileRef1.getAbsolutePath());
            FileInputStream strm = new FileInputStream(fileimg);
            pst.setBinaryStream(6, (InputStream) strm, (int)fileimg.length());

            fileimg = new File(fileRef2.getAbsolutePath());
            strm = new FileInputStream(fileimg);
            pst.setBinaryStream(7, (InputStream) strm, (int)fileimg.length());

            fileimg = new File(fileRef3.getAbsolutePath());
            strm = new FileInputStream(fileimg);
            pst.setBinaryStream(8, (InputStream) strm, (int)fileimg.length());

            pst.executeUpdate();
            EmailSender.sendCustomerEmailAsync("Update", txtEmail.getText(), txtName.getText(), txtMob.getText(), comboType.getValue(), txtAddr.getText(), txtCity.getText());
            // System.out.println("Record Updated Successfully");
            MyAlert.alertMsg("Customer record updated successfully!", Alert.AlertType.INFORMATION, "Update Successful", "Success");

        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
            MyAlert.alertMsg("Failed to update customer record: " + e.getMessage(), Alert.AlertType.ERROR, "Update Failed", "Error");
        } catch (NullPointerException e) {
            MyAlert.alertMsg("Please select images to update customer profile.", Alert.AlertType.ERROR, "Missing Images", "Validation Error");
        }
    }

    @FXML
    void doDelete(ActionEvent event) {
        try{
            PreparedStatement pst = con.prepareStatement("delete from Customers where mobileNumber = ?");
            pst.setString(1, txtMob.getText());

            pst.executeUpdate();
            // System.out.println("Deleted Successfully...");
            MyAlert.alertMsg("Customer record deleted successfully.", Alert.AlertType.INFORMATION, "Deletion Successful", "Success");
            doClear(null);
        } catch (SQLException e) {
            MyAlert.alertMsg("Failed to delete customer record: " + e.getMessage(), Alert.AlertType.ERROR, "Deletion Failed", "Error");
        }
    }

    @FXML
    void doSearch(ActionEvent event){
        try{
            PreparedStatement pst = con.prepareStatement("select * from Customers where mobileNumber = ?");
            pst.setString(1, txtMob.getText());
            ResultSet res = pst.executeQuery();
            if(res.next()){
                String name = res.getString("name");
                txtName.setText(name);
                String address = res.getString("address");
                txtAddr.setText(address);
                String city = res.getString("city");
                txtCity.setText(city);
                String email = res.getString("email");
                txtEmail.setText(email);
                String type = res.getString("type");
                comboType.getSelectionModel().select(type);

//                InputStream profilePic = res.getBinaryStream("profilePic");
//                prev1.setImage(new Image(profilePic));
//                InputStream aadharFrontPic = res.getBinaryStream("aadharFrontPic");
//                prev2.setImage(new Image(aadharFrontPic));
//                InputStream aadharBackPic = res.getBinaryStream("aadharBackPic");
//                prev3.setImage(new Image(aadharBackPic));

                byte[] profilePic = res.getBytes("profilePic");
                if (profilePic != null) {
                    prev1.setImage(new Image(new ByteArrayInputStream(profilePic)));

                    File tempFile1 = File.createTempFile("img_temp1", ".jpg");
                    tempFile1.deleteOnExit();
                    Files.write(tempFile1.toPath(), profilePic, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    //================================= Creates new file if not already there // ===== if prev file there then truncate it if not done then if new file is shorter then last bytes of old file will remain
                    fileRef1 = tempFile1;
                } else {
                    prev1.setImage(null);
                    fileRef1 = null;
                }

                byte[] aadharFrontPic = res.getBytes("aadharFrontPic");
                if (aadharFrontPic != null) {
                    prev2.setImage(new Image(new ByteArrayInputStream(aadharFrontPic)));

                    File tempFile1 = File.createTempFile("img_temp2", ".jpg");
                    tempFile1.deleteOnExit();
                    Files.write(tempFile1.toPath(), aadharFrontPic, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    //================================= Creates new file if not already there // ===== if prev file there then truncate it if not done then if new file is shorter then last bytes of old file will remain
                    fileRef2 = tempFile1;
                } else {
                    prev2.setImage(null);
                    fileRef2 = null;
                }

                byte[] aadharBackPic = res.getBytes("aadharBackPic");
                if (aadharBackPic != null) {
                    prev3.setImage(new Image(new ByteArrayInputStream(aadharBackPic)));

                    File tempFile1 = File.createTempFile("img_temp3", ".jpg");
                    tempFile1.deleteOnExit();
                    Files.write(tempFile1.toPath(), aadharBackPic, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    //================================= Creates new file if not already there // ===== if prev file there then truncate it if not done then if new file is shorter then last bytes of old file will remain
                    fileRef3 = tempFile1;
                } else {
                    prev3.setImage(null);
                    fileRef3 = null;
                }

                System.out.println("Fetched Successfully");

            } else{
                // System.out.println("No record found...");
                MyAlert.alertMsg("No customer record found matching mobile number: " + txtMob.getText(), Alert.AlertType.WARNING, "Record Not Found", "Search Result");
                doClear(null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            MyAlert.alertMsg("Error searching database: " + e.getMessage(), Alert.AlertType.ERROR, "Search Error", "Database Error");
        } catch (IOException e) {
            e.printStackTrace();
            MyAlert.alertMsg("Error handling picture: " + e.getMessage(), Alert.AlertType.ERROR, "Picture fetch error", "File IO Error");
        }


    }



    @FXML
    void initialize() {
        assert comboType != null : "fx:id=\"comboType\" was not injected: check your FXML file 'CustomerView.fxml'.";
        assert prev1 != null : "fx:id=\"prev1\" was not injected: check your FXML file 'CustomerView.fxml'.";
        assert prev2 != null : "fx:id=\"prev2\" was not injected: check your FXML file 'CustomerView.fxml'.";
        assert prev3 != null : "fx:id=\"prev3\" was not injected: check your FXML file 'CustomerView.fxml'.";
        assert txtAddr != null : "fx:id=\"txtAddr\" was not injected: check your FXML file 'CustomerView.fxml'.";
        assert txtEmail != null : "fx:id=\"txtEmail\" was not injected: check your FXML file 'CustomerView.fxml'.";
        assert txtMob != null : "fx:id=\"txtMob\" was not injected: check your FXML file 'CustomerView.fxml'.";
        assert txtName != null : "fx:id=\"txtName\" was not injected: check your FXML file 'CustomerView.fxml'.";
        assert txtCity != null : "fx:id=\"txtName1\" was not injected: check your FXML file 'CustomerView.fxml'.";
        doConnect();
        String[] types = {"Select", "Buyer", "Seller", "Both"};
        comboType.getItems().addAll(types);
        comboType.getSelectionModel().select(0);
    }

    Connection con;

    public void doConnect(){
        con = DatabaseConnection.doConnectToDb();
        if(con == null){
            // System.out.println("Database Connection Error....");
            MyAlert.alertMsg("Could not connect to MySQL database. Please verify the database server is running.", Alert.AlertType.ERROR, "Database Connection Failed", "Connection Error");
        } else{
            System.out.println("Database Connected Successfully...");
        }
    }

}
