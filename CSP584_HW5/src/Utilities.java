import java.awt.image.LookupOp;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

@WebServlet("/Utilities")

/* 
	Utilities class contains class variables of type HttpServletRequest, PrintWriter,String and HttpSession.

	Utilities class has a constructor with  HttpServletRequest, PrintWriter variables.
	  
*/

public class Utilities extends HttpServlet {
    HttpServletRequest req;
    PrintWriter pw;
    String url;
    HttpSession session;

    public Utilities(HttpServletRequest req, PrintWriter pw) {
        this.req = req;
        this.pw = pw;
        this.url = this.getFullURL();
        this.session = req.getSession(true);
    }



	/*  Printhtml Function gets the html file name as function Argument, 
		If the html file name is Header.html then It gets Username from session variables.
		Account ,Cart Information ang Logout Options are Displayed*/

    public void printHtml(String file) {
        String result = HtmlToString(file);
        //to print the right navigation in header of username cart and logout etc
        if (file.equals("Header.html")) {
            result = result + "<div id='menu' style='float: right;'><ul>";
            if (session.getAttribute("username") != null) {
                String username = session.getAttribute("username").toString();
                username = Character.toUpperCase(username.charAt(0)) + username.substring(1);

                String userType = session.getAttribute("userType").toString();
                switch (userType) {
                    case "Customer":
                        result = result + "<li><a><span class='glyphicon'>Hello, " + username + "</span></a></li>"
                                + "<li><a href='Account'><span class='glyphicon'>Account</span></a></li>"

                                + "<li><a href='Logout'><span class='glyphicon'>Logout</span></a></li>";
                        break;
                    case "StoreManager":
                        result = result + "<li><a href='StoreManagerHome'><span class='glyphicon'>ViewProduct</span></a></li>"
                                + "<li><a><span class='glyphicon'>Hello, " + username + "</span></a></li>"
                                + "<li><a href='DataVisualization'><span class='glyphicon'>Trending</span></a></li>"
                                + "<li><a href='DataAnalytics'><span class='glyphicon'>DataAnalytics</span></a></li>"
                                + "<li><a href='Inventory'><span class='glyphicon'>Inventory</span></a></li>"
                                + "<li><a href='SalesReport'><span class='glyphicon'>SalesReport</span></a></li>"
                                + "<li><a href='Logout'><span class='glyphicon'>Logout</span></a></li>";
                        break;

                    case "Salesman":
                        result = result + "<li><a href='SalesmanHome'><span class='glyphicon'>ViewOrder</span></a></li>"
                                + "<li><a><span class='glyphicon'>Hello, " + username + "</span></a></li>"
                                + "<li><a href='Logout'><span class='glyphicon'>Logout</span></a></li>";
                        break;
                }
            } else
                result = result + "<li><a href='ViewOrder'><span class='glyphicon'>ViewOrder</span></a></li>" + "<li><a href='Login'><span class='glyphicon'>Login</span></a></li>";
            result = result + "<li><a href='Cart'><span class='glyphicon'>Cart(" + CartCount() + ")</span></a></li></ul></div></div><div id='page'>";
            pw.print(result);
        } else
            pw.print(result);
    }

    /*  getFullURL Function - Reconstructs the URL user request  */

    public String getFullURL() {
        String scheme = req.getScheme();
        String serverName = req.getServerName();
        int serverPort = req.getServerPort();
        String contextPath = req.getContextPath();
        StringBuffer url = new StringBuffer();
        url.append(scheme).append("://").append(serverName);

        if ((serverPort != 80) && (serverPort != 443)) {
            url.append(":").append(serverPort);
        }
        url.append(contextPath);
        url.append("/");
        return url.toString();
    }

    /*  HtmlToString - Gets the Html file and Converts into String and returns the String.*/
    public String HtmlToString(String file) {
        String result = null;
        try {
            String webPage = url + file;
            URL url = new URL(webPage);
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuffer sb = new StringBuffer();
            while ((numCharsRead = isr.read(charArray)) > 0) {
                sb.append(charArray, 0, numCharsRead);
            }
            result = sb.toString();
        } catch (Exception ignored) {
        }
        return result;
    }

