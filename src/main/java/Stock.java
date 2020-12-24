import java.util.*;
import java.lang.*;
import java.sql.*;
import java.util.Date;

public class Stock {

    private final String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
    private final String user = "postgres";
    private final String password = "Chaman2020";
    ArrayList<Integer> OrderListqty = new ArrayList<Integer>();
    ArrayList<String> OrderListname = new ArrayList<>();

    public Stock() {
    }

    public void check_stock_value(String product_name) throws SQLException {
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

        String sqlStr = "SELECT * FROM shop_product WHERE name = ?";
        PreparedStatement s = c.prepareStatement(sqlStr);
        s.setString(1, product_name);
        ResultSet rset = s.executeQuery();

        Integer qty = rset.getInt("quantity");
        Integer fullstock = rset.getInt("full_stock");

        int percentage = qty / fullstock;

        if (percentage <= 0.2) {
            place_order();
        }

        rset.close();
        s.close();
        c.close();
    }

    public void update_whole_stock() throws SQLException {
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

        String sqlStr = "SELECT * FROM shop_product";
        PreparedStatement s = c.prepareStatement(sqlStr);
        ResultSet rset = s.executeQuery();
        while (rset.next()) {
            int qty = rset.getInt("quantity");
            int fullstock = rset.getInt("full_stock");
            String sqlStr2 = "UPDATE shop_product set quantity = ?";
            PreparedStatement s2 = c.prepareStatement(sqlStr2);
            s2.setInt(1, fullstock);
            ResultSet rset2 = s2.executeQuery();
            rset2.close();
            s2.close();
        }
        rset.close();
        s.close();
        c.close();
    }

    public void place_order() throws SQLException {
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

        String sqlStr2 = "SELECT * FROM shop_product";
        PreparedStatement s2 = c.prepareStatement(sqlStr2);
        ResultSet rset2 = s2.executeQuery();
        while (rset2.next()) {
            String n = rset2.getString("name");
            int q = rset2.getInt("quantity");
            int fs = rset2.getInt("full_stock");
            int amount_to_order = fs - q;

            OrderListname.add(n);
            OrderListqty.add(amount_to_order);

        }
        update_whole_stock();

        rset2.close();
        s2.close();
        c.close();
    }
}
