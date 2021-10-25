/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lamnv.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lamnv.DAO.UserDAO;
import lamnv.DTO.UserDTO;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author ACER
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger();

    private final String loginViewURL = "/WEB-INF/jsp/view/login.jsp";
    private final String errorViewURL = "/WEB-INF/jsp/view/error.jsp";
    private final String waitViewURL = "/WEB-INF/jsp/view/wait.jsp";

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
        RequestDispatcher rd = request.getRequestDispatcher(loginViewURL);

        String action = request.getParameter("action");
        if (action == null) {
            action = "login";
        }

        if (action.equals("logout")) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            rd.forward(request, response);
            return;
        }

        //Require user to logout first
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            request.setAttribute("loginError", "Must logout before login new user");
            //forward to error page
            request.getRequestDispatcher(errorViewURL).forward(request, response);
            return;
        }

        if (action.equals("login")) {
            rd.forward(request, response);
        }

        if (action.equals("loginByGG")) {
            loginByGG(request, response);
            return;
        }

        if (action.equals("loginByGGCodeEndpoint")) {
            loginByGGCodeEndpoint(request, response);
            return;
        }

        if (action.equals("loginByGGTokenEndpoint")) {
            loginByGGTokenEndpoint(request, response);
            return;
        }

        if (action.equals("loginByGGProfileEndpoint")) {
            loginByGGProfileEndpoint(request, response);
        }

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
        if (action == null) {
            action = "login";
        }

        if (action.equals("login")) {
            login(request, response);
            return;
        }

        if (action.equals("loginByGG")) {
            loginByGG(request, response);
            return;
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

    private void loginByGG(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = ("https://accounts.google.com/o/oauth2/v2/auth?"
                + "scope=https%3A//www.googleapis.com/auth/userinfo.profile"
                + "&access_type=offline"
                + "&include_granted_scopes=true"
                + "&response_type=code"
                + "&state=state_parameter_passthrough_value"
                + "&redirect_uri=http%3A//localhost:8080/CarShop/login?action=loginByGGCodeEndpoint"
                + "&client_id=715844637538-hpui035stt79b1o3dokgq9ac366g0b0f.apps.googleusercontent.com");
        response.sendRedirect(url);

    }

    private void googleTokenEnpoint(HttpServletRequest request, HttpServletResponse response) {

    }

    private void loginByGGCodeEndpoint(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean success = false;
        String code = request.getParameter("code");
        if (code == null) {
            request.setAttribute("loginGGError", "No code found from GG response");
            request.getRequestDispatcher(loginViewURL).forward(request, response);
            return;
        }
        HttpPost post = new HttpPost("https://oauth2.googleapis.com/token");

        // add request parameter, form parameters
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("code", code));
        urlParameters.add(new BasicNameValuePair("client_secret", "eJK-VMtMAONfTMZ_85zIBsA2"));
        urlParameters.add(new BasicNameValuePair("redirect_uri", "http://localhost:8080/CarShop/login?action=loginByGGCodeEndpoint"));
        urlParameters.add(new BasicNameValuePair("grant_type", "authorization_code"));
        urlParameters.add(new BasicNameValuePair("client_id", "715844637538-hpui035stt79b1o3dokgq9ac366g0b0f.apps.googleusercontent.com"));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        JSONParser parser = new JSONParser();
        JSONObject profileObject = null;

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse ggResponse = httpClient.execute(post)) {
            JSONObject jsonObject = (JSONObject) parser.parse(EntityUtils.toString(ggResponse.getEntity()));
            if (jsonObject == null || jsonObject.get("error") != null) {
                request.setAttribute("loginGGError", "cant grant access token");
                request.getRequestDispatcher(loginViewURL).forward(request, response);
                return;
            }
            String accessToken = (String) jsonObject.get("access_token");
//            request.setAttribute("ggCode", accessToken);
//            request.getRequestDispatcher(loginViewURL).forward(request, response);

            String apiURL = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json";
            HttpGet get = new HttpGet(apiURL);
            get.setHeader("Authorization", "Bearer " + accessToken);

            try (CloseableHttpResponse profileAPIResponse = httpClient.execute(get)) {
                profileObject = (JSONObject) parser.parse(EntityUtils.toString(profileAPIResponse.getEntity()));
                if (jsonObject == null || jsonObject.get("error") != null) {
                    request.setAttribute("loginGGError", "Access token is invalid");
                    request.getRequestDispatcher(loginViewURL).forward(request, response);
                    return;
                }

            }
        } catch (ParseException | org.json.simple.parser.ParseException ex) {
            log.error(ex);
        }
        String name = (String) profileObject.get("name");
        String id = (String) profileObject.get("id");
        if (id != null && name != null) {
            //check if user is created
            UserDAO dao = new UserDAO();
            boolean exist = dao.checkUserID(id);

            //does not exist in db
            if (!exist) {
                UserDTO newUserByGG = new UserDTO(id, id, name);
                UserDTO createdUser = dao.createUser(newUserByGG);
                if (createdUser == null) {
                    log.error("Cannot create user by id and name from GG");
                    request.setAttribute("loginGGError", "Please try again");
                } else {
                    createdUser.setRoleID(1);
                    HttpSession session = request.getSession();
                    session.setAttribute("user", createdUser);
                    success = true;
                }
            } //user was created in dtb
            else {
                UserDTO userByGG = dao.getUser(id, id);
                HttpSession session = request.getSession();
                session.setAttribute("user", userByGG);
                success = true;
            }
        } else {
            request.setAttribute("loginGGError", "gg profile doesnt contain name and id");
            request.getRequestDispatcher(loginViewURL).forward(request, response);
            return;
        }

        if (success) {
            response.sendRedirect("product");
        } else {
            response.sendRedirect("login");
        }

    }

    private void loginByGGTokenEndpoint(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void loginByGGProfileEndpoint(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Require user to logout first
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            request.setAttribute("loginError", "Must logout before login new user");
            //forward to error page
            RequestDispatcher rd = request.getRequestDispatcher(errorViewURL);
            rd.forward(request, response);
            return;
        }

        //Get login in4
        String userID = request.getParameter("userID");
        String password = request.getParameter("password");
        if (userID != null && password != null) {
            UserDAO dao = new UserDAO();
            UserDTO userInfo = dao.getUser(userID, password);

            //Cant find user in database 
            if (userInfo == null) {
                request.setAttribute("loginError", "Cannot find user, please try again");
                RequestDispatcher rd = request.getRequestDispatcher(loginViewURL);
                rd.forward(request, response);
                return;
            }

            //Login success, set user infor in session
            session.setAttribute("user", userInfo);
            response.sendRedirect("product");
            return;
        }

        //userID and password are null
        request.setAttribute("loginError", "Please type userID and password to login");
        RequestDispatcher rd = request.getRequestDispatcher(loginViewURL);
        rd.forward(request, response);
    }
}