    /*  logout Function removes the username , usertype attributes from the session variable*/

    public void logout() {
        session.removeAttribute("username");
        session.removeAttribute("usertype");
    }

    /*  logout Function checks whether the user is loggedIn or Not*/

    public boolean isLoggedin() {
        return session.getAttribute("username") != null;
    }

    public boolean isItemExist(String itemCatalog, String itemName) {

        HashMap<String, Object> hm = new HashMap<String, Object>();

        switch (itemCatalog) {
            case "FitnessWatch":
                hm.putAll(SaxParserDataStore.fitnessWatchHashMap);
                break;
            case "SmartWatch":
                hm.putAll(SaxParserDataStore.smartWatchHashMap);
                break;
            case "VirtualReality":
                hm.putAll(SaxParserDataStore.virtualRealityHashMap);
                break;
            case "PetTracker":
                hm.putAll(SaxParserDataStore.petTrackerHashMap);
                break;
            case "Headphone":
                hm.putAll(SaxParserDataStore.headphoneHashMap);
                break;
            case "Phone":
                hm.putAll(SaxParserDataStore.phoneHashMap);
                break;
            case "Laptop":
                hm.putAll(SaxParserDataStore.laptopHashMap);
                break;
            case "VoiceAssistant":
                hm.putAll(SaxParserDataStore.voiceAssistantHashMap);
                break;
            case "Accessory":
                hm.putAll(SaxParserDataStore.accessories);
                break;
        }
        return true;
    }

    public String getRealPath(String catalog) {
        String realPath = "images";
        switch (catalog) {
            case "FitnessWatch":
                realPath = realPath + "/fitnessWatch";
                break;
            case "SmartWatch":
                realPath = realPath + "/smartWatch";
                break;
            case "VirtualReality":
                realPath = realPath + "/virtualReality";
                break;
            case "PetTracker":
                realPath = realPath + "/petTracker";
                break;
            case "Headphone":
                realPath = realPath + "/headphone";
                break;
            case "Phone":
                realPath = realPath + "/phone";
                break;
            case "Laptop":
                realPath = realPath + "/laptop";
                break;
            case "VoiceAssistant":
                realPath = realPath + "/voiceAssistant";
                break;
            case "Accessory":
                realPath = realPath + "/accessory";
                break;
        }

        return realPath;
    }

