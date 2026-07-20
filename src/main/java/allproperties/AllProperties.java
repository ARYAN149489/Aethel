package allproperties;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import jdbcc.DatabaseConnection;
import myalert.MyAlert;

public class AllProperties {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnShowProperties;

    @FXML
    private ComboBox<String> comboArea;

    @FXML
    private ComboBox<String> comboCity;

    @FXML
    private ComboBox<String> comboPropertyType;

    @FXML
    private ComboBox<String> comboStructure;

    @FXML
    private TableView<PropertyBean> tableProperties;

    @FXML
    private TextField txtMaxPrice;

    @FXML
    private TextField txtMinPrice;

    @FXML
    void doShowProperties(ActionEvent event) {
        tableProperties.getColumns().clear();
        String mini = txtMinPrice.getText();
        String maxi = txtMaxPrice.getText();

        if(mini.isEmpty() || maxi.isEmpty()){
            // System.out.println("Please fill price ranges");
            MyAlert.alertMsg("Please fill both Min Price and Max Price range fields.", Alert.AlertType.WARNING, "Price Range Required", "Validation Warning");
            return;
        }
        float min, max;
        try{
            min = Float.parseFloat(mini);
            max = Float.parseFloat(maxi);
        } catch(NumberFormatException e){
            // System.out.println("Please enter Number values");
            MyAlert.alertMsg("Please enter valid numeric values for Min Price and Max Price.", Alert.AlertType.ERROR, "Invalid Price Format", "Validation Error");
            return;
        }
        if(comboCity.getSelectionModel().isEmpty() || comboArea.getSelectionModel().isEmpty()){
            // System.out.println("Please fill all the fields");
            MyAlert.alertMsg("Please select City and Area filters.", Alert.AlertType.WARNING, "Filters Required", "Validation Warning");
            return;
        }



        TableColumn<PropertyBean, String> mobileCol = new TableColumn<>("Mobile Number");
        mobileCol.setCellValueFactory(new PropertyValueFactory<PropertyBean, String>("mobileNumber"));
        mobileCol.setMinWidth(100);

        TableColumn<PropertyBean, String> propCol = new TableColumn<>("Property Name");
        propCol.setCellValueFactory(new PropertyValueFactory<PropertyBean, String>("prop_name"));
        propCol.setMinWidth(100);

        TableColumn<PropertyBean, String> addrCol = new TableColumn<>("Address");
        addrCol.setCellValueFactory(new PropertyValueFactory<PropertyBean, String>("address"));
        addrCol.setMinWidth(100);

        TableColumn<PropertyBean, String> sizeCol = new TableColumn<>("Size");
        sizeCol.setCellValueFactory(new PropertyValueFactory<PropertyBean, String>("size_dim"));
        sizeCol.setMinWidth(70);

        TableColumn<PropertyBean, String> approvedCol = new TableColumn<>("Approved By");
        approvedCol.setCellValueFactory(new PropertyValueFactory<PropertyBean, String>("approved_by"));
        approvedCol.setMinWidth(100);

        TableColumn<PropertyBean, String> priceCol = new TableColumn<>("Price Demanded");
        priceCol.setCellValueFactory(new PropertyValueFactory<PropertyBean, String>("price_demanded"));
        priceCol.setMinWidth(100);

        TableColumn<PropertyBean, String> otherCol = new TableColumn<>("Other info");
        otherCol.setCellValueFactory(new PropertyValueFactory<PropertyBean, String>("other_info"));
        otherCol.setMinWidth(100);

        //=========================================================//
        TableColumn<PropertyBean, byte[]> picCol = new TableColumn<>("Price Demanded");
        picCol.setCellValueFactory(new PropertyValueFactory<>("pic1"));
        picCol.setMinWidth(100);

        picCol.setCellFactory(col -> new TableCell<PropertyBean, byte[]>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitWidth(60);
                imageView.setFitHeight(60);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(byte[] item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.length == 0) {
                    setGraphic(null);
                } else {
                    Image image = new Image(new ByteArrayInputStream(item));
                    imageView.setImage(image);
                    setGraphic(imageView);
                }
            }
        });

        tableProperties.getColumns().addAll(mobileCol, propCol, addrCol, sizeCol, approvedCol, priceCol, otherCol, picCol);


        tableProperties.setItems(getAllProperties(min, max));

    }

    ObservableList<PropertyBean> getAllProperties(Float min, Float max){
        ObservableList<PropertyBean> list = FXCollections.observableArrayList();
        String city = comboCity.getSelectionModel().getSelectedItem();
        String area = comboArea.getSelectionModel().getSelectedItem();
        String struc = comboStructure.getSelectionModel().getSelectedItem();
        String propType = comboPropertyType.getSelectionModel().getSelectedItem();
        try{
            StringBuilder s = new StringBuilder("select mobileNumber, prop_name, address, size_dim, approved_by, price_demanded, other_info, pic1 from Properties " +
                   "where city = ? AND price_demanded BETWEEN ? AND ?");
            List<Object> params = new ArrayList<>();
            params.add(city);
            params.add(min);
            params.add(max);
            if(!area.equals("Any")){
                s.append(" AND area = ?");
                params.add(area);
            }
            if(!propType.equals("Any")){
                s.append(" AND type_usage = ?");
                params.add(propType);
            }
            if(!struc.equals("Any")){
                s.append(" AND type_status = ?");
                params.add(struc);
            }

            PreparedStatement pst = con.prepareStatement(s.toString());
            for(int i = 0; i < params.size(); ++i){
                pst.setObject(i + 1, params.get(i));
            }

            ResultSet res = pst.executeQuery();
            while (res.next()){
                String mob = res.getString(1);
                String name = res.getString(2);
                String addr = res.getString(3);
                String size = String.valueOf(res.getFloat(4));
                String appr = res.getString(5);
                String price = String.valueOf(res.getString(6));
                String other = res.getString(7);
                byte[] pic = res.getBytes(8);
                PropertyBean p = new PropertyBean(mob, name, addr, size, appr, price, other, pic);
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            MyAlert.alertMsg("Error querying properties: " + e.getMessage(), Alert.AlertType.ERROR, "Database Error", "Query Error");
        }
        return list;
    }

    @FXML
    void exportToPdf(ActionEvent event) {
        try {

            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save Properties data as Pdf");
            chooser.setInitialFileName("Properties.pdf");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pdf Files", "*.pdf"));
            File file = chooser.showSaveDialog(null);
            if(file == null)
                return;
            Document document = new Document();
            PdfWriter.getInstance(document,new FileOutputStream(file));

            document.open();
            document.add(new Paragraph("Property Detail"));
            PdfPTable table=new PdfPTable(7);

            table.addCell("Mobile");
            table.addCell("Property Name");
            table.addCell("Address");
            table.addCell("Size");
            table.addCell("Approved By");
            table.addCell("Price Demanded");
            table.addCell("Other info");

            for(PropertyBean data: tableProperties.getItems())
            {
                table.addCell(data.getMobileNumber());
                table.addCell(data.getProp_name());
                table.addCell(data.getAddress());
                table.addCell(data.getSize_dim());
                table.addCell(data.getApproved_by());
                table.addCell(data.getPrice_demanded());
                table.addCell(data.getOther_info());
            }

            document.add(table);
            document.close();
            Desktop.getDesktop().open(file);

            System.out.println("Pdf. Created");

        }
        catch(Exception ep)
        {
            ep.printStackTrace();
            MyAlert.alertMsg("Failed to generate PDF report: " + ep.getMessage(), Alert.AlertType.ERROR, "PDF Export Failed", "Error");
        }

    }

    @FXML
    void onCitySelected(ActionEvent event) {
        String city = comboCity.getSelectionModel().getSelectedItem();
        comboArea.getItems().clear();
        try{
            PreparedStatement pst = con.prepareStatement("select DISTINCT area from Properties where city = ?");
            pst.setString(1, city);
            ResultSet res = pst.executeQuery();
            comboArea.getItems().add("Any");
            while(res.next()){
                comboArea.getItems().add(res.getString(1));
            }
            // System.out.println("Areas added...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        assert btnShowProperties != null : "fx:id=\"btnShowProperties\" was not injected: check your FXML file 'AllProperties.fxml'.";
        assert comboArea != null : "fx:id=\"comboArea\" was not injected: check your FXML file 'AllProperties.fxml'.";
        assert comboCity != null : "fx:id=\"comboCity\" was not injected: check your FXML file 'AllProperties.fxml'.";
        assert comboPropertyType != null : "fx:id=\"comboPropertyType\" was not injected: check your FXML file 'AllProperties.fxml'.";
        assert comboStructure != null : "fx:id=\"comboStructure\" was not injected: check your FXML file 'AllProperties.fxml'.";
        assert tableProperties != null : "fx:id=\"tableProperties\" was not injected: check your FXML file 'AllProperties.fxml'.";
        assert txtMaxPrice != null : "fx:id=\"txtMaxPrice\" was not injected: check your FXML file 'AllProperties.fxml'.";
        assert txtMinPrice != null : "fx:id=\"txtMinPrice\" was not injected: check your FXML file 'AllProperties.fxml'.";
        doConnect();
        String[] propTypes = {"Any", "Commercial", "Residential", "Agriculture"};
        comboPropertyType.getItems().addAll(propTypes);
        comboPropertyType.getSelectionModel().select(0);

        String[] struc = {"Any", "Plot", "Constructed"};
        comboStructure.getItems().addAll(struc);
        comboStructure.getSelectionModel().select(0);
        try{
            PreparedStatement pst = con.prepareStatement("select DISTINCT city from Properties");
            ResultSet res = pst.executeQuery();
            while (res.next()){
                comboCity.getItems().add(res.getString(1));
            }
            // System.out.println("Cities added in ComboBox");
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    Connection con;
    void doConnect(){
        con = DatabaseConnection.doConnectToDb();
        if(con == null){
            // System.out.println("Database Connection error..");
            MyAlert.alertMsg("Could not connect to MySQL database.", Alert.AlertType.ERROR, "Database Connection Failed", "Connection Error");
        } else{
            System.out.println("Database connected Successfully");
        }

    }

}
