import java.sql.*;

public class DatabaseHelper {
    static  Connection connection;

    static {
        String jdbcDriver = "com.mysql.cj.jdbc.Driver";
        String username = "root";
        String password = "";
        String serverUrl = "jdbc:mysql://localhost:3306/stockRecommender";


        try {
            //check jdbc driver (mysql connector / j). Make sure the connector is configured correctly (added to libraries) before checking it.
            Class.forName(jdbcDriver);
            System.out.println("Driver Loaded");
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver Failed To Load");
            System.out.println(ex.getMessage());
        }

        try {
            //connecting to xampp server (Apache Server)
            connection = DriverManager.getConnection(serverUrl, username, password);
            System.out.println("Connected To Server Successfully");
        } catch (SQLException ex) {
            System.out.println("Failed To Connect To Server Successfully");
            System.out.println(ex.getMessage());

        }

    }


    /**
     * @param stockName
     * @return -1 if error, 0 if , 1 if , 2if
     */
    public static int getStockStatus(String stockName) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("Select * from stocks where stockName = '"+stockName+"'");
            if (rs.next()){
                return rs.getInt("status");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return  -1;
    }

    public static boolean isStockPresent(String stockName) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("Select * from stocks where stockName = '"+stockName+"'");
            if (rs.next()){
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return  false;
    }

    public static boolean updateStockStatus(String stockName, int status){
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate("UPDATE  stocks set status = "+status+" where stockName = '"+stockName+"'");
            return  true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return  false;
    }

    public static boolean insertStock(String stockName, int currentStatus) {
        Statement stmt = null;
        try {
            connection.createStatement()
                .executeUpdate("INSERT into stocks (stockName,status) values ('"+stockName+"' ,'"+currentStatus+"')");
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}