    public boolean storeNewProduct(Map<String, Object> map) {
        String id = String.valueOf(map.get("id"));
        String name = String.valueOf(map.get("name"));
        double price = Double.parseDouble(String.valueOf(map.get("price")));
        String image = String.valueOf(map.get("image"));
        String retailer = String.valueOf(map.get("manufacturer"));
        String condition = String.valueOf(map.get("condition"));
        double discount = Double.parseDouble(String.valueOf(map.get("discount")));
        String catalog = String.valueOf(map.get("productCatalog"));

        switch (catalog) {
            case "FitnessWatch":
                FitnessWatch fitnessWatch = new FitnessWatch();
                fitnessWatch.setId(id);
                fitnessWatch.setName(name);
                fitnessWatch.setPrice(price);
                fitnessWatch.setImage(image);
                fitnessWatch.setRetailer(retailer);
                fitnessWatch.setCondition(condition);
                fitnessWatch.setDiscount(discount);
                SaxParserDataStore.fitnessWatchHashMap.put(id, fitnessWatch);
                return true;
            case "SmartWatch":
                SmartWatch smartWatch = new SmartWatch();
                smartWatch.setId(id);
                smartWatch.setName(name);
                smartWatch.setPrice(price);
                smartWatch.setImage(image);
                smartWatch.setRetailer(retailer);
                smartWatch.setCondition(condition);
                smartWatch.setDiscount(discount);
                SaxParserDataStore.smartWatchHashMap.put(id, smartWatch);
                return true;
            case "VirtualReality":
                VirtualReality virtualReality = new VirtualReality();
                virtualReality.setId(id);
                virtualReality.setName(name);
                virtualReality.setPrice(price);
                virtualReality.setImage(image);
                virtualReality.setRetailer(retailer);
                virtualReality.setCondition(condition);
                virtualReality.setDiscount(discount);
                SaxParserDataStore.virtualRealityHashMap.put(id, virtualReality);
                return true;
            case "PetTracker":
                PetTracker petTracker = new PetTracker();
                petTracker.setId(id);
                petTracker.setName(name);
                petTracker.setPrice(price);
                petTracker.setImage(image);
                petTracker.setRetailer(retailer);
                petTracker.setCondition(condition);
                petTracker.setDiscount(discount);
                SaxParserDataStore.petTrackerHashMap.put(id, petTracker);
                return true;
            case "Headphone":
                Headphone headphone = new Headphone();
                headphone.setId(id);
                headphone.setName(name);
                headphone.setPrice(price);
                headphone.setImage(image);
                headphone.setRetailer(retailer);
                headphone.setCondition(condition);
                headphone.setDiscount(discount);
                SaxParserDataStore.headphoneHashMap.put(id, headphone);
                return true;
            case "Phone":
                Phone phone = new Phone();
                phone.setId(id);
                phone.setName(name);
                phone.setPrice(price);
                phone.setImage(image);
                phone.setRetailer(retailer);
                phone.setCondition(condition);
                phone.setDiscount(discount);
                SaxParserDataStore.phoneHashMap.put(id, phone);
                return true;
            case "Laptop":
                Laptop laptop = new Laptop();
                laptop.setId(id);
                laptop.setName(name);
                laptop.setPrice(price);
                laptop.setImage(image);
                laptop.setRetailer(retailer);
                laptop.setCondition(condition);
                laptop.setDiscount(discount);
                SaxParserDataStore.laptopHashMap.put(id, laptop);
                return true;
            case "VoiceAssistant":
                VoiceAssistant voiceAssistant = new VoiceAssistant();
                voiceAssistant.setId(id);
                voiceAssistant.setName(name);
                voiceAssistant.setPrice(price);
                voiceAssistant.setImage(image);
                voiceAssistant.setRetailer(retailer);
                voiceAssistant.setCondition(condition);
                voiceAssistant.setDiscount(discount);
                SaxParserDataStore.voiceAssistantHashMap.put(id, voiceAssistant);
                return true;
            case "Accessory":
                Accessory accessory = new Accessory();
                accessory.setId(id);
                accessory.setName(name);
                accessory.setPrice(price);
                accessory.setImage(image);
                accessory.setRetailer(retailer);
                accessory.setCondition(condition);
                accessory.setDiscount(discount);
                SaxParserDataStore.accessories.put(id, accessory);
                return true;
        }
        return false;
    }

    public boolean removeProduct(String productId, String catalog) {
        switch (catalog) {
            case "Fitness Watch":
                SaxParserDataStore.fitnessWatchHashMap.remove(productId);
                return true;
            case "Smart Watch":

                SaxParserDataStore.smartWatchHashMap.remove(productId);
                return true;
            case "Virtual Reality":

                SaxParserDataStore.virtualRealityHashMap.remove(productId);
                return true;
            case "Pet Tracker":

                SaxParserDataStore.petTrackerHashMap.remove(productId);
                return true;
            case "Headphone":

                SaxParserDataStore.headphoneHashMap.remove(productId);
                return true;
            case "Phone":

                SaxParserDataStore.phoneHashMap.remove(productId);
                return true;
            case "Laptop":

                SaxParserDataStore.laptopHashMap.remove(productId);
                return true;
            case "Voice Assistant":

                SaxParserDataStore.voiceAssistantHashMap.remove(productId);
                return true;
            case "Accessory":

                SaxParserDataStore.accessories.remove(productId);
                return true;
        }
        return false;
    }

