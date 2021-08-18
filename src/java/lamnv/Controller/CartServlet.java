/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lamnv.Controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lamnv.DAO.ProductDAO;
import lamnv.DTO.ProductDTO;
import lamnv.DTO.UserDTO;
import lamnv.exception.ProductExc;
import lamnv.viewmodel.Cart;

/**
 *
 * @author ACER
 */
@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {

    private final String cartViewURL = "/WEB-INF/jsp/view/cart.jsp";
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

        HttpSession session = request.getSession();
        
        //admin cant access this servlet
        if(session.getAttribute("user")!=null){
            UserDTO user = (UserDTO) session.getAttribute("user");
            if(user.RoleID==2){
                response.sendRedirect("product");
                return;
            }
                
        }
        
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "show";
        }

        switch (action) {
            case "add": {
                AddToCart(request, response, cart);
                return;
            }
            case "delete": {
                DeleteFromCart(request, response, cart);
                return;
            }
            case "deleteProduct":{
                deleteProduct(request,response,cart);
                return;
            }
            case "clearAll":{
                ClearAll(request,response);
                return;
            }
            default: {
                ShowCart(request, response, cart);
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

    private void AddToCart(HttpServletRequest request, HttpServletResponse response, Cart cart) throws ServletException, IOException {

        String proIDString = request.getParameter("proID");
        String quantityString = request.getParameter("quantity");

        try {
            int proID = Integer.parseInt(proIDString);
            int quantity = Integer.parseInt(quantityString);
            
            ProductDAO dao = new ProductDAO();
            ProductDTO addedProduct = dao.getAvailableProductByID(proID);
            if(addedProduct == null) throw new ProductExc("Product is unable or not exists");
            
            cart.addProduct(addedProduct, quantity);
            request.getSession().setAttribute("cart", cart);
        } catch (NumberFormatException e) {
            request.setAttribute("addError", "Product ID and quantity must be integer");
        } catch (ProductExc ex) {
            request.setAttribute("addError", ex.getMessage());
        }

        request.getRequestDispatcher(cartViewURL).forward(request, response);
    }

    private void DeleteFromCart(HttpServletRequest request, HttpServletResponse response, Cart cart) throws ServletException, IOException {
        String proIDString = request.getParameter("proID");
        String quantityString = request.getParameter("quantity");

        try {
            int proID = Integer.parseInt(proIDString);
            int quantity = Integer.parseInt(quantityString);
            
            ProductDAO dao = new ProductDAO();
            ProductDTO deletedProduct = dao.getAvailableProductByID(proID);
            if(deletedProduct == null) throw new ProductExc("Product is unable or not exists");
            
            cart.deleteProduct(deletedProduct, quantity);
            request.getSession().setAttribute("cart", cart);
        } catch (NumberFormatException e) {
            request.setAttribute("deleteError", "Product ID and quantity must be integer");
        } catch (ProductExc ex) {
            request.setAttribute("deleteError", ex.getMessage());
        }

        request.getRequestDispatcher(cartViewURL).forward(request, response);
    }

    private void ShowCart(HttpServletRequest request, HttpServletResponse response, Cart cart) throws ServletException, IOException {
        request.getRequestDispatcher(cartViewURL).forward(request, response);
    }

    private void ClearAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       request.getSession().setAttribute("cart", null);
       request.getRequestDispatcher(cartViewURL).forward(request, response);
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response, Cart cart) throws ServletException, IOException {
        String proIDString = request.getParameter("proID");

        try {
            int proID = Integer.parseInt(proIDString);
            ProductDAO dao = new ProductDAO();
            ProductDTO deletedProduct = dao.getProductByID(proID);
            if(deletedProduct==null) throw new ProductExc("Product does not exist");
            cart.deleteProduct(deletedProduct, -1);
            request.getSession().setAttribute("cart", cart);
        } catch (NumberFormatException e) {
            request.setAttribute("deleteError", "Product ID and quantity must be integer");
        } catch (ProductExc ex) {
            request.setAttribute("deleteError", ex.getMessage());
        }

        request.getRequestDispatcher(cartViewURL).forward(request, response);
    }

}
