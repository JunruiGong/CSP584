import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@WebServlet("/Inventory")
public class Inventory extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        displayInventory(request, response, pw);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);


        HashMap<String, Product> hm = new HashMap<String, Product>();
        String TOMCAT_HOME = System.getProperty("catalina.home");

        //get the user details from file
        try {
            hm = MySqlDataStoreUtilities.selectInventory();
        } catch (Exception e) {

        }
    }

    protected void displayInventory(HttpServletRequest request,
                                    HttpServletResponse response, PrintWriter pw)  //error: true代表有错误，false代表没有错误
            throws ServletException, IOException {

        Utilities utility = new Utilities(request, pw);
        utility.printHtml("Header.html");
        utility.printHtml("LeftNavigationBar.html");

        HashMap<String, Product> productMap = new HashMap<String, Product>();

        //Table of Inventory
        pw.print("<div id='content'>");
        pw.print("<div class='post'>");
        pw.print("<h3 class='title'>");
        pw.print("Table of Product Inventory");
        pw.print("</h3>");
        pw.print("<div class='entry'>");

        pw.print("<table class='gridtable'>");
        pw.print("<tr>");
        pw.print("<td>Product Name</td>");
        pw.print("<td>Price</td>");
        pw.print("<td>Inventory</td>");
        pw.print("</tr>");


        try {
            productMap = MySqlDataStoreUtilities.selectInventory();
        } catch (Exception ignored) {

        }

        for (Product product : productMap.values()) {

            pw.print("<tr>");
            pw.print("<td>" + product.getName() + "</td>" +
                    "<td>" + product.getPrice() + "</td>" +
                    "<td>" + product.getInventory() + "</td>");
            pw.print("</tr>");

        }
        pw.print("</table></div></div>");


        //Bar Chart of Inventory
        pw.print("<div id='content'>");
        pw.print("<div class='post'>");
        pw.print("<h3 class='title'>");
        pw.print("Bar Chart of Inventory");
        pw.print("</h3>");
        pw.print("<div class='entry'>");

        pw.print("<table class='gridtable'>");
        pw.print("<tr>");
        pw.print("<td>Product Name</td>");
        pw.print("<td>Price</td>");
        pw.print("<td>Inventory</td>");
        pw.print("</tr>");
//        for (Map.Entry<Integer, ArrayList<OrderPayment>> entry : orderPayments.entrySet()) {
//            for (OrderPayment od : entry.getValue()) {
//
//
//                pw.print("<form method='post' action='RemoveUpdateOrder'>");
//                pw.print("<tr>");
//                pw.print("<td>" + od.getOrderId() + "</td>" +
//                        "<td>" + od.getUserName() + "</td>" +
//                        "<td>" + od.getOrderName() + "</td>" +
//                        "<td>" + od.getOrderPrice() + "</td>" +
//                        "<td>" + od.getUserAddress() + "</td>" +
//                        "<td>" + od.getCreditCardNo() + "</td>");
//                pw.print("</tr>");
//                pw.print("</form>");
//            }
//        }
        pw.print("</table></div></div>");


        //Table of all products currently on sale
        pw.print("<div id='content'>");
        pw.print("<div class='post'>");
        pw.print("<h3 class='title'>");
        pw.print("Table of all products currently on sale");
        pw.print("</h3>");
        pw.print("<div class='entry'>");

        pw.print("<table class='gridtable'>");
        pw.print("<tr>");
        pw.print("<td>Product Name</td>");
        pw.print("</tr>");

        try {
            productMap = MySqlDataStoreUtilities.selectOnSale();
        } catch (Exception ignored) {

        }

        for (Product product : productMap.values()) {

            pw.print("<tr>");
            pw.print("<td>" + product.getName() + "</td>");
            pw.print("</tr>");

        }

        pw.print("</table></div></div>");


        //Table of all products currently that have manufacturer rebates
        pw.print("<div id='content'>");
        pw.print("<div class='post'>");
        pw.print("<h3 class='title'>");
        pw.print("Table of all products currently that have manufacturer rebates");
        pw.print("</h3>");
        pw.print("<div class='entry'>");

        pw.print("<table class='gridtable'>");
        pw.print("<tr>");
        pw.print("<td>Product Name</td>");
        pw.print("<td>Rebate</td>");
        pw.print("</tr>");
        try {
            productMap = MySqlDataStoreUtilities.selectRebate();
        } catch (Exception ignored) {

        }

        for (Product product : productMap.values()) {
            System.out.println(product.getDiscount());

            pw.print("<tr>");
            pw.print("<td>" + product.getName() + "</td>");
            pw.print("<td>" + product.getDiscount() + "</td>");
            pw.print("</tr>");

        }
        pw.print("</table></div></div>");
    }
}