    public boolean updateProduct(String id, String name, String price, String manufacturer, String condition, String discount, String image, String catalog) {
        switch (catalog) {
            case "Fitness Watch":
                FitnessWatch fitnessWatch = new FitnessWatch();
                fitnessWatch.setId(id);
                fitnessWatch.setName(name);
                fitnessWatch.setPrice(Double.parseDouble(price));
                fitnessWatch.setRetailer(manufacturer);
                fitnessWatch.setCondition(condition);
                fitnessWatch.setDiscount(Double.parseDouble(discount));
                fitnessWatch.setImage(image);
                SaxParserDataStore.fitnessWatchHashMap.remove(id);
                SaxParserDataStore.fitnessWatchHashMap.put(id, fitnessWatch);

                return true;
            case "Smart Watch":

                SmartWatch smartWatch = new SmartWatch();
                smartWatch.setId(id);
                smartWatch.setName(name);
                smartWatch.setPrice(Double.parseDouble(price));
                smartWatch.setRetailer(manufacturer);
                smartWatch.setCondition(condition);
                smartWatch.setDiscount(Double.parseDouble(discount));
                smartWatch.setImage(image);
                SaxParserDataStore.smartWatchHashMap.remove(id);
                SaxParserDataStore.smartWatchHashMap.put(id, smartWatch);
                return true;
            case "Virtual Reality":

                VirtualReality virtualReality = new VirtualReality();
                virtualReality.setId(id);
                virtualReality.setName(name);
                virtualReality.setPrice(Double.parseDouble(price));
                virtualReality.setRetailer(manufacturer);
                virtualReality.setCondition(condition);
                virtualReality.setDiscount(Double.parseDouble(discount));
                virtualReality.setImage(image);
                SaxParserDataStore.virtualRealityHashMap.remove(id);
                SaxParserDataStore.virtualRealityHashMap.put(id, virtualReality);
                return true;
            case "Pet Tracker":

                PetTracker petTracker = new PetTracker();
                petTracker.setId(id);
                petTracker.setName(name);
                petTracker.setPrice(Double.parseDouble(price));
                petTracker.setRetailer(manufacturer);
                petTracker.setCondition(condition);
                petTracker.setDiscount(Double.parseDouble(discount));
                petTracker.setImage(image);
                SaxParserDataStore.petTrackerHashMap.remove(id);
                SaxParserDataStore.petTrackerHashMap.put(id, petTracker);
                return true;
            case "Headphone":

                Headphone headphone = new Headphone();
                headphone.setId(id);
                headphone.setName(name);
                headphone.setPrice(Double.parseDouble(price));
                headphone.setRetailer(manufacturer);
                headphone.setCondition(condition);
                headphone.setDiscount(Double.parseDouble(discount));
                headphone.setImage(image);
                SaxParserDataStore.headphoneHashMap.remove(id);
                SaxParserDataStore.headphoneHashMap.put(id, headphone);
                return true;
            case "Phone":

                Phone phone = new Phone();
                phone.setId(id);
                phone.setName(name);
                phone.setPrice(Double.parseDouble(price));
                phone.setRetailer(manufacturer);
                phone.setCondition(condition);
                phone.setDiscount(Double.parseDouble(discount));
                phone.setImage(image);
                SaxParserDataStore.phoneHashMap.remove(id);
                SaxParserDataStore.phoneHashMap.put(id, phone);
                return true;
            case "Laptop":

                Laptop laptop = new Laptop();
                laptop.setId(id);
                laptop.setName(name);
                laptop.setPrice(Double.parseDouble(price));
                laptop.setRetailer(manufacturer);
                laptop.setCondition(condition);
                laptop.setDiscount(Double.parseDouble(discount));
                laptop.setImage(image);
                SaxParserDataStore.laptopHashMap.remove(id);
                SaxParserDataStore.laptopHashMap.put(id, laptop);
                return true;
            case "Voice Assistant":

                VoiceAssistant voiceAssistant = new VoiceAssistant();
                voiceAssistant.setId(id);
                voiceAssistant.setName(name);
                voiceAssistant.setPrice(Double.parseDouble(price));
                voiceAssistant.setRetailer(manufacturer);
                voiceAssistant.setCondition(condition);
                voiceAssistant.setDiscount(Double.parseDouble(discount));
                voiceAssistant.setImage(image);
                SaxParserDataStore.voiceAssistantHashMap.remove(id);
                SaxParserDataStore.voiceAssistantHashMap.put(id, voiceAssistant);
                return true;
            case "Accessory":

                Accessory accessory = new Accessory();
                accessory.setId(id);
                accessory.setName(name);
                accessory.setPrice(Double.parseDouble(price));
                accessory.setRetailer(manufacturer);
                accessory.setCondition(condition);
                accessory.setDiscount(Double.parseDouble(discount));
                accessory.setImage(image);
                SaxParserDataStore.accessories.remove(id);
                SaxParserDataStore.accessories.put(id, accessory);
                return true;
        }
        return false;
    }

