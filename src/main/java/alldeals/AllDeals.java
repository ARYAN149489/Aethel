package alldeals;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import jdbcc.DatabaseConnection;

public class AllDeals {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnShowDeals;

    @FXML
    private ComboBox<String> comboDealStatus;

    @FXML
    private DatePicker dpFromDate;

    @FXML
    private DatePicker dpToDate;

    @FXML
    private TableView<DealBean> tableDeals;

    @FXML
    void doShowDeals(ActionEvent event) {
        tableDeals.getColumns().clear();

        if(dpFromDate.getValue() == null || dpToDate.getValue() == null){
            System.out.println("Please select both dates");
            return;
        }
        if(comboDealStatus.getSelectionModel().isEmpty()){
            System.out.println("Please select a deal status");
            return;
        }

        LocalDate from = dpFromDate.getValue();
        LocalDate to = dpToDate.getValue();

        if(from.isAfter(to)){
            System.out.println("From date cannot be after To date");
            return;
        }

        TableColumn<DealBean, String> buyerMobileCol = new TableColumn<>("Buyer Mobile");
        buyerMobileCol.setCellValueFactory(new PropertyValueFactory<DealBean, String>("buyerMobile"));
        buyerMobileCol.setMinWidth(100);

        TableColumn<DealBean, String> buyerNameCol = new TableColumn<>("Buyer Name");
        buyerNameCol.setCellValueFactory(new PropertyValueFactory<DealBean, String>("buyerName"));
        buyerNameCol.setMinWidth(100);

        TableColumn<DealBean, String> sellerMobileCol = new TableColumn<>("Seller Mobile");
        sellerMobileCol.setCellValueFactory(new PropertyValueFactory<DealBean, String>("sellerMobile"));
        sellerMobileCol.setMinWidth(100);

        TableColumn<DealBean, String> sellerNameCol = new TableColumn<>("Seller Name");
        sellerNameCol.setCellValueFactory(new PropertyValueFactory<DealBean, String>("sellerName"));
        sellerNameCol.setMinWidth(100);

        TableColumn<DealBean, String> propertyNameCol = new TableColumn<>("Property Name");
        propertyNameCol.setCellValueFactory(new PropertyValueFactory<DealBean, String>("propertyName"));
        propertyNameCol.setMinWidth(130);

        TableColumn<DealBean, String> finalAmountCol = new TableColumn<>("Final Amount");
        finalAmountCol.setCellValueFactory(new PropertyValueFactory<DealBean, String>("finalAmount"));
        finalAmountCol.setMinWidth(100);

        TableColumn<DealBean, String> myCommissionCol = new TableColumn<>("My Commission");
        myCommissionCol.setCellValueFactory(new PropertyValueFactory<DealBean, String>("myCommission"));
        myCommissionCol.setMinWidth(100);

        TableColumn<DealBean, String> amountLeftCol = new TableColumn<>("Amount Left");
        amountLeftCol.setCellValueFactory(new PropertyValueFactory<DealBean, String>("amountLeft"));
        amountLeftCol.setMinWidth(100);

        TableColumn<DealBean, String> commissionLeftCol = new TableColumn<>("Commission Left");
        commissionLeftCol.setCellValueFactory(new PropertyValueFactory<DealBean, String>("commissionLeft"));
        commissionLeftCol.setMinWidth(110);

        TableColumn<DealBean, String> advGivenDateCol = new TableColumn<>("Adv Given Date");
        advGivenDateCol.setCellValueFactory(new PropertyValueFactory<DealBean, String>("advGivenDate"));
        advGivenDateCol.setMinWidth(100);

        TableColumn<DealBean, String> registryDateCol = new TableColumn<>("Registry Date");
        registryDateCol.setCellValueFactory(new PropertyValueFactory<DealBean, String>("registryDate"));
        registryDateCol.setMinWidth(100);

        TableColumn<DealBean, String> otherInfoCol = new TableColumn<>("Other Info");
        otherInfoCol.setCellValueFactory(new PropertyValueFactory<DealBean, String>("otherInfo"));
        otherInfoCol.setMinWidth(130);

        TableColumn<DealBean, String> dealStatusCol = new TableColumn<>("Deal Status");
        dealStatusCol.setCellValueFactory(new PropertyValueFactory<DealBean, String>("dealStatus"));
        dealStatusCol.setMinWidth(100);

        tableDeals.getColumns().addAll(buyerMobileCol, buyerNameCol, sellerMobileCol, sellerNameCol,
                propertyNameCol, finalAmountCol, myCommissionCol,
                amountLeftCol, commissionLeftCol, advGivenDateCol, registryDateCol, otherInfoCol, dealStatusCol);

        tableDeals.setItems(getAllDeals(Date.valueOf(from), Date.valueOf(to)));
    }

