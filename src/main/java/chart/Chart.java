package chart;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import jdbcc.DatabaseConnection;
import myalert.MyAlert;

public class Chart {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BarChart<String, Integer> barChart;

    @FXML
    private PieChart pieChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    void showPie(){
        try{
            PreparedStatement pst = con.prepareStatement("select type, count(*) from Customers group by type");
            ResultSet res = pst.executeQuery();
            ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
            while(res.next()){
                data.add(new PieChart.Data(res.getString(1), res.getInt(2)));
            }
            pieChart.setData(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void showBar(){
        try{
            PreparedStatement pst = con.prepareStatement("select deal_status, count(*) from Deals group by deal_status");
            ResultSet res = pst.executeQuery();
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            series.setName("Deal Status");
            while (res.next()){
                series.getData().add(new XYChart.Data<>(res.getString(1), res.getInt(2)));
            }
            barChart.getData().addAll(series);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        assert barChart != null : "fx:id=\"barChart\" was not injected: check your FXML file 'Chart.fxml'.";
        assert pieChart != null : "fx:id=\"pieChart\" was not injected: check your FXML file 'Chart.fxml'.";
        assert xAxis != null : "fx:id=\"xAxis\" was not injected: check your FXML file 'Chart.fxml'.";
        assert yAxis != null : "fx:id=\"yAxis\" was not injected: check your FXML file 'Chart.fxml'.";
        doConnect();
        showPie();
        showBar();
    }
    Connection con;
    void  doConnect(){
        con = DatabaseConnection.doConnectToDb();
        if(con == null){
            // System.out.println("Database connection error...");
            MyAlert.alertMsg("Could not connect to MySQL database for loading chart analytics.", Alert.AlertType.ERROR, "Database Connection Failed", "Connection Error");
        } else{
            System.out.println("Database connected successfully");
        }
    }

}
