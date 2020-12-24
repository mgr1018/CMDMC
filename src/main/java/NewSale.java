import java.util.*;
import java.lang.*;
import java.sql.*;
import java.util.Date;
import java.awt.*;

public class NewSale extends Stock{

    private final String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
    private final String user = "postgres";
    private final String password = "Chaman2020";
    ArrayList<Double> DailyProfit = new ArrayList<Double>();
    Stock stock = new Stock();

//This function should be called whenever an item's barcode has been scanned
//It adds the price of the item to the daily profit, subtracts one to the running stock quantity of the item
// then checks if the item is below the hard min value, and if it is, the order to the wholesaler is placed
    public NewSale(int bcd){
        Connection c = null;
        Double p = null;
        Integer q = null;
        String n = null;
        try {
            // Registers the driver
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
        }

        try {
            c = DriverManager.getConnection(dbUrl, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            String sqlStr = "SELECT * FROM shop_product WHERE barcode = ?";
            PreparedStatement s = c.prepareStatement(sqlStr);
            s.setInt(1,bcd);
            ResultSet rset = s.executeQuery();

            p = rset.getDouble("sell_price");
            q = rset.getInt("quantity");
            n = rset.getString("name");

            rset.close();
            s.close();

            String sqlStr2 = "UPDATE shop_product set quantity = ?  WHERE barcode = ?";
            PreparedStatement s2 = c.prepareStatement(sqlStr2);
            s2.setInt(1,q-1);
            s2.setInt(2,bcd);
            ResultSet rset2 = s2.executeQuery();
            rset2.close();
            s2.close();


            c.close();

            DailyProfit.add(p);
            stock.check_stock_value(n);

        }
        catch (Exception e){
        }
    }
// This function updates the daily profit database
    //it should only be called once at the end of the day
    public Double get_daily_profit() throws SQLException{

        Double p = 0.0;
        for (int i = 0; i <= DailyProfit.size(); i++){
            p += DailyProfit.get(i);
        }

        Connection c = null;

        try {
            // Registers the driver
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
        }

        try {
            c = DriverManager.getConnection(dbUrl, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sqlStr = "UPDATE daily_profit set profit = ? ";
        PreparedStatement s = c.prepareStatement(sqlStr);
        s.setDouble(1,p);
        ResultSet rset = s.executeQuery();
        rset.close();
        s.close();

        Date date = new Date();
        String sqlStr2 = "UPDATE daily_profit set date = ? ";
        PreparedStatement s2 = c.prepareStatement(sqlStr2);
        s2.setString(1,date.toString());
        ResultSet rset2 = s2.executeQuery();
        rset2.close();
        s2.close();

        c.close();

        return p;
    }
}
