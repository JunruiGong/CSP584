import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;

@WebServlet("/RemoveUpdateOrder")
public class RemoveUpdateOrder extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        Utilities utility = new Utilities(request, pw);


        int orderId = Integer.parseInt(request.getParameter("orderId"));
        String username = request.getParameter("username");
        String productName = request.getParameter("productName");
        String price = request.getParameter("price");
        String address = request.getParameter("address");
        String creditCard = request.getParameter("creditCard");


        if (request.getParameter("Order") != null && request.getParameter("Order").equals("Cancel")) {
            //Cancel Order
            if (utility.cancelOrder(orderId)) {
                response.sendRedirect("SalesmanHome");
            }

        } else if (request.getParameter("Order") != null && request.getParameter("Order").equals("Update")) {
            //Update Order

            utility.printHtml("Header.html");
            utility.printHtml("LeftNavigationBar.html");

            pw.print("<div id='content'>");
            pw.print("<div class='post'>");
            pw.print("<h3 class='title'>");
            pw.print("Update Order");
            pw.print("</h3>");
            pw.print("<div class='entry'>");

            //显示更新order的表格   //TODO  order的更新和删除
            pw.print("<form action='UpdateOrder' method='post'");
            pw.print("<table style='width:100%'><tr><td>");

            pw.print("<h4>Order ID: " + orderId + "</h4></td>");
            pw.print("</tr><tr><td>");
//            pw.print("<input type='hidden' name='username' value='" + username + "'>");
//            pw.print("<input type='hidden' name='catalog' value='" + catalog + "'>");
//            pw.print("<input type='hidden' name='image' value='" + image + "'>");

            pw.print("<h4>Username</h4></td><td><input type='text' name='productName' value='" + name + "' class='input' required></input>");
            pw.print("</td></tr><tr><td>");

            pw.print("<h4>Product Catalog</h4><td><select id='catalog' name='productCatalog' class='input'>" +
                    "<option value='FitnessWatch' selected>Fitness watch</option>" +
                    "<option value='SmartWatch'>Smart watch</option>" +
                    "<option value='Headphone'>Headphone</option>" +
                    "<option value='VirtualReality'>Virtual reality</option>" +
                    "<option value='PetTracker'>Pet tracker</option>" +
                    "<option value='Phone'>Phone</option>" +
                    "<option value='Laptop'>Laptop</option>" +
                    "<option value='VoiceAssistant'>Voice assistant</option>" +
                    "<option value='Accessory'>Accessory</option></select>");
            pw.print("</td></tr></td><tr><td>");


            pw.print("<h4>Price</h4></td><td><input type='text' name='price' value='" + price + "' class='input' required></input>");
            pw.print("</td></tr><tr><td>");
            pw.print("<h4>Manufacturer</h4></td><td><input type='text' name='manufacturer' value='" + manufacturer + "' class='input' required></input>");
            pw.print("</td></tr><tr><td>");

            pw.print("<h4>Condition</h4><td><select name='condition' class='input'>" +
                    "<option value='New' selected>New</option>" +
                    "<option value='Used'>Used</option>" +
                    "<option value='Refurbished'>Refurbished</option></select>");
            pw.print("</td></tr></td><tr><td>");

            pw.print("<h4>Discount</h4></td><td><input type='text' name='discount' value='" + discount + "' class='input' required></input>");
            pw.print("</td></tr><tr><td>");

            pw.print("<input type='submit' class='btnbuy' value='Update' style='float: right;height: 20px margin: 20px; margin-right: 10px;'></input>");
            pw.print("</td></tr><tr><td></td><td>");
            pw.print("</td></tr></table>");
            pw.print("</form></div></div></div>");
        }
    }
}
