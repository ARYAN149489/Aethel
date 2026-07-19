module com.example.javaproj {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires kotlin.stdlib;
    requires annotations;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.apache.pdfbox;
    requires com.github.librepdf.openpdf;

    opens com.example.javaproj to javafx.fxml;
    exports com.example.javaproj;

    opens customer to javafx.fxml;
    exports customer;

    opens listproperties to javafx.fxml;
    exports listproperties;

    opens addproperty to javafx.fxml;
    exports addproperty;

    opens updateproperty to javafx.fxml;
    exports updateproperty;

    opens adddeal to javafx.fxml;
    exports adddeal;

    opens updatedeal to javafx.fxml;
    exports updatedeal;

    opens allcustomer to javafx.fxml;
    exports allcustomer;

    opens jdbcc to javafx.fxml;
    exports jdbcc;

    opens allproperties to javafx.fxml;
    exports allproperties;

    opens alldeals to javafx.fxml;
    exports alldeals;
}