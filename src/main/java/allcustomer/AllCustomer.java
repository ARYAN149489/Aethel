package allcustomer;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import java.util.List;
import java.util.ArrayList;

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
        if(list == null || list.isEmpty()){
            System.out.println("No data to export....");
            return;
        }

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save Customers as PDF");
        chooser.setInitialFileName("Customers.pdf");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = chooser.showSaveDialog(null);
        if(file == null)
            return;

        PDType1Font helvetica = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
        PDType1Font helveticaBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

        float leftMargin = 40;
        float rightEdge = 555;
        float topMargin = 780;
        float bottomMargin = 50;

        float photoSize = 70;
        float photoGap = 15;
        float textX = leftMargin + photoSize + photoGap;
        float textWidth = rightEdge - textX;
        float lineHeight = 14;
        float cardPadding = 12;
        float cardSpacing = 14;
        int fontSize = 10;

        try(PDDocument document = new PDDocument()){
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDPageContentStream content = new PDPageContentStream(document, page);

            content.beginText();
            content.setFont(helveticaBold, 16);
            content.newLineAtOffset(leftMargin, topMargin);
            content.showText("Customer List");
            content.endText();

            float y = topMargin - 30;

            for(CustomerBean c : list){

                List<String> lines = new ArrayList<>();
                lines.add("Name: " + safe(c.getName()));
                lines.add("Mobile: " + safe(c.getMobileNumber()));
                lines.addAll(wrapText("Email: " + safe(c.getEmail()), helvetica, fontSize, textWidth));
                lines.addAll(wrapText("Address: " + safe(c.getAddress()), helvetica, fontSize, textWidth));
                lines.add("City: " + safe(c.getCity()));
                lines.add("Type: " + safe(c.getType()));

                float textBlockHeight = lines.size() * lineHeight;
                float cardContentHeight = Math.max(textBlockHeight, photoSize);
                float cardHeight = cardContentHeight + (cardPadding * 2);

                if(y - cardHeight < bottomMargin){
                    content.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    content = new PDPageContentStream(document, page);
                    y = topMargin;
                }

                float cardTop = y;
                float cardBottom = y - cardHeight;

                content.setLineWidth(0.7f);
                content.addRect(leftMargin, cardBottom, rightEdge - leftMargin, cardHeight);
                content.stroke();

                byte[] picBytes = c.getProfilePic();
                if(picBytes != null){
                    PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, picBytes, "profilePic");
                    float imgW = pdImage.getWidth();
                    float imgH = pdImage.getHeight();
                    float scale = Math.min(photoSize / imgW, photoSize / imgH);
                    float drawW = imgW * scale;
                    float drawH = imgH * scale;
                    float imgX = leftMargin + (photoSize - drawW) / 2f;
                    float imgY = cardTop - cardPadding - (photoSize + drawH) / 2f;
                    content.drawImage(pdImage, imgX, imgY, drawW, drawH);
                }

                float textY = cardTop - cardPadding - fontSize;
                for(String line : lines){
                    content.beginText();
                    content.setFont(helvetica, fontSize);
                    content.newLineAtOffset(textX, textY);
                    content.showText(line);
                    content.endText();
                    textY -= lineHeight;
                }

                y -= (cardHeight + cardSpacing);
            }

            content.close();
            document.save(file);
            System.out.println("Exported to PDF Successfully...");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> wrapText(String text, PDType1Font font, float fontSize, float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        if(text == null || text.isEmpty()){
            lines.add("");
            return lines;
        }

        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for(String word : words){
            String candidate = currentLine.length() == 0 ? word : currentLine + " " + word;
            float candidateWidth = font.getStringWidth(candidate) / 1000f * fontSize;

            if(candidateWidth <= maxWidth){
                currentLine = new StringBuilder(candidate);
            } else{
                if(currentLine.length() > 0){
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder();
                }
                // word itself is wider than the column - force-break it character by character
                if(font.getStringWidth(word) / 1000f * fontSize > maxWidth){
                    StringBuilder piece = new StringBuilder();
                    for(char ch : word.toCharArray()){
                        String testPiece = piece.toString() + ch;
                        if(font.getStringWidth(testPiece) / 1000f * fontSize > maxWidth){
                            lines.add(piece.toString());
                            piece = new StringBuilder();
                        }
                        piece.append(ch);
                    }
                    currentLine = new StringBuilder(piece.toString());
                } else{
                    currentLine = new StringBuilder(word);
                }
            }
        }
        if(currentLine.length() > 0)
            lines.add(currentLine.toString());

        return lines;
    }

    private String safe(String s){
        return s == null ? "" : s;
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
