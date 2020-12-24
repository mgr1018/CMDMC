import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {
    public String name1;
    public Database(){
        Connection c = null;
        Statement stmt = null;
        Statement stmt2 = null;
        Statement stmt3 = null;
        String dbUrl = System.getenv("JDBC_DATABASE_URL");

        try{
            Class.forName("org.postgresql:Driver");
            c = DriverManager.getConnection(dbUrl);
            stmt = c.createStatement();
            String sql = "CREATE TABLE COMPANY" +
                    "(ID INT PRIMARY KEY     NOT NULL),"+
                    "NAME            TEXT    NOT NULL," +
                    "AGE             INT     NOT NULL," +
                    "ADDRESS         CHAR(50)," +
                    "SALARY          REAL)";
            stmt.executeUpdate(sql);
            stmt2 = c.createStatement();
            String sql2 = "INSERT INTO COMPANY (ID, NAME, AGE, ADDRESS, SALARY"+
                    "VALUES (1, 'Paul', 32, 'California', 20000.00);";
            stmt2.executeUpdate(sql2);
            stmt3 = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM COMPANY;");

            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                this.name1 = name;
                int age = rs.getInt("age");
                String address = rs.getString("address");
                float salary = rs.getFloat("salary");
            }
            rs.close();
            stmt.close();
            stmt2.close();
            stmt3.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }
}

