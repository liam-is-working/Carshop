/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lamnv.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import lamnv.DAO.UserDAO;
import lamnv.DTO.UserDTO;

/**
 *
 * @author ACER
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private final String loginViewURL = "/WEB-INF/jsp/view/login.jsp";
    private final String shopViewURL = "/WEB-INF/jsp/view/shop.jsp";

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
        String action = request.getParameter("action");
        if (action != null) {
            if (action.equals("logout")) {
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.removeAttribute("user");
                }
                response.sendRedirect("login");
            }
        }
        response.sendRedirect("login");

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
        String action = request.getParameter("action");
        
        if (action != null) {
            if (action.equals("login")) {

                //Require user to logout first
                HttpSession session = request.getSession();
                if (session.getAttribute("user") != null) {
                    request.setAttribute("loginError", "Must logout before login new user");
                    RequestDispatcher rd = request.getRequestDispatcher(loginViewURL);
                    rd.forward(request, response);
                }

                String userID = request.getParameter("userID");
                String password = request.getParameter("password");
                if (userID != null && password != null) {
                    UserDAO dao = new UserDAO();
                    UserDTO userInfo = dao.getUser(userID, password);
                    //Cant find user in database 
                    if (userInfo == null) {
                        request.setAttribute("loginError", "User is not in database");
                        RequestDispatcher rd = request.getRequestDispatcher(loginViewURL);
                        rd.forward(request, response);
                    }
                    //Set user infor in session
                    session.setAttribute("user", userInfo);
                    response.sendRedirect("shop");
                }

                //userID and password are null
                request.setAttribute("loginError", "Please type userID and password to login");
                RequestDispatcher rd = request.getRequestDispatcher(loginViewURL);
                rd.forward(request, response);
            }
        }
        
        //no action specify
        response.sendRedirect("login");

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

}
