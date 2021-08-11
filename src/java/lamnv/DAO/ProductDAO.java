/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lamnv.DAO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import lamnv.DTO.ProductDTO;
import lamnv.Util.DBHelper;

/**
 *
 * @author ACER
 */
public class ProductDAO {
    
    public List<ProductDTO> getProductListByCategory(int catID){
        try(Connection con = DBHelper.getConnection()){
            if(con!=null){
                String queryString = "SELECT ProductID, ProductName, Quantity, Price "
                        + "FROM dbo.tblProduct "
                        + "WHERE CategoryID = ?";
                try(PreparedStatement stm = con.prepareStatement(queryString)){
                    stm.setInt(1, catID);
                    try(ResultSet rs = stm.executeQuery(queryString) ){
                        List<ProductDTO> productList = new ArrayList<>();
                        if(rs.next()){
                            ProductDTO product = new ProductDTO(rs.getInt("ProductID"), rs.getInt("Quantity"),
                                    rs.getBigDecimal("Price"), rs.getNString("ProductName"), catID);
                            productList.add(product);
                        }
                        return productList;
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public List<ProductDTO> getProductList(){
        try(Connection con = DBHelper.getConnection()){
            if(con!=null){
                String queryString = "SELECT ProductID, ProductName, Quantity, Price, CategoryID "
                        + "FROM dbo.tblProduct ";
                try(Statement stm = con.createStatement()){
                    try(ResultSet rs = stm.executeQuery(queryString) ){
                        List<ProductDTO> productList = new ArrayList<>();
                        if(rs.next()){
                            ProductDTO product = new ProductDTO(rs.getInt("ProductID"), rs.getInt("Quantity"),
                                    rs.getBigDecimal("Price"), rs.getNString("ProductName"), rs.getInt("CategoryID"));
                            productList.add(product);
                        }
                        return productList;
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
        
//    public ProductDTO getProduct(String productID, String ProductName){
//        try(Connection con = DBHelper.getConnection()){
//            if(con!=null){
//                String queryString = "SELECT FullName, RoleID "
//                        + "FROM dbo.tblProduct "
//                        + "WHERE ProductID = ? AND Password = ?";
//                try(PreparedStatement stm = con.prepareStatement(queryString)){
//                    stm.setString(1, productID);
//                    stm.setString(2, password);
//                    try(ResultSet rs = stm.executeQuery() ){
//                        if(rs.next()){
//                            ProductDTO returnProduct = new ProductDTO(productID, rs.getNString("FullName"),rs.getInt("RoleID") );
//                            return returnProduct;
//                        }
//                    }
//                }
//            }
//        } catch (NamingException | SQLException ex) {
//            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
    
//    public boolean checkProductID(String productID){
//        try(Connection con = DBHelper.getConnection()){
//            if(con!=null){
//                String queryString = "SELECT ProductID "
//                        + "FROM dbo.tblProduct "
//                        + "WHERE ProductID = ?";
//                try(PreparedStatement stm = con.prepareStatement(queryString)){
//                    stm.setString(1, productID);
//                    try(ResultSet rs = stm.executeQuery() ){
//                        if(rs.next()){
//                            return true;
//                        }
//                    }
//                }
//            }
//        } catch (NamingException | SQLException ex) {
//            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return false;
//    }
    
    //product transaction instead
    public boolean editProduct(ProductDTO updatedProduct){
        try(Connection con = DBHelper.getConnection()){
            if(con!=null){
                String queryString = "UPDATE dbo.tblProduct "
                        + "SET ProductName = ?, Price = ?, CategoryID = ?, Quantity = ? "
                        + "WHERE ProductID = ?";
                try(PreparedStatement stm = con.prepareStatement(queryString)){
                    stm.setString(1, updatedProduct.productName);
                    stm.setBigDecimal(2, updatedProduct.price);
                    stm.setInt(3, updatedProduct.categoryID);
                    stm.setInt(4, updatedProduct.quantity);
                    int result = stm.executeUpdate();
                    return result == 1;
                }
            }
        } catch (NamingException | SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    //Hardcode role
    public ProductDTO createProduct(ProductDTO newProduct){
        try(Connection con = DBHelper.getConnection()){
            if(con!=null){
                String queryString = "INSERT INTO dbo.tblProduct (ProductName) "
                        + "VALUES(?)";
                try(PreparedStatement stm = con.prepareStatement(queryString)){
                    stm.setString(1, newProduct.productName);
                    int result = stm.executeUpdate();
                    if(result == 1)
                        return newProduct;
                }
            }
        } catch (NamingException | SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public boolean unableProduct(int productID){
        try(Connection con = DBHelper.getConnection()){
            if(con!=null){
                String queryString = "UPDATE dbo.tblProduct "
                        + "SET IsEnable = 0 "
                        + "WHERE ProductID = ?";
                try(PreparedStatement stm = con.prepareStatement(queryString)){
                    stm.setInt(1, productID);
                    int result = stm.executeUpdate();
                    return result == 1;
                }
            }
        } catch (NamingException | SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean enableProduct(int productID){
        try(Connection con = DBHelper.getConnection()){
            if(con!=null){
                String queryString = "UPDATE dbo.tblProduct "
                        + "SET IsEnable = 1 "
                        + "WHERE ProductID = ?";
                try(PreparedStatement stm = con.prepareStatement(queryString)){
                    stm.setInt(1, productID);
                    int result = stm.executeUpdate();
                    return result == 1;
                }
            }
        } catch (NamingException | SQLException ex) {
            Logger.getLogger(ProductDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
