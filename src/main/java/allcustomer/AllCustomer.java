package allcustomer;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import jdbcc.DatabaseConnection;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AllCustomer {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnShowCustomers;

    @FXML
    private TableColumn<CustomerBean, String> colAddr;

    @FXML
    private TableColumn<CustomerBean, String> colCity;

    @FXML
    private TableColumn<CustomerBean, String> colEmail;

    @FXML
    private TableColumn<CustomerBean, String> colMobile;

    @FXML
    private TableColumn<CustomerBean, String> colName;

    @FXML
    private TableColumn<CustomerBean, String> colType;

    @FXML
    private ComboBox<String> comboCustomerType;

    @FXML
    private TableView<CustomerBean> tableCustomers;

    @FXML
    private TableColumn<CustomerBean, byte[]> colProfilePic;

    @FXML
    void exportToExcel(ActionEvent event) {
        ObservableList<CustomerBean> list = tableCustomers.getItems();
        if(list == null || list.isEmpty()){
            System.out.println("No data to export....");
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Customers as Excel");
        chooser.setInitialFileName("Customers.xlsx");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = chooser.showSaveDialog(null);
        if(file == null)
            return;

        try(XSSFWorkbook workbook = new XSSFWorkbook()){
            XSSFSheet sheet = workbook.createSheet("Customers");
            XSSFDrawing drawing = sheet.createDrawingPatriarch(); // for picture

            // Header row
            Row header = sheet.createRow(0);
            String[] headers = {"Mobile", "Name", "Address", "City", "Email", "Type", "Profile Pic"};
            for(int i = 0; i < headers.length; i++){
                header.createCell(i).setCellValue(headers[i]);
            }

            int rowIndex = 1;
            for(CustomerBean c : list){
                Row row = sheet.createRow(rowIndex);
                row.setHeightInPoints(60);

                row.createCell(0).setCellValue(c.getMobileNumber());
                row.createCell(1).setCellValue(c.getName());
                row.createCell(2).setCellValue(c.getAddress());
                row.createCell(3).setCellValue(c.getCity());
                row.createCell(4).setCellValue(c.getEmail());
                row.createCell(5).setCellValue(c.getType());

                // for picture
                byte[] picBytes = c.getProfilePic();
                if(picBytes != null){
                    int pictureIndex = workbook.addPicture(picBytes, Workbook.PICTURE_TYPE_JPEG);
                    ClientAnchor anchor = new XSSFClientAnchor();
                    anchor.setCol1(6); // starting col to draw pic
                    anchor.setRow1(rowIndex); // starting row to draw pic
                    anchor.setCol2(7); // ending col to draw pic
                    anchor.setRow2(rowIndex + 1); // ending row to draw pic
                    drawing.createPicture(anchor, pictureIndex);
                }

                rowIndex++;
            }

            for(int i = 0; i < headers.length - 1; i++){
                sheet.autoSizeColumn(i);
            }
            sheet.setColumnWidth(6, 4000);

            try(FileOutputStream fos = new FileOutputStream(file)){
                workbook.write(fos);
            }

            System.out.println("Exported to Excel Successfully...");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void exportToPdf(ActionEvent event) {
        ObservableList<CustomerBean> list = tableCustomers.getItems();
        if (list == null || list.isEmpty()) {
            System.out.println("No data to export....");
            return;
        }

        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save Customers as PDF");
            chooser.setInitialFileName("Customers.pdf");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

            File file = chooser.showSaveDialog(null);
            if (file == null) {
                return;
            }

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            document.add(new Paragraph("Customer Detail"));

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);

            String[] headers = {"Name", "Mobile", "Email", "Address", "City", "Type"};
            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(header));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(headerCell);
            }

            for (CustomerBean data : list) {
                table.addCell(data.getName());
                table.addCell(String.valueOf(data.getMobileNumber()));
                table.addCell(data.getEmail());
                table.addCell(data.getAddress());
                table.addCell(data.getCity());
                table.addCell(data.getType());
            }

            document.add(table);
            document.close();

            System.out.println("Pdf Created");

        } catch (Exception ep) {
            ep.printStackTrace();
        }
    }

    @FXML
    void doShowCustomers(ActionEvent event) {
        if(comboCustomerType.getSelectionModel().getSelectedIndex() == -1){
            System.out.println("Please select a customer type");
            return;
        }
        String type = comboCustomerType.getSelectionModel().getSelectedItem();
//        tableCustomers.getColumns().clear();
        tableCustomers.setItems(getCustomers(type));
    }

    ObservableList<CustomerBean> getCustomers(String type){
        ObservableList<CustomerBean> list = FXCollections.observableArrayList();
        try{
            PreparedStatement pst;
            if(type.equals("All")){
                pst = con.prepareStatement("select mobileNumber, name, address, city, email, type, profilePic from Customers");
            } else{
                pst = con.prepareStatement("select mobileNumber, name, address, city, email, type, profilePic from Customers where type IN (?, ?)");;
                pst.setString(1, type);
                pst.setString(2, "Both");
            }
            ResultSet res = pst.executeQuery();
            while(res.next()){
                String mobile = res.getString(1);
                String name = res.getString(2);
                String address = res.getString(3);
                String city = res.getString(4);
                String email = res.getString(5);
                String type1 = res.getString(6);
                byte[] profilePic = res.getBytes(7);
                list.add(new CustomerBean(mobile, name, address, city, email, type1, profilePic));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @FXML
    void initialize() {
        assert btnShowCustomers != null : "fx:id=\"btnShowCustomers\" was not injected: check your FXML file 'AllCustomer.fxml'.";
        assert colAddr != null : "fx:id=\"colAddr\" was not injected: check your FXML file 'AllCustomer.fxml'.";
        assert colCity != null : "fx:id=\"colCity\" was not injected: check your FXML file 'AllCustomer.fxml'.";
        assert colEmail != null : "fx:id=\"colEmail\" was not injected: check your FXML file 'AllCustomer.fxml'.";
        assert colMobile != null : "fx:id=\"colMobile\" was not injected: check your FXML file 'AllCustomer.fxml'.";
        assert colName != null : "fx:id=\"colName\" was not injected: check your FXML file 'AllCustomer.fxml'.";
        assert colProfilePic != null : "fx:id=\"colProfilePic\" was not injected: check your FXML file 'AllCustomer.fxml'.";
        assert colType != null : "fx:id=\"colType\" was not injected: check your FXML file 'AllCustomer.fxml'.";
        assert comboCustomerType != null : "fx:id=\"comboCustomerType\" was not injected: check your FXML file 'AllCustomer.fxml'.";
        assert tableCustomers != null : "fx:id=\"tableCustomers\" was not injected: check your FXML file 'AllCustomer.fxml'.";

        colMobile.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
        colAddr.setCellValueFactory(new PropertyValueFactory<>("address"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colProfilePic.setCellValueFactory(new PropertyValueFactory<>("profilePic"));

        colProfilePic.setCellFactory(col -> new TableCell<CustomerBean, byte[]>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(byte[] bytes, boolean empty) {
                super.updateItem(bytes, empty);
                if (empty || bytes == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(new Image(new ByteArrayInputStream(bytes)));
                    imageView.setFitWidth(60);
                    imageView.setFitHeight(60);
                    imageView.setPreserveRatio(true);
                    setGraphic(imageView);
                }
            }
        });

        String[] types = {"All", "Buyer", "Seller", "Both"};
        comboCustomerType.getItems().addAll(types);
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
