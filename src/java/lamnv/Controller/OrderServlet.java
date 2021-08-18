/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lamnv.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lamnv.DAO.OrderDAO;
import lamnv.DAO.OrderDetailDAO;
import lamnv.DTO.OrderDTO;
import lamnv.DTO.ProductDTO;
import lamnv.DTO.UserDTO;
import lamnv.Util.Validator;
import lamnv.exception.OrderExc;
import lamnv.exception.ProductExc;
import lamnv.viewmodel.Cart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ACER
 */
@WebServlet(name = "OrderServlet", urlPatterns = {"/order"})
public class OrderServlet extends HttpServlet {
    private final Logger log = LogManager.getLogger();

    private final String checkOutViewURL = "/WEB-INF/jsp/view/cart.jsp";
    private final String orderViewURL = "/WEB-INF/jsp/view/order.jsp";
    private final String detailViewURL = "/WEB-INF/jsp/view/orderDetail.jsp";
    private final String errorViewURL = "/WEB-INF/jsp/view/error.jsp";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "showOrders";
        }
        
        HttpSession session = request.getSession();
        //admin cant access checkOut and submitOrder actions
        if(session.getAttribute("user")!=null){
            UserDTO user = (UserDTO) session.getAttribute("user");
            if(user.RoleID==2 &&(action.equals("checkOut") || action.equals("submitOrder"))){
                response.sendRedirect("product");
            }
                
        }
        
        switch (action) {
            case "checkOut": {
                checkOut(request, response, session);
                return;
            }
            case "submitOrder": {
                submitOrder(request, response, session);
                return;
            }
            case "orderDetail": {
                showOrderDetail(request, response);
                return;
            }
            case "showOrders":
            default:{
                showOrders(request, response);
            }
        }

    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void checkOut(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException, ServletException {
//        if(!authorize(request, response))
//            return;

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            request.setAttribute("checkOutError", "Cart is empty");
        }

        request.getRequestDispatcher(checkOutViewURL).forward(request, response);
    }

    private void submitOrder(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException, ServletException {
//        if(!authorize(request, response))
//            return;

        //validate address here
        String address = request.getParameter("address");
        if (address == null || address.isEmpty()) {
            request.setAttribute("checkOutError", "Please fill your address");
            request.getRequestDispatcher(checkOutViewURL).forward(request, response);
            return;
        }else if(!Validator.checkAddress(address)){
            request.setAttribute("checkOutError", "Invalid address");
            request.getRequestDispatcher(checkOutViewURL).forward(request, response);
            return;
        }

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            request.setAttribute("checkOutError", "Cart is empty");
            request.getRequestDispatcher(checkOutViewURL).forward(request, response);
            return;
        }
        UserDTO userInf = (UserDTO) session.getAttribute("user");

        //Action on database and catch error
        OrderDAO dao = new OrderDAO();
        boolean success = false;
        Integer newOrderID = null;
        try {
            Timestamp orderDateTime = Timestamp.valueOf(LocalDateTime.now());
            newOrderID = dao.createOrder(cart, userInf, address, orderDateTime);
            session.setAttribute("cart", null);
            success = true;
        } catch (OrderExc e) {
            request.setAttribute("checkOutError", e.getMessage());
        }

        if(success){
            request.setAttribute("checkOutSuccess", "Success");
            log.info("OrderID: "+ newOrderID + " has been made");
        }
            
        
        request.getRequestDispatcher(checkOutViewURL).forward(request, response);
    }

//    private boolean authorize(HttpServletRequest request, HttpServletResponse response) throws IOException{
//        HttpSession session = request.getSession(false);
//        if (session == null) {
//            //log here
//            response.sendRedirect("product");
//            return false;
//        }
//
//        UserDTO userInf = (UserDTO) session.getAttribute("user");
//        if (userInf == null) {
//            //log here
//            response.sendRedirect("product");
//            return false;
//        }
//        
//        return true;
//    }
    private void showOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDTO userInf = (UserDTO) request.getSession().getAttribute("user");

        OrderDAO dao = new OrderDAO();
        List<OrderDTO> orderList;
        if(userInf.RoleID==1){
            orderList = dao.getCustomerOrders(userInf.userID);
        }else{
            orderList = dao.AllOrder();
        }
        
        request.setAttribute("orderList", orderList);

        request.getRequestDispatcher(orderViewURL).forward(request, response);
    }

    private void showOrderDetail(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        UserDTO userInf = (UserDTO) request.getSession().getAttribute("user");

        String orderIDString = request.getParameter("orderID");
        int orderID = 0;
        if (orderIDString == null) {
            response.sendRedirect("product");
            return;
        } else {
            try {
                orderID = Integer.parseInt(orderIDString);
                OrderDAO orderDAO = new OrderDAO();
                UserDTO ownerInf =  orderDAO.getOrderOwner(orderID);
                if (userInf.RoleID!=2 && !ownerInf.userID.equals(userInf.userID)) {
                    response.sendRedirect("product");
                    return;
                }
            } catch (NumberFormatException e) {
            }
        }

        OrderDetailDAO dao = new OrderDetailDAO();
        List<ProductDTO> productList = dao.getOrderDetails(orderID);
        Cart cart = new Cart(productList);
        request.setAttribute("totalPrice", cart.totalPrice);
        request.setAttribute("productList", productList);
        request.setAttribute("orderID", orderID);

        request.getRequestDispatcher(detailViewURL).forward(request, response);
    }

}
