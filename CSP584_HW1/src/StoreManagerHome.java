import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.jws.WebService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet("/StoreManagerHome")
public class StoreManagerHome extends HttpServlet {

    private String error_msg;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        displayStoreManagerHome(request, response, pw, "");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Utilities utility = new Utilities(request, pw);

        Map<String, Object> map = new HashMap<String, Object>(); //保存表单提交的数据(新建product)

        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
        String catalog;
        try {
            List<FileItem> parseRequest = servletFileUpload.parseRequest(request);
            for (FileItem fileItem : parseRequest) {
                boolean formField = fileItem.isFormField();
                if (formField) {
                    //普通表单项
                    String fieldName = fileItem.getFieldName();
                    String fieldValue = fileItem.getString();
                    map.put(fieldName, fieldValue);
                } else {
                    //图片上传项，获得文件名称和内容

                    catalog = String.valueOf(map.get("productCatalog"));
                    String realPath = utility.getRealPath(catalog);

                    String fileName = fileItem.getName();
                    String path = this.getServletContext().getRealPath(realPath);
                    InputStream inputStream = fileItem.getInputStream();
                    OutputStream outputStream = new FileOutputStream(path + "/" + fileName);
                    IOUtils.copy(inputStream, outputStream);
                    inputStream.close();
                    outputStream.close();
                    fileItem.delete();

                    map.put("image", fileName);
                }
            }

            if (utility.storeNewProduct(map)) {
                //添加成功
                error_msg = "Completed!";
                displayStoreManagerHome(request, response, pw, "newProduct");
            } else {
                //添加失败
                error_msg = "Cannot add new product!";
                displayStoreManagerHome(request, response, pw, "newProduct");
            }

        } catch (FileUploadException e) {
            e.printStackTrace();
        }


//        //创建item的表格
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//        String repassword = request.getParameter("repassword");
//
//        //创建order的表格
//        String customerName = request.getParameter("customerName");
//        String itemName = request.getParameter("itemName");
//        //String itemCatalog = request.getParameter("itemCatalog");
//        double totalPrice = 0;
//        if (utility.isContainsStr(request.getParameter("totalPrice"))) {
//            //含有字母，报错
//            error_msg = "Passwords doesn't match!";  //已完成测试
//            displaySalesmanHome(request, response, pw, "order");
//            return;
//        } else {
//            totalPrice = Double.parseDouble(request.getParameter("totalPrice"));
//        }
//        String creditCardNo = request.getParameter("creditCardNo");
//        String customerAddress = request.getParameter("customerAddress");
//
//
//        HashMap<String, User> hm = new HashMap<String, User>();
//        String TOMCAT_HOME = System.getProperty("catalina.home");
//
//        //get the user details from file
//        try {
//            FileInputStream fileInputStream = new FileInputStream(new File(TOMCAT_HOME + "/webapps/CSP584HW1/UserDetails.txt"));
//            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//            hm = (HashMap) objectInputStream.readObject();
//        } catch (Exception e) {
//
//        }
//
//        if (customerName.isEmpty() && itemName.isEmpty()) {
//            //提交的是创建customer的表格
//            if (!password.equals(repassword)) {
//                error_msg = "Passwords doesn't match!";  //已完成测试
//                displaySalesmanHome(request, response, pw, "customer");
//            } else {
//
//                // if the user already exist show error that already exist
//                if (hm.containsKey(username)) {
//                    error_msg = "Username already exist."; //已完成测试
//                    displaySalesmanHome(request, response, pw, "customer");
//                } else {
//                    User user = new User(username, password, "Customer");
//                    hm.put(username, user);
//                    FileOutputStream fileOutputStream = new FileOutputStream(TOMCAT_HOME + "/webapps/CSP584HW1/UserDetails.txt");
//                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
//                    objectOutputStream.writeObject(hm);
//                    objectOutputStream.flush();
//                    objectOutputStream.close();
//                    fileOutputStream.close();
//                    HttpSession session = request.getSession(true);
//                    session.setAttribute("login_msg", "The customer account created successfully.");
//
//                    //创建customer成功
//                    error_msg = "The customer account created successfully.";
//                    displaySalesmanHome(request, response, pw, "customer");  //已完成测试
//                }
//
//            }
//        } else {//提交的是创建订单的表格
//            if (!hm.containsKey(customerName)) { //已完成测试
//                error_msg = "Cannot found this customer.";
//                displaySalesmanHome(request, response, pw, "order");
//            } else {
//                if (utility.isItemExist(productCatalog, itemName)) { //已完成测试  //TODO  判断商品是否存在的功能没有完成
//                    SimpleDateFormat df = new SimpleDateFormat("HHmmss");//设置日期格式
//                    int orderId = Integer.parseInt(df.format(new Date()));  //设置订单号为当前下单时间的时分秒
//                    utility.storePayment(orderId, customerName, totalPrice, customerAddress, creditCardNo);
//                    //创建order成功
//                    error_msg = "The order created successfully.";
//                    displaySalesmanHome(request, response, pw, "order");
//                } else { //已完成测试
//                    error_msg = "Cannot found this item.";
//                    displaySalesmanHome(request, response, pw, "order");
//                }
//            }
//        }
    }

    protected void displayStoreManagerHome(HttpServletRequest request,
                                           HttpServletResponse response, PrintWriter pw, String flag)  //error: true代表有错误，false代表没有错误
            throws ServletException, IOException {

        Utilities utility = new Utilities(request, pw);
        utility.printHtml("Header.html");
        utility.printHtml("LeftNavigationBar.html");

        pw.print("<div id='content'>");
        pw.print("<div class='post'>");
        pw.print("<h3 class='title'>");
        pw.print("Create New product");
        pw.print("</h3>");
        pw.print("<div class='entry'>");

        if (flag.equals("newProduct"))
            pw.print("<h4 style='color:red'>" + error_msg + "</h4>");
        //显示创建product的表格
        pw.print("<form action='StoreManagerHome' method='post' enctype='multipart/form-data'>");
        pw.print("<table style='width:100%'><tr><td>");

        pw.print("<h4>Product ID</h4></td><td><input type='text' name='id' value='' class='input' required></input>");
        pw.print("</td></tr><tr><td>");

        pw.print("<h4>Product Name</h4></td><td><input type='text' name='name' value='' class='input' required></input>");
        pw.print("</td></tr><tr><td>");

        pw.print("<h4>Product Catalog</h4><td><select name='productCatalog' class='input'>" +
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


        pw.print("<h4>Price</h4></td><td><input type='text' name='price' value='' class='input' required></input>");
        pw.print("</td></tr><tr><td>");
        pw.print("<h4>Manufacturer</h4></td><td><input type='text' name='manufacturer' value='' class='input' required></input>");
        pw.print("</td></tr><tr><td>");

        pw.print("<h4>Condition</h4><td><select name='condition' class='input'>" +
                "<option value='New' selected>New</option>" +
                "<option value='Used'>Used</option>" +
                "<option value='Refurbished'>Refurbished</option></select>");
        pw.print("</td></tr></td><tr><td>");

        pw.print("<h4>Discount</h4></td><td><input type='text' name='discount' value='' class='input' required></input>");
        pw.print("</td></tr><tr><td>");


        pw.print("<h4>Image</h4></td><td><img id=\"preview\" /><br/><input type='file' name='image' class='input' required></input>");
        pw.print("</td></tr><tr><td>");


        pw.print("<input type='submit' class='btnbuy' value='Create' style='float: right;height: 20px margin: 20px; margin-right: 10px;'></input>");
        pw.print("</td></tr><tr><td></td><td>");
        pw.print("</td></tr></table>");
        pw.print("</form></div></div>");


        /////////////////////////////////// /////////////////////////////////// /////////////////////////////////// ///////////////////////////////////


        //显示order的详细信息
        HashMap<Integer, ArrayList<OrderPayment>> orderPayments = new HashMap<Integer, ArrayList<OrderPayment>>();
        String TOMCAT_HOME = System.getProperty("catalina.home");
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(TOMCAT_HOME + "/webapps/CSP584HW1/PaymentDetails.txt"));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            orderPayments = (HashMap) objectInputStream.readObject();
        } catch (Exception ignored) {

        }

        pw.print("<div class='post'>");
        pw.print("<h2 class='title meta'>");
        pw.print("<a style='font-size: 24px;'>View Orders</a>");
        pw.print("</h2><div class='entry'>");

