package dashboard;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Dashboard {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnLogout;

    @FXML
    void doLogout(ActionEvent event) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/loginpageview/LoginPage.fxml")));
        Scene scene = new Scene(fxmlLoader, 800, 800);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        Scene scPrev = btnLogout.getScene();
        scPrev.getWindow().hide();
    }

    @FXML
    void goToAddDeal(ActionEvent event) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/adddealview/AddDeal.fxml")));
        Scene scene = new Scene(fxmlLoader, 800, 800);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
//        Scene scPrev = btnLogout.getScene();
//        scPrev.getWindow().hide();
    }

    @FXML
    void goToAddProperty(ActionEvent event) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/addpropertyview/AddProperty.fxml")));
        Scene scene = new Scene(fxmlLoader, 800, 800);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
//        Scene scPrev = btnLogout.getScene();
//        scPrev.getWindow().hide();
    }

    @FXML
    void goToAnalytics(ActionEvent event) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/chartview/Chart.fxml")));
        Scene scene = new Scene(fxmlLoader, 800, 800);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
//        Scene scPrev = btnLogout.getScene();
//        scPrev.getWindow().hide();
    }

    @FXML
    void goToManageCustomer(ActionEvent event) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/customerView/CustomerView.fxml")));
        Scene scene = new Scene(fxmlLoader, 800, 800);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
//        Scene scPrev = btnLogout.getScene();
//        scPrev.getWindow().hide();
    }

    @FXML
    void goToUpdateDeal(ActionEvent event) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/updatedealview/UpdateDeal.fxml")));
        Scene scene = new Scene(fxmlLoader, 800, 800);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
//        Scene scPrev = btnLogout.getScene();
//        scPrev.getWindow().hide();
    }

    @FXML
    void goToUpdateProperty(ActionEvent event) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/updatepropertyview/UpdateProperty.fxml")));
        Scene scene = new Scene(fxmlLoader, 800, 800);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
//        Scene scPrev = btnLogout.getScene();
//        scPrev.getWindow().hide();
    }

    @FXML
    void goToViewAllCustomers(ActionEvent event) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/updatepropertyview/UpdateProperty.fxml")));
        Scene scene = new Scene(fxmlLoader, 800, 800);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
//        Scene scPrev = btnLogout.getScene();
//        scPrev.getWindow().hide();
    }

    @FXML
    void goToViewAllDeals(ActionEvent event) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/alldealview/AllDealView.fxml")));
        Scene scene = new Scene(fxmlLoader, 800, 800);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
//        Scene scPrev = btnLogout.getScene();
//        scPrev.getWindow().hide();
    }

    @FXML
    void goToViewAllProperties(ActionEvent event) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/allpropertiesview/AllProperties.fxml")));
        Scene scene = new Scene(fxmlLoader, 800, 800);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
//        Scene scPrev = btnLogout.getScene();
//        scPrev.getWindow().hide();
    }

    @FXML
    void initialize() {
        assert btnLogout != null : "fx:id=\"btnLogout\" was not injected: check your FXML file 'Dashboard.fxml'.";

    }

}
