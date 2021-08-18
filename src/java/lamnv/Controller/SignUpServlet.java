/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lamnv.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lamnv.DAO.UserDAO;
import lamnv.DTO.UserDTO;
import lamnv.Util.Validator;
import lamnv.exception.SignUpExc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ACER
 */
@WebServlet(name = "SignUpServlet", urlPatterns = {"/signup"})
public class SignUpServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger();

    private final String signUpViewURL = "/WEB-INF/jsp/view/signup.jsp";
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
        request.getRequestDispatcher(signUpViewURL).forward(request, response);
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
        List<SignUpExc> errorList;
        if (request.getAttribute("errorList") == null) {
            errorList = new ArrayList<>();
            request.setAttribute("errorList", errorList);
        } else {
            errorList = (ArrayList<SignUpExc>) request.getAttribute("errorList");
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "signup";
        }

        if (action.equals("signup")) {
            String userID = request.getParameter("userID");
            String fullName = request.getParameter("fullName");
            String password = request.getParameter("password");

            //Validate code here
            boolean valid = true;
            if (userID == null || fullName == null || password == null) {
                errorList.add(new SignUpExc("Must fill all fields"));
                valid = false;
            } else {
                if (!Validator.checkFullName(fullName)) {
                    errorList.add(new SignUpExc("Please insert valid full name"));
                    valid = false;
                }
                if (!Validator.checkPassword(password)) {
                    errorList.add(new SignUpExc("Password must be between 8 and 30 characters"));
                    valid = false;
                }
                if (!Validator.checkUserID(userID)) {
                    errorList.add(new SignUpExc("UserID format must be XX000000 - ex:SE123123, PP321312"));
                    valid = false;
                }
            }

            if (!valid) {
                request.setAttribute("errorList", errorList);
                RequestDispatcher rd = request.getRequestDispatcher(signUpViewURL);
                rd.forward(request, response);
                return;
            }

            //Check userID in db
            UserDAO dao = new UserDAO();
            boolean doesUserIDExist = dao.checkUserID(userID);
            if (doesUserIDExist) {
                errorList.add(new SignUpExc("UserID is used, please choose another userID"));
                request.setAttribute("errorList", errorList);
                RequestDispatcher rd = request.getRequestDispatcher(signUpViewURL);
                rd.forward(request, response);
                return;
            }

            //Create user in db
            UserDTO signUpInfo = new UserDTO(userID, password, fullName);
            UserDTO newUser = dao.createUser(signUpInfo);
            if (newUser == null) {
                errorList.add(new SignUpExc("Cannot create new user in database, please try again"));
                request.setAttribute("errorList", errorList);
                RequestDispatcher rd = request.getRequestDispatcher(signUpViewURL);
                rd.forward(request, response);
                return;
            }

            //Create successfully
            //log here
            log.info("UserID:" + userID + " is created successfully");
            response.sendRedirect("login");
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
