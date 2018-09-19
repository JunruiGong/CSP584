import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


//Remove product by manager
@WebServlet("/RemoveProduct")
public class RemoveProduct extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//        response.setContentType("text/html");
//        PrintWriter pw = response.getWriter();
//
//        Utilities utility = new Utilities(request, pw);
//        String productName = request.getParameter("orderName");
//        utility.removeItemFromCart(name);
//        /* StoreProduct Function stores the Purchased product in Orders HashMap.*/
//
//        response.sendRedirect("Cart");
//        return;
    }
}
