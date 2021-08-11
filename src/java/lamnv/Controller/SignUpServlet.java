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
import javax.websocket.Session;
import lamnv.DAO.UserDAO;
import lamnv.DTO.UserDTO;

/**
 *
 * @author ACER
 */
@WebServlet(name = "SignUpServlet", urlPatterns = {"/SignUp"})
public class SignUpServlet extends HttpServlet {
    private final String signUpViewURL = "/WEB-INF/jsp/view/signup.jsp";
    private final String loginViewURL = "/WEB-INF/jsp/view/login.jsp";
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
        response.sendRedirect(signUpViewURL);
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
        if(action.equals("signUp")){
            String userID = request.getParameter("userID");
            String fullName = request.getParameter("fullName");
            String password = request.getParameter("password");
            
            //Validate here
            
            UserDAO dao = new UserDAO();
            
            //Check userID in db
            boolean doesUserIDExist = dao.checkUserID(userID);
            if(doesUserIDExist){
                request.setAttribute("signUpError", "UserID is used, please choose another userID");
                RequestDispatcher rd = request.getRequestDispatcher(signUpViewURL);
                rd.forward(request, response);
            }
            
            //Create user in db
            UserDTO signUpInfo = new UserDTO(userID, password, fullName);
            UserDTO newUser = dao.createUser(signUpInfo);
            if(newUser == null){
                request.setAttribute("signUpError", "Cannot create new user in database, please try again");
                RequestDispatcher rd = request.getRequestDispatcher(signUpViewURL);
                rd.forward(request, response);
            }
            
            //Create successfully
            response.sendRedirect(loginViewURL);
        }
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
