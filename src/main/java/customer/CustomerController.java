package customer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import jdbcc.DatabaseConnection;

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
            System.out.println("Select customer type");
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
            System.out.println("Record Saved Successfully");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doUpdate(ActionEvent event) {
        if(comboType.getSelectionModel().getSelectedItem() == "Select"){
            System.out.println("Select customer type");
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
            System.out.println("Record Updated Successfully");

        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doDelete(ActionEvent event) {
        try{
            PreparedStatement pst = con.prepareStatement("delete from Customers where mobileNumber = ?");
            pst.setString(1, txtMob.getText());

            pst.executeUpdate();
            System.out.println("Deleted Successfully...");
            doClear(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

                InputStream profilePic = res.getBinaryStream("profilePic");
                prev1.setImage(new Image(profilePic));
                InputStream aadharFrontPic = res.getBinaryStream("aadharFrontPic");
                prev2.setImage(new Image(aadharFrontPic));
                InputStream aadharBackPic = res.getBinaryStream("aadharBackPic");
                prev3.setImage(new Image(aadharBackPic));

                System.out.println("Fetched Successfully");

            } else{
                System.out.println("No record found...");
                doClear(null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
            System.out.println("Database Connection Error....");
        } else{
            System.out.println("Database Connected Successfully...");
        }
    }

}
