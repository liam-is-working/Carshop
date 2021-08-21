/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lamnv.Controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lamnv.DAO.CategoryDAO;
import lamnv.DAO.ProductDAO;
import lamnv.DTO.CategoryDTO;
import lamnv.DTO.ProductDTO;
import lamnv.DTO.UserDTO;
import lamnv.Util.Validator;
import lamnv.exception.ProductExc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ACER
 */
@WebServlet(name = "ProductServlet", urlPatterns = {"/product"})
public class ProductServlet extends HttpServlet {

    private static final Logger log = LogManager.getLogger();
    private final String productViewURL = "/WEB-INF/jsp/view/product.jsp";
    private final String errorViewURL = "/WEB-INF/jsp/view/error.jsp";

//    private List<ProductDTO> availableProductList;
    private Map<Integer, String> categoryMap;

    @Override
    public void init() throws ServletException {
        super.init(); //To change body of generated methods, choose Tools | Templates.

        //fetch available product list from db
//        fetchProduct();
        //fetch available category list from db
        fetchCategory();

    }
//
//    private void fetchProduct() {
//        ProductDAO productDAO = new ProductDAO();
//        availableProductList = productDAO.getAvailableProduct();
//        if (availableProductList == null) {
//            availableProductList = new LinkedList<>();
//        }
//    }

    private void fetchCategory() {
        CategoryDAO categoryDAO = new CategoryDAO();
        List<CategoryDTO> availableCategoryList = categoryDAO.getAvailableCategory();
        if (availableCategoryList != null) {
            categoryMap = new HashMap<>();
            availableCategoryList.forEach((categoryDTO) -> {
                categoryMap.put(categoryDTO.getCategoryID(), categoryDTO.getCategoryName());
            });
        }
    }

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
        request.setAttribute("categoryMap", categoryMap);
        request.setAttribute("isAdmin", isAdmin(request));

        String action = request.getParameter("action");
        if (action == null) {
            if (isAdmin(request)) {
                action = "getAllAdmin";
            } else {
                action = "getAll";
            }
        }

        switch (action) {
            case "getAll": {
                getAll(request, response);
                return;
            }
            case "searchByName": {
                searchByName(request, response);
                return;
            }
            case "searchByCategoryID": {
                searchByCategory(request, response);
                return;
            }
            case "addProduct": {
                addProduct(request, response);
                return;
            }
            case "updateProduct": {
                updateProduct(request, response);
                return;
            }
            case "getAllAdmin": {
                getAllAdmin(request, response);
                return;
            }
            case "getProductByID": {
                getProductByID(request, response);
                return;
            }
            case "updateState": {
                updateState(request, response);
                return;
            }
            case "getUnable": {
                getUnable(request, response);
                return;
            }
            default: {
                if (isAdmin(request)) {
                    getAllAdmin(request, response);
                } else {
                    getAll(request, response);
                }
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

    private void getAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDAO dao = new ProductDAO();
        List<ProductDTO> proList = dao.getAvailableProduct();
        request.setAttribute("productList", proList);
        RequestDispatcher rd = request.getRequestDispatcher(productViewURL);
        rd.forward(request, response);
    }

    private void searchByName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDAO dao = new ProductDAO();
        String searchNameTxt = request.getParameter("searchNameTxt");
        if (searchNameTxt == null) {
            searchNameTxt = "";
        }
        List<ProductDTO> proList = dao.getProductListByName(searchNameTxt);
        request.setAttribute("productList", proList);
        RequestDispatcher rd = request.getRequestDispatcher(productViewURL);
        rd.forward(request, response);
    }

    private void searchByCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDAO dao = new ProductDAO();
        String searchCategoryIDTxt = request.getParameter("searchCategoryIDTxt");
        if (searchCategoryIDTxt == null) {
            searchCategoryIDTxt = "";
        }

