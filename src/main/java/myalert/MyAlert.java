package myalert;

import javafx.scene.control.Alert;

public class MyAlert {
    public static void alertMsg(String msg, Alert.AlertType type, String headerTextForAlert, String titleForAlert){
        Alert alert = new Alert(type);
        alert.setTitle(titleForAlert);
        alert.setHeaderText(headerTextForAlert);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
