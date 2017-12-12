package sample.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbUtil {

    private static final String username = "dbdictionary";
    private static final String password = "dictengidn";
    private static final String conn = "jdbc:mysql://localhost/dictionary";
    private static final String sconn = "jdbc:sqlite:dictionary.sqlite";

    public static Connection getConnection() throws SQLException{
        try {
            Class.forName("com.mysql.jdbc.Driver");

            return DriverManager.getConnection("jdbc:mysql://localhost/dictionary", "dbdictionary", "dictengidn");
        } catch (ClassNotFoundException | SQLException ex){
            Logger.getLogger(DbUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
