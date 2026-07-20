package loginpage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import jdbcc.DatabaseConnection;
import myalert.MyAlert;

public class LoginPage {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnLogin;

    @FXML
    private ImageView imgLogo;

    @FXML
    private PasswordField txtPassword;

    @FXML
    void doLogin(ActionEvent event) {
        try{
            PreparedStatement pst = con.prepareStatement("select 1 from mainUser where password = ?");
            pst.setString(1, txtPassword.getText());
            ResultSet res = pst.executeQuery();
            if(res.next()){
                // System.out.println("Password verified...");
                Parent fxmlLoader = FXMLLoader.load(getClass().getResource("/dashboardview/Dashboard.fxml"));
                Scene scene = new Scene(fxmlLoader, 800, 800);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                Scene scPrev = btnLogin.getScene();
                scPrev.getWindow().hide();
            } else{
                // System.out.println("Wrong password");
                MyAlert.alertMsg("The password you entered is incorrect. Please try again.", Alert.AlertType.ERROR, "Authentication Failed", "Login Error");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            MyAlert.alertMsg("Error during login authentication: " + e.getMessage(), Alert.AlertType.ERROR, "Login Error", "System Error");
        }
    }

    @FXML
    void initialize() {
        assert btnLogin != null : "fx:id=\"btnLogin\" was not injected: check your FXML file 'LoginPage.fxml'.";
        assert imgLogo != null : "fx:id=\"imgLogo\" was not injected: check your FXML file 'LoginPage.fxml'.";
        assert txtPassword != null : "fx:id=\"txtPassword\" was not injected: check your FXML file 'LoginPage.fxml'.";
        doConnect();
    }
    Connection con;
    void  doConnect(){
        con = DatabaseConnection.doConnectToDb();
        if(con == null){
            // System.out.println("Database connection error...");
            MyAlert.alertMsg("Could not connect to MySQL database. Please verify server status.", Alert.AlertType.ERROR, "Database Connection Failed", "Connection Error");
        } else{
            System.out.println("Database connected successfully");
        }
    }

}
