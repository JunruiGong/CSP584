import com.sun.tools.corba.se.idl.constExpr.Or;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/SalesReport")
public class SalesReport extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        displaySalesReport(request, response, pw);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);


        HashMap<String, User> hm = new HashMap<String, User>();
        String TOMCAT_HOME = System.getProperty("catalina.home");

        //get the user details from file
        try {
            hm = MySqlDataStoreUtilities.selectUser();
        } catch (Exception e) {

        }
    }

    protected void displaySalesReport(HttpServletRequest request,
                                      HttpServletResponse response, PrintWriter pw)
            throws ServletException, IOException {

        Utilities utility = new Utilities(request, pw);
        utility.printHtml("Header.html");
        utility.printHtml("LeftNavigationBar.html");

        HashMap<String, OrderPayment> orderPaymentHashMap = new HashMap<String, OrderPayment>();

        //Table of all product sold
        pw.print("<div id='content'>");
        pw.print("<div class='post'>");
        pw.print("<h3 class='title'>");
        pw.print("Table of Product Sold");
        pw.print("</h3>");
        pw.print("<div class='entry'>");

        pw.print("<table class='gridtable'>");
        pw.print("<tr>");
        pw.print("<td>Product Name</td>");
        pw.print("<td>Price</td>");
        pw.print("<td>Sold Amount</td>");
        pw.print("</tr>");

        try {
            orderPaymentHashMap = MySqlDataStoreUtilities.selectSaleAmount();
        } catch (Exception ignored) {

        }

        for (OrderPayment orderPayment : orderPaymentHashMap.values()) {

            pw.print("<tr>");
            pw.print("<td>" + orderPayment.getOrderName() + "</td>" +
                    "<td>" + orderPayment.getOrderPrice() + "</td>" +
                    "<td>" + orderPayment.getSaleAmount() + "</td>");
            pw.print("</tr>");

        }
        pw.print("</table></div></div>");


        //Bar Chart of that shows the product names and the total sales for every product
        pw.print("<div id='content'>");
        pw.print("<div class='post'>");
        pw.print("<h3 class='title'>");
        pw.print("Bar Chart of Product Sold");
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


        //Table of total daily sales transactions
        pw.print("<div id='content'>");
        pw.print("<div class='post'>");
        pw.print("<h3 class='title'>");
        pw.print("Table of Daily Sales Transactions");
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
    }
}