//        for (Map.Entry<Integer, ArrayList<OrderPayment>> entry : orderPayments.entrySet()) {
//            for (OrderPayment od : entry.getValue()) {
//
//                pw.print("<table class='gridtable'>");
//                pw.print("<tr>");
//                pw.print("<td> User Name: </td>");
//                pw.print("<td>" + od.getUserName() + "</td>");
//                pw.print("</tr>");
////                pw.print("<tr>");
////
////                pw.print("<tr><td></td>");
//                pw.print("<td>OrderId:</td>");
//                pw.print("<td>UserName:</td>");
//                pw.print("<td>productOrdered:</td>");
//                pw.print("<td>productPrice:</td></tr>");
//
//
//                pw.print("<form method='get' action='ViewOrder'>");
//                //pw.print("<tr>");
//                //pw.print("<td><input type='hidden' name='orderName' value='" + od.getOrderName() + "'></td>");
//                pw.print("<input type='hidden' name='orderName' value='" + od.getOrderName() + "'>");
//                pw.print("<td>" + od.getOrderId() + "</td><td>" + od.getUserName() + "</td><td>" + od.getOrderName() + "</td><td>Price: " + od.getOrderPrice() + "</td>");
//                pw.print("<input type='hidden' name='orderId' value='" + od.getOrderId() + "'>");
//                pw.print("<input type='hidden' name='customerName' value='" + od.getUserName() + "'>");
//                pw.print("<td><input type='submit' name='Order' value='CancelOrder' class='btnbuy'></td>");
//                //pw.print("</tr>");
//                pw.print("</form>");
//                pw.print("<br>");
//            }
//        }
        pw.print("</table>");
//        pw.print("</table>");
        pw.print("</h2></div></div></div>");
    }

}
