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
                        result = result + "<li><a href='ViewOrder'><span class='glyphicon'>ViewOrder</span></a></li>"
                                + "<li><a><span class='glyphicon'>Hello, " + username + "</span></a></li>"
                                + "<li><a href='Account'><span class='glyphicon'>Account</span></a></li>"
                                + "<li><a href='Logout'><span class='glyphicon'>Logout</span></a></li>";
                        break;
                    case "StoreManager":
                        result = result + "<li><a href='ViewOrder'><span class='glyphicon'>ViewOrder</span></a></li>"
                                + "<li><a><span class='glyphicon'>Hello, " + username + "</span></a></li>"
                                + "<li><a href='Account'><span class='glyphicon'>Account</span></a></li>"
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
        String TOMCAT_HOME = System.getProperty("catalina.home");
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(TOMCAT_HOME + "/webapps/CSP584HW1/UserDetails.txt"));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            hm = (HashMap) objectInputStream.readObject();
        } catch (Exception ignored) {
        }
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
        String TOMCAT_HOME = System.getProperty("catalina.home");
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(TOMCAT_HOME + "/webapps/CSP584HW1/PaymentDetails.txt"));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            orderPayments = (HashMap) objectInputStream.readObject();
        } catch (Exception ignored) {

        }
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
                             String orderName, double orderPrice, String userAddress, String creditCardNo) {
        HashMap<Integer, ArrayList<OrderPayment>> orderPayments = new HashMap<Integer, ArrayList<OrderPayment>>();
        String TOMCAT_HOME = System.getProperty("catalina.home");
        // get the payment details file
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(TOMCAT_HOME + "/webapps/CSP584HW1/PaymentDetails.txt"));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            orderPayments = (HashMap) objectInputStream.readObject();
        } catch (Exception ignored) {

        }
        if (orderPayments == null) {
            orderPayments = new HashMap<Integer, ArrayList<OrderPayment>>();
        }
        // if there exist order id already add it into same list for order id or create a new record with order id

        if (!orderPayments.containsKey(orderId)) {
            ArrayList<OrderPayment> arr = new ArrayList<OrderPayment>();
            orderPayments.put(orderId, arr);
        }
        ArrayList<OrderPayment> listOrderPayment = orderPayments.get(orderId);

        OrderPayment orderpayment = new OrderPayment(orderId, username(), orderName, orderPrice, userAddress, creditCardNo);
        listOrderPayment.add(orderpayment);

        // add order details into file

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(TOMCAT_HOME + "/webapps/CSP584HW1/PaymentDetails.txt"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(orderPayments);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            System.out.println("inside exception file not written properly");
        }
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