        try {
            int searchCatIDInt = Integer.parseInt(searchCategoryIDTxt);
            List<ProductDTO> proList = dao.getProductListByCategory(searchCatIDInt);
            request.setAttribute("productList", proList);
            
        } catch (NumberFormatException e) {
            request.setAttribute("searchByCategoryError", "Category ID must be an integer");
        }
        RequestDispatcher rd = request.getRequestDispatcher(productViewURL);
            rd.forward(request, response);

    }

    private void addProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAdmin(request)) {
            authorizeAction(request, response);
            return;
        }

        List<ProductExc> validationErrors = new ArrayList<>();
        Integer productID = null;

        String proName = request.getParameter("productName");
        String proPriceString = request.getParameter("productPrice");
        String proQuantityString = request.getParameter("productQuantity");
        String catIDString = request.getParameter("categoryID");
        boolean isEnable = request.getParameter("isEnable") != null;

        //Validate 
        boolean valid = true;
        boolean success = false;
        if (proName == null || proPriceString == null || proQuantityString == null || catIDString == null
                || proName.isEmpty() || proPriceString.isEmpty() || proQuantityString.isEmpty() || catIDString.isEmpty()) {
            validationErrors.add(new ProductExc("Must fill all fields"));
            valid = false;
        } else {
            try {
                BigDecimal price = new BigDecimal(proPriceString);
                int quantity = Integer.parseInt(proQuantityString);
                int catID = Integer.parseInt(catIDString);

                if (categoryMap.get(catID) == null) {
                    validationErrors.add(new ProductExc("CategoryID is not in database"));
                    valid = false;
                }

                if (!Validator.checkProductName(proName)) {
                    validationErrors.add(new ProductExc("Invalid product name"));
                    valid = false;
                }
                if (!Validator.checkPrice(price)) {
                    validationErrors.add(new ProductExc("Price must be between 0 and 100 billions"));
                    valid = false;
                }
                if (!Validator.checkQuantity(quantity)) {
                    validationErrors.add(new ProductExc("Quantity must be between 0 and 1 million"));
                    valid = false;
                }

                //perform insert into dbs
                if (valid) {
                    ProductDTO newPro = new ProductDTO(quantity, price, proName, catID, isEnable);
                    ProductDAO dao = new ProductDAO();
                    productID = dao.createProduct(newPro);
                    if (productID == null) {
                        request.setAttribute("serverErr", "Add unsuccessfully, please try again");
                    } else {
                        success = true;
                    }
                }

            } catch (NumberFormatException e) {
                validationErrors.add(new ProductExc("Price and categoryID and quantity must be number"));
                validationErrors.add(new ProductExc("Price must be between 0 and 100 billions"));
                validationErrors.add(new ProductExc("Quantity must be between 0 and 1 million"));
            }
        }

        if (!valid || !success) {
            request.setAttribute("addValidationErrors", validationErrors);
            request.getRequestDispatcher(productViewURL).forward(request, response);
            return;
        }

        //Success 
        log.info("ProductID:" + productID +" is created");
        response.sendRedirect("product");

    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAdmin(request)) {
            authorizeAction(request, response);
            return;
        }

        List<ProductExc> validationErrors = new ArrayList<>();

        String proIDString = request.getParameter("_productID");
        String proName = request.getParameter("_productName");
        String proPriceString = request.getParameter("_productPrice");
        String proQuantityString = request.getParameter("_productQuantity");
        String catIDString = request.getParameter("_categoryID");
        boolean isEnable = request.getParameter("_isEnable") != null;

        //Validate 
        boolean valid = true;
        boolean success = false;
        if (proName == null || proPriceString == null || proQuantityString == null || catIDString == null
                || proName.isEmpty() || proPriceString.isEmpty() || proQuantityString.isEmpty() || catIDString.isEmpty()) {
            validationErrors.add(new ProductExc("Must fill all fields"));
            valid = false;
        } else {
            try {
                BigDecimal price = new BigDecimal(proPriceString);
                int quantity = Integer.parseInt(proQuantityString);
                int catID = Integer.parseInt(catIDString);
                int proID = Integer.parseInt(proIDString);

                if (!Validator.checkProductName(proName)) {
                    validationErrors.add(new ProductExc("Invalid product name"));
                    valid = false;
                }
                if (!Validator.checkPrice(price)) {
                    validationErrors.add(new ProductExc("Price must be between 0 and 100 billions"));
                    valid = false;
                }
                if (!Validator.checkQuantity(quantity)) {
                    validationErrors.add(new ProductExc("Quantity must be between 0 and 1 million"));
                    valid = false;
                }

                ProductDAO dao = new ProductDAO();

                if (categoryMap.get(catID) == null) {
                    validationErrors.add(new ProductExc("CategoryID is not in database"));
                    valid = false;
                }

                if (dao.getProductByID(proID) == null) {
                    validationErrors.add(new ProductExc("ProID is not in database"));
                    valid = false;
                }

                //perform edit on db
                if (valid) {
                    ProductDTO updatePro = new ProductDTO(proID,quantity, price, proName, catID, isEnable);

                    if (dao.editProduct(updatePro)) {
                        success=true;
                    } else {
                        success = false;
                    }
                }

            } catch (NumberFormatException e) {
                validationErrors.add(new ProductExc("Price and categoryID and quantity must be number"));
                validationErrors.add(new ProductExc("Price must be between 0 and 100 billions"));
                validationErrors.add(new ProductExc("Quantity must be between 0 and 1 million"));
            }
        }

        if (!valid || !success) {
            request.setAttribute("updateValidationErrors", validationErrors);
            request.getRequestDispatcher(productViewURL).forward(request, response);
            return;
        }
        
        //Success
        log.info("ProductID:" + proIDString +" is updated");
        response.sendRedirect("product");

    }

    private void getAllAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAdmin(request)) {
            authorizeAction(request, response);
            return;
        }

        ProductDAO dao = new ProductDAO();
        List<ProductDTO> proList = dao.getProductList();
        request.setAttribute("productList", proList);
        RequestDispatcher rd = request.getRequestDispatcher(productViewURL);
        rd.forward(request, response);
    }