    public boolean cancelOrder(int orderId) {

        return MySqlDataStoreUtilities.deleteOrder(orderId);

    }

    //将更新后的数据保存到文件中
    public boolean updateOrderFile(HashMap<Integer, ArrayList<OrderPayment>> orderPayments) {
        String TOMCAT_HOME = System.getProperty("catalina.home");

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(TOMCAT_HOME + "/webapps/CSP584HW1/PaymentDetails.txt"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(orderPayments);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {

        }
        return true;
    }

    public void updateOrder(int orderId, String customerName,
                            String orderName, double orderPrice, String userAddress, String creditCardNo) {
        MySqlDataStoreUtilities.deleteOrder(orderId);
        MySqlDataStoreUtilities.insertOrder(orderId, customerName, orderName, orderPrice, userAddress, creditCardNo);
    }


    public void readXML() {

        String filepath = "/Users/junruigong/Desktop/ProductCatalog.xml";
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);


        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public String storeReview(String productname, String producttype, String productmaker, String reviewrating,
                              String reviewdate, String reviewtext, String reatilerpin, String price, String city, String userAge, String userGender, String userOccupation) {
        String message = MongoDBDataStoreUtilities.insertReview(productname, username(), producttype, productmaker, reviewrating, reviewdate, reviewtext, reatilerpin, price, city, userAge, userGender, userOccupation);
        if (!message.equals("Successful")) {
            return "UnSuccessful";
        } else {
            HashMap<String, ArrayList<Review>> reviews = new HashMap<String, ArrayList<Review>>();
            try {
                reviews = MongoDBDataStoreUtilities.selectReview();
            } catch (Exception e) {

            }
            if (reviews == null) {
                reviews = new HashMap<String, ArrayList<Review>>();
            }
            // if there exist product review already add it into same list for productname or create a new record with product name

            if (!reviews.containsKey(productname)) {
                ArrayList<Review> arr = new ArrayList<Review>();
                reviews.put(productname, arr);
            }
            ArrayList<Review> listReview = reviews.get(productname);
            Review review = new Review(productname, username(), producttype, productmaker, reviewrating, reviewdate, reviewtext, reatilerpin, price, city, userAge, userGender, userOccupation);
            listReview.add(review);

            // add Reviews into database

            return "Successful";
        }
    }

    public boolean isContainsStr(String string) {
        String regex = ".*[a-zA-Z]+.*";
        Matcher m = Pattern.compile(regex).matcher(string);
        return m.matches();
    }

    /*  username Function returns the username from the session variable.*/

    public String username() {
        if (session.getAttribute("username") != null)
            return session.getAttribute("username").toString();
        return null;
    }

    /*  usertype Function returns the usertype from the session variable.*/
    public String usertype() {
        if (session.getAttribute("usertype") != null)
            return session.getAttribute("usertype").toString();
        return null;
    }

    /*  getUser Function checks the user is a customer or retailer or manager and returns the user class variable.*/
    public User getUser() {
        String usertype = usertype();
        HashMap<String, User> hm = new HashMap<String, User>();
        hm = MySqlDataStoreUtilities.selectUser();

        return hm.get(username());
    }

    /*  getCustomerOrders Function gets  the Orders for the user*/
    public ArrayList<OrderItem> getCustomerOrders() {
        ArrayList<OrderItem> order = new ArrayList<OrderItem>();
        if (OrdersHashMap.orders.containsKey(username()))
            order = OrdersHashMap.orders.get(username());
        return order;
    }

    /*  getOrdersPaymentSize Function gets  the size of OrderPayment */
    public int getOrderPaymentSize() {
        HashMap<Integer, ArrayList<OrderPayment>> orderPayments = new HashMap<Integer, ArrayList<OrderPayment>>();

        orderPayments = MySqlDataStoreUtilities.selectOrder();

        int size = 0;
        for (Map.Entry<Integer, ArrayList<OrderPayment>> entry : orderPayments.entrySet()) {
            size = size + 1;

        }
        return size;
    }

    /*  CartCount Function gets the size of User Orders*/
    public int CartCount() {
        if (isLoggedin())
            return getCustomerOrders().size();
        return 0;
    }


    public void removeItemFromCart(String itemName) {
        ArrayList<OrderItem> orderItems = OrdersHashMap.orders.get(username());
        int index = 0;

        //遍历所有orderItem，找到需要删除的item的index。
        for (OrderItem oi : orderItems) {
            if (oi.getName().equals(itemName)) {
                break;
            } else index++;
        }
        orderItems.remove(index);
    }

    /* StoreProduct Function stores the Purchased product in Orders HashMap according to the User Names.*/

    public void storeProduct(String name, String type, String maker, String acc) {
        if (!OrdersHashMap.orders.containsKey(username())) {
            ArrayList<OrderItem> arr = new ArrayList<OrderItem>();
            OrdersHashMap.orders.put(username(), arr);
        }

        ArrayList<OrderItem> orderItems = OrdersHashMap.orders.get(username());

        if (type.equals("fitnessWatch")) {
            FitnessWatch fitnessWatch = null;
            fitnessWatch = SaxParserDataStore.fitnessWatchHashMap.get(name);
            OrderItem orderitem = new OrderItem(fitnessWatch.getName(), fitnessWatch.getPrice(), fitnessWatch.getImage(), fitnessWatch.getRetailer());
            orderItems.add(orderitem);
        }

        if (type.equals("smartWatch")) {
            SmartWatch smartWatch = null;
            smartWatch = SaxParserDataStore.smartWatchHashMap.get(name);
            OrderItem orderitem = new OrderItem(smartWatch.getName(), smartWatch.getPrice(), smartWatch.getImage(), smartWatch.getRetailer());
            orderItems.add(orderitem);
        }

        if (type.equals("headphone")) {
            Headphone headphone = null;
            headphone = SaxParserDataStore.headphoneHashMap.get(name);
            OrderItem orderitem = new OrderItem(headphone.getName(), headphone.getPrice(), headphone.getImage(), headphone.getRetailer());
            orderItems.add(orderitem);
        }

        if (type.equals("virtualReality")) {
            VirtualReality virtualReality = null;
            virtualReality = SaxParserDataStore.virtualRealityHashMap.get(name);
            OrderItem orderitem = new OrderItem(virtualReality.getName(), virtualReality.getPrice(), virtualReality.getImage(), virtualReality.getRetailer());
            orderItems.add(orderitem);
        }

        if (type.equals("petTracker")) {
            PetTracker petTracker = null;
            petTracker = SaxParserDataStore.petTrackerHashMap.get(name);
            OrderItem orderitem = new OrderItem(petTracker.getName(), petTracker.getPrice(), petTracker.getImage(), petTracker.getRetailer());
            orderItems.add(orderitem);
        }

        if (type.equals("phone")) {
            Phone phone = null;
            phone = SaxParserDataStore.phoneHashMap.get(name);
            // OrderItem orderitem = new OrderItem(phone.getName(), phone.getPrice(), phone.getImage(), phone.getRetailer());
            OrderItem orderitem = new OrderItem(phone.getId(), phone.getPrice(), phone.getImage(), phone.getRetailer());
            orderItems.add(orderitem);
        }
        if (type.equals("laptop")) {
            Laptop laptop = null;
            laptop = SaxParserDataStore.laptopHashMap.get(name);
            OrderItem orderitem = new OrderItem(laptop.getName(), laptop.getPrice(), laptop.getImage(), laptop.getRetailer());
            orderItems.add(orderitem);
        }
        if (type.equals("voiceAssistant")) {
            VoiceAssistant voiceAssistant = SaxParserDataStore.voiceAssistantHashMap.get(name);
            OrderItem orderitem = new OrderItem(voiceAssistant.getName(), voiceAssistant.getPrice(), voiceAssistant.getImage(), voiceAssistant.getRetailer());
            orderItems.add(orderitem);
        }

        if (type.equals("accessory")) {
            Accessory accessory = null;
            accessory = SaxParserDataStore.accessories.get(name);
            OrderItem orderitem = new OrderItem(accessory.getName(), accessory.getPrice(), accessory.getImage(), accessory.getRetailer());
            orderItems.add(orderitem);
        }
    }

    // store the payment details for orders
    public void storePayment(int orderId,
                             String orderName, double orderPrice, String userAddress, String creditCardNo, String customer) {
        String username = (String) session.getAttribute("username");

        MySqlDataStoreUtilities.insertOrder(orderId, username, orderName, orderPrice, userAddress, creditCardNo);
    }

    public boolean removeOldOrder(int orderId, String orderName, String customerName) {
        return MySqlDataStoreUtilities.deleteOrder(orderId);
    }

    public void storeNewOrder(int orderId, String orderName, String customerName, double orderPrice, String userAddress, String creditCardNo) {
        MySqlDataStoreUtilities.insertOrder(orderId, orderName, customerName, orderPrice, userAddress, creditCardNo);

    }

    public HashMap<String, FitnessWatch> getFitnessWatches() {
        return new HashMap<String, FitnessWatch>(SaxParserDataStore.fitnessWatchHashMap);
    }

    public HashMap<String, SmartWatch> getSmartWatches() {
        HashMap<String, SmartWatch> hm = new HashMap<String, SmartWatch>();
        hm.putAll(SaxParserDataStore.smartWatchHashMap);
        return hm;
    }

    public HashMap<String, Headphone> getHeadphones() {
        HashMap<String, Headphone> hm = new HashMap<String, Headphone>();
        hm.putAll(SaxParserDataStore.headphoneHashMap);
        return hm;
    }

    public HashMap<String, VirtualReality> getVirtualRealitys() {
        HashMap<String, VirtualReality> hm = new HashMap<String, VirtualReality>();
        hm.putAll(SaxParserDataStore.virtualRealityHashMap);
        return hm;
    }

    public HashMap<String, PetTracker> getPetTrackers() {
        HashMap<String, PetTracker> hm = new HashMap<String, PetTracker>();
        hm.putAll(SaxParserDataStore.petTrackerHashMap);
        return hm;
    }


    /* getGames Functions returns the  Hashmap with all Phone in the store.*/

    public HashMap<String, Phone> getPhones() {
        HashMap<String, Phone> hm = new HashMap<String, Phone>();
        hm.putAll(SaxParserDataStore.phoneHashMap);
        return hm;
    }

    /* getTablets Functions returns the Hashmap with all laptops in the store.*/

    public HashMap<String, Laptop> getLaptops() {
        HashMap<String, Laptop> hm = new HashMap<String, Laptop>();
        hm.putAll(SaxParserDataStore.laptopHashMap);
        return hm;
    }


    /* getVoiceAssistants Functions returns the Hashmap with all VoiceAssistants in the store.*/
    public HashMap<String, VoiceAssistant> getVoiceAssistants() {
        HashMap<String, VoiceAssistant> hm = new HashMap<String, VoiceAssistant>();
        hm.putAll(SaxParserDataStore.voiceAssistantHashMap);
        return hm;
    }


    /* getProducts Functions returns the Arraylist of games in the store.*/

    public ArrayList<String> getProductsPhone() {
        ArrayList<String> ar = new ArrayList<String>();
        for (Map.Entry<String, Phone> entry : getPhones().entrySet()) {
            ar.add(entry.getValue().getName());
        }
        return ar;
    }

    /* getProducts Functions returns the Arraylist of Tablets in the store.*/

    public ArrayList<String> getProductsLaptops() {
        ArrayList<String> ar = new ArrayList<String>();
        for (Map.Entry<String, Laptop> entry : getLaptops().entrySet()) {
            ar.add(entry.getValue().getName());
        }
        return ar;
    }
}
