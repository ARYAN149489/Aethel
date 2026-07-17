package listproperties;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import jdbcc.DatabaseConnection;

public class ListPropertiesController {

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
    private Button btnFetch;

    @FXML
    private Button btnListNow;

    @FXML
    private Button btnRemoveListing;

    @FXML
    private Button btnUpdateDetails;

    @FXML
    private ComboBox<String> comboApprovedBy;

    @FXML
    private ComboBox<String> comboDirection;

    @FXML
    private ComboBox<String> comboListedProperties;

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
            System.out.println("Enter fields correctly");
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
            System.out.println("Invalid values to calculate area");
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
                System.out.println("Usage type not specified....");
                return;
            }
            if(radioPlot.isSelected()) pst.setString(13, "Plot");
            else if(radioConstructed.isSelected()) pst.setString(13, "Constructed");
            else{
                System.out.println("Status type not specified....");
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
            System.out.println("Added Successfully");
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doRemoveListing(ActionEvent event) {
        try{
            PreparedStatement pst = con.prepareStatement("delete from Properties where mobileNumber = ? AND prop_name = ?");
            pst.setString(1, txtMobile.getText());
            pst.setString(2, txtPropertyName.getText());
            pst.executeUpdate();
            System.out.println("Deleted Successfully...");
            doClear();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doUpdateDetails(ActionEvent event) {
        try{
            PreparedStatement pst = con.prepareStatement("update Properties set price_demanded = ?, other_info = ?, pic1 = ?, pic2 = ?, updated_at = current_date where mobileNumber = ? AND prop_name = ?");
            pst.setString(5, txtMobile.getText());
            pst.setString(6, txtPropertyName.getText());
            pst.setFloat(1, Float.parseFloat(txtTotalPrice.getText()));
            pst.setString(2, txtOtherInfo.getText());

            File fileimg1 = new File(fileRef1.getAbsolutePath());
            InputStream inpt = new FileInputStream(fileimg1);
            pst.setBinaryStream(3, (InputStream) inpt, (int)fileimg1.length());
            if(fileRef2 != null){
                File fileimg2 = new File(fileRef2.getAbsolutePath());
                InputStream inpt2 = new FileInputStream(fileimg2);
                pst.setBinaryStream(4, (InputStream) inpt2, (int)fileimg2.length());
            } else{
                pst.setBinaryStream(4, null, 0);
            }


            pst.executeUpdate();
            System.out.println("Updated Successfully...");
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void fetchProperties(ActionEvent event) {
        try{
            PreparedStatement pst = con.prepareStatement("select prop_name from Properties where mobileNumber = ?");
            pst.setString(1, txtMobile.getText());
            ResultSet res = pst.executeQuery();
            ObservableList<String> resList = FXCollections.observableArrayList();
            while(res.next()){
                String s = res.getString("prop_name");
                resList.add(s);
            }
            if(!resList.isEmpty()){
                comboListedProperties.setItems(resList);
            } else {
                comboListedProperties.setItems(null);
                System.out.println("No Property related to this mobile number found");
                doClear();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void fetchPropertyDetails(ActionEvent event) {
        try{
            PreparedStatement pst = con.prepareStatement("select * from Properties where mobileNumber = ? and prop_name = ?");
            pst.setString(1, txtMobile.getText());
            pst.setString(2, comboListedProperties.getSelectionModel().getSelectedItem());
            ResultSet res = pst.executeQuery();
            if(res.next()){
                txtPropertyName.setText(res.getString("prop_name"));
                txtArea.setText(res.getString("area"));
                txtCity.setText(res.getString("city"));
                txtAddress.setText(res.getString("address"));
                txtSize.setText(String.valueOf(res.getFloat("size_dim")));
                txtFront.setText(String.valueOf(res.getFloat("front_dim")));
                txtRear.setText(String.valueOf(res.getFloat("rear_dim")));
                txtLeft.setText(String.valueOf(res.getFloat("left_dim")));
                txtRight.setText(String.valueOf(res.getFloat("right_dim")));
                comboDirection.getSelectionModel().select(res.getString("direction_facing"));
                String type_usage = res.getString("type_usage");
                switch (type_usage){
                    case "Commercial":
                        radioCommercial.setSelected(true);
                        break;
                    case "Residential":
                        radioResidential.setSelected(true);
                        break;
                    case "Agriculture":
                        radioAgricultural.setSelected(true);
                        break;
                }
                String type_status = res.getString("type_status");
                if(type_status.equals("Plot"))
                    radioPlot.setSelected(true);
                else
                    radioConstructed.setSelected(true);

                comboApprovedBy.getSelectionModel().select(res.getString("approved_by"));
                txtTotalPrice.setText(String.valueOf(res.getFloat("price_demanded")));
                txtOtherInfo.setText(res.getString("other_info"));
                /// Very important
                byte[] pic1Bytes = res.getBytes("pic1");
                if (pic1Bytes != null) {
                    imgViewPropPic1.setImage(new Image(new ByteArrayInputStream(pic1Bytes)));

                    File tempFile1 = File.createTempFile("img_temp1", ".jpg");
                    tempFile1.deleteOnExit();
                    Files.write(tempFile1.toPath(), pic1Bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    //================================= Creates new file if not already there // ===== if prev file there then truncate it if not done then if new file is shorter then last bytes of old file will remain
                    fileRef1 = tempFile1;
                } else {
                    imgViewPropPic1.setImage(null);
                    fileRef1 = null;
                }

                byte[] pic2Bytes = res.getBytes("pic2");
                if (pic2Bytes != null) {
                    imgViewPropPic2.setImage(new Image(new ByteArrayInputStream(pic2Bytes)));

                    File tempFile2 = File.createTempFile("img_temp2", ".jpg");
                    tempFile2.deleteOnExit();
                    Files.write(tempFile2.toPath(), pic2Bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    fileRef2 = tempFile2;
                } else {
                    imgViewPropPic2.setImage(null);
                    fileRef2 = null;
                }

            } else {
                System.out.println("no property found");
                doClear();
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void initialize() {
        assert btnCalculateArea != null : "fx:id=\"btnCalculateArea\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert btnChoosePropPic1 != null : "fx:id=\"btnChoosePropPic1\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert btnChoosePropPic2 != null : "fx:id=\"btnChoosePropPic2\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert btnFetch != null : "fx:id=\"btnFetch\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert btnListNow != null : "fx:id=\"btnListNow\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert btnRemoveListing != null : "fx:id=\"btnRemoveListing\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert btnUpdateDetails != null : "fx:id=\"btnUpdateDetails\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert comboApprovedBy != null : "fx:id=\"comboApprovedBy\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert comboDirection != null : "fx:id=\"comboDirection\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert comboListedProperties != null : "fx:id=\"comboListedProperties\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert imgViewPropPic1 != null : "fx:id=\"imgViewPropPic1\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert imgViewPropPic2 != null : "fx:id=\"imgViewPropPic2\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert radioAgricultural != null : "fx:id=\"radioAgricultural\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert radioCommercial != null : "fx:id=\"radioCommercial\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert radioConstructed != null : "fx:id=\"radioConstructed\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert radioPlot != null : "fx:id=\"radioPlot\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert radioResidential != null : "fx:id=\"radioResidential\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert txtArea != null : "fx:id=\"txtArea\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert txtCity != null : "fx:id=\"txtCity\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert txtFront != null : "fx:id=\"txtFront\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert txtLeft != null : "fx:id=\"txtLeft\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert txtAddress != null : "fx:id=\"txtAddress\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert txtMobile != null : "fx:id=\"txtMobile\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert txtOtherInfo != null : "fx:id=\"txtOtherInfo\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert txtPropertyName != null : "fx:id=\"txtPropertyName\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert txtRear != null : "fx:id=\"txtRear\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert txtRight != null : "fx:id=\"txtRight\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert txtSize != null : "fx:id=\"txtSize\" was not injected: check your FXML file 'ListProperties.fxml'.";
        assert txtTotalPrice != null : "fx:id=\"txtTotalPrice\" was not injected: check your FXML file 'ListProperties.fxml'.";

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
            System.out.println("Database Connection Error....");
        } else{
            System.out.println("Database Connected Successfully...");
        }
    }

}