//    private void updateStates(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        if (!isAdmin(request)) {
//            authorizeAction(request, response);
//            return;
//        }
//
//        String[] enabledProIDString = request.getParameterValues("enable");
//        String[] unabledProIDString = request.getParameterValues("unable");
//
//        if (enabledProIDString.length > 0 || unabledProIDString.length > 0) {
//            List<Integer> enableProID = new ArrayList<>();
//            List<Integer> unableProID = new ArrayList<>();
//
//            for (String idString : enabledProIDString) {
//                try {
//                    enableProID.add(Integer.parseInt(idString));
//                } catch (NumberFormatException e) {
//                    response.sendRedirect("/product");
//                    return;
//                }
//            }
//
//            for (String idString : unabledProIDString) {
//                try {
//                    unableProID.add(Integer.parseInt(idString));
//                } catch (NumberFormatException e) {
//                    response.sendRedirect("/product");
//                    return;
//                }
//            }
//
//            ProductDAO dao = new ProductDAO();
//
//            enableProID.forEach((id) -> dao.enableProduct(id));
//            unableProID.forEach((id) -> dao.unableProduct(id));
//        }
//
//        response.sendRedirect("/product");
//    }
    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            return ((UserDTO) session.getAttribute("user")).getRoleID() == 2;
        }
        return false;
    }

    private void authorizeAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("authorizeError", "You are not authorized for the action");
        request.getRequestDispatcher(productViewURL).forward(request, response);
    }

    private void getUnable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAdmin(request)) {
            authorizeAction(request, response);
            return;
        }

        ProductDAO dao = new ProductDAO();
        List<ProductDTO> proList = dao.getUnableProductList();
        request.setAttribute("productList", proList);
        RequestDispatcher rd = request.getRequestDispatcher(productViewURL);
        rd.forward(request, response);

    }

    private void getProductByID(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAdmin(request)) {
            authorizeAction(request, response);
            return;
        }

        String proIDString = request.getParameter("productID");
        if (proIDString == null) {
            response.sendRedirect("product");
            return;
        }

        try {
            int proID = Integer.parseInt(proIDString);
            ProductDAO dao = new ProductDAO();
            ProductDTO product = dao.getProductByID(proID);

            if (product == null) {
                request.setAttribute("getProductByIdError", "No product was found");
                RequestDispatcher rd = request.getRequestDispatcher(productViewURL);
                rd.forward(request, response);
                return;
            }

            request.setAttribute("product", product);

            RequestDispatcher rd = request.getRequestDispatcher(productViewURL);
            rd.forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("product");
        }

    }

    private void updateState(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAdmin(request)) {
            authorizeAction(request, response);
            return;
        }

        String proIDString = request.getParameter("UpdateStateProductID");
        if (proIDString == null) {
            response.sendRedirect("product");
            return;
        }

        try {
            int proID = Integer.parseInt(proIDString);
            ProductDAO dao = new ProductDAO();
            ProductDTO product = dao.getProductByID(proID);

            if (product == null) {
                request.setAttribute("updateStateError", "No product was found");
                RequestDispatcher rd = request.getRequestDispatcher(productViewURL);
                rd.forward(request, response);
                return;
            }

            boolean success = false;
            if (product.isEnable) {
                success = dao.unableProduct(proID);
            } else {
                success = dao.enableProduct(proID);
            }

            if (!success) {
                request.setAttribute("getProductByIdError", "No update was made");
                request.getRequestDispatcher(productViewURL).forward(request, response);
                return;
            }
            
            //Success
            log.info("ProductID:" + proIDString +" state is switched");
            response.sendRedirect("product");
        } catch (NumberFormatException e) {
            response.sendRedirect("product");
        }
    }

}
