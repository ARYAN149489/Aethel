package jdbcc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection doConnectToDb(){
        Connection con = null;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Class Loaded Successfully");
            con = DriverManager.getConnection("jdbc:mysql://localhost/javaProj", "root", "123456");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return con;
    }
    public static void main(String[] args){
        doConnectToDb();
    }
}