    ObservableList<DealBean> getAllDeals(Date from, Date to){
        ObservableList<DealBean> list = FXCollections.observableArrayList();
        String status = comboDealStatus.getSelectionModel().getSelectedItem();

        try{
            StringBuilder s = new StringBuilder("select buyer_mobile, buyer_name, seller_mobile, seller_name, " +
                    "property_name, final_amount, my_commission, adv_commission, adv_amount, amount_left, " +
                    "commission_left, adv_given_date, registry_date, other_info, deal_status from Deals " +
                    "where registry_date BETWEEN ? AND ?");
            List<Object> params = new ArrayList<>();
            params.add(from);
            params.add(to);

            if(!status.equals("All")){
                s.append(" AND deal_status = ?");
                params.add(status);
            }

            PreparedStatement pst = con.prepareStatement(s.toString());
            for(int i = 0; i < params.size(); ++i){
                pst.setObject(i + 1, params.get(i));
            }

            ResultSet res = pst.executeQuery();
            while (res.next()){
                String buyerMobile = res.getString(1);
                String buyerName = res.getString(2);
                String sellerMobile = res.getString(3);
                String sellerName = res.getString(4);
                String propertyName = res.getString(5);
                String finalAmount = String.valueOf(res.getFloat(6));
                String myCommission = String.valueOf(res.getFloat(7));
                String advCommission = String.valueOf(res.getFloat(8));
                String advAmount = String.valueOf(res.getFloat(9));
                String amountLeft = String.valueOf(res.getFloat(10));
                String commissionLeft = String.valueOf(res.getFloat(11));
                String advGivenDate = String.valueOf(res.getDate(12));
                String registryDate = String.valueOf(res.getDate(13));
                String otherInfo = res.getString(14);
                String dealStatus = res.getString(15);

                DealBean d = new DealBean(buyerMobile, buyerName, sellerMobile, sellerName, propertyName,
                        finalAmount, myCommission, amountLeft, commissionLeft,
                        advGivenDate, registryDate, otherInfo, dealStatus);
                list.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @FXML
    void exportToPdf(ActionEvent event) {
        try {

            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save Deals data as Pdf");
            chooser.setInitialFileName("Deals.pdf");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pdf Files", "*.pdf"));
            File file = chooser.showSaveDialog(null);
            if(file == null)
                return;
            Document document = new Document();
            PdfWriter.getInstance(document,new FileOutputStream(file));

            document.open();
            document.add(new Paragraph("Deal Detail"));
            PdfPTable table = new PdfPTable(9);
            table.addCell("Buyer Mobile");
            table.addCell("Buyer Name");
            table.addCell("Seller Mobile");
            table.addCell("Seller Name");
            table.addCell("Property Name");
            table.addCell("Final Amount");
            table.addCell("Registry Date");
            table.addCell("Deal Status");
            table.addCell("Other Info");

            for(DealBean data: tableDeals.getItems())
            {
                table.addCell(data.getBuyerMobile());
                table.addCell(data.getBuyerName());
                table.addCell(data.getSellerMobile());
                table.addCell(data.getSellerName());
                table.addCell(data.getPropertyName());
                table.addCell(data.getFinalAmount());
                table.addCell(data.getRegistryDate());
                table.addCell(data.getDealStatus());
                table.addCell(data.getOtherInfo());
            }

            document.add(table);
            document.close();

            System.out.println("Pdf. Created");

        }
        catch(Exception ep)
        {
            ep.printStackTrace();
        }

    }

    @FXML
    void initialize() {
        assert btnShowDeals != null : "fx:id=\"btnShowDeals\" was not injected: check your FXML file 'AllDeals.fxml'.";
        assert comboDealStatus != null : "fx:id=\"comboDealStatus\" was not injected: check your FXML file 'AllDeals.fxml'.";
        assert dpFromDate != null : "fx:id=\"dpFromDate\" was not injected: check your FXML file 'AllDeals.fxml'.";
        assert dpToDate != null : "fx:id=\"dpToDate\" was not injected: check your FXML file 'AllDeals.fxml'.";
        assert tableDeals != null : "fx:id=\"tableDeals\" was not injected: check your FXML file 'AllDeals.fxml'.";

        doConnect();

        String[] statuses = {"All", "Ongoing", "Completed", "Cancelled"};
        comboDealStatus.getItems().addAll(statuses);
        comboDealStatus.getSelectionModel().select(0);
    }

    Connection con;
    void doConnect(){
        con = DatabaseConnection.doConnectToDb();
        if(con == null){
            System.out.println("Database Connection error..");
        } else{
            System.out.println("Database connected Successfully");
        }
    }

}