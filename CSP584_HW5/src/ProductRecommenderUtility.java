import java.io.*;
import java.sql.*;
import java.io.IOException;
import java.util.*;

public class ProductRecommenderUtility {

    static Connection conn = null;
    static String message;

    public static String getConnection() {

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CSP584?useUnicode=true&characterEncoding=utf-8", "root", "12345678");
            message = "Successfull";
            return message;
        } catch (Exception e) {
            message = "unsuccessful";
            return message;
        }
    }

    public HashMap<String, String> readOutputFile() {

        String TOMCAT_HOME = System.getProperty("catalina.home");
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        HashMap<String, String> prodRecmMap = new HashMap<String, String>();
        try {

            br = new BufferedReader(new FileReader(new File(TOMCAT_HOME + "/webapps/CSP584HW5/matrixFactorizationBasedRecommendations.csv")));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] prod_recm = line.split(cvsSplitBy, 2);
                prodRecmMap.put(prod_recm[0], prod_recm[1]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return prodRecmMap;
    }

    public static Product getProduct(String product) {
        Product prodObj = new Product();
        try {
            String msg = getConnection();
            String selectProd = "select * from Productdetails where Id=?";
            PreparedStatement pst = conn.prepareStatement(selectProd);
            pst.setString(1, product);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                prodObj = new Product(rs.getString("Id"), rs.getString("productName"), rs.getDouble("productPrice"), rs.getString("productImage"), rs.getString("productManufacturer"), rs.getString("productCondition"), rs.getString("ProductType"), rs.getDouble("productDiscount"));
            }
            rs.close();
            pst.close();
            conn.close();
        } catch (Exception e) {
        }
        return prodObj;
    }
}