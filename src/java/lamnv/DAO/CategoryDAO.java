/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lamnv.DAO;

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
import lamnv.DTO.CategoryDTO;
import lamnv.DTO.CategoryDTO;
import lamnv.Util.DBHelper;

/**
 *
 * @author ACER
 */
public class CategoryDAO {
    
    public List<CategoryDTO> getCategoryList(){
        try(Connection con = DBHelper.getConnection()){
            if(con!=null){
                String queryString = "SELECT CategoryID, CategoryName "
                        + "FROM dbo.tblCategory ";
                try(Statement stm = con.createStatement()){
                    try(ResultSet rs = stm.executeQuery(queryString) ){
                        List<CategoryDTO> categoryList = new ArrayList<>();
                        while(rs.next()){
                            CategoryDTO category = new CategoryDTO(rs.getInt("CategoryID"), rs.getNString("CategoryName"));
                            categoryList.add(category);
                        }
                        return categoryList;
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
        
//    public CategoryDTO getCategory(String categoryID, String CategoryName){
//        try(Connection con = DBHelper.getConnection()){
//            if(con!=null){
//                String queryString = "SELECT FullName, RoleID "
//                        + "FROM dbo.tblCategory "
//                        + "WHERE CategoryID = ? AND Password = ?";
//                try(PreparedStatement stm = con.prepareStatement(queryString)){
//                    stm.setString(1, categoryID);
//                    stm.setString(2, password);
//                    try(ResultSet rs = stm.executeQuery() ){
//                        if(rs.next()){
//                            CategoryDTO returnCategory = new CategoryDTO(categoryID, rs.getNString("FullName"),rs.getInt("RoleID") );
//                            return returnCategory;
//                        }
//                    }
//                }
//            }
//        } catch (NamingException | SQLException ex) {
//            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
    
//    public boolean checkCategoryID(String categoryID){
//        try(Connection con = DBHelper.getConnection()){
//            if(con!=null){
//                String queryString = "SELECT CategoryID "
//                        + "FROM dbo.tblCategory "
//                        + "WHERE CategoryID = ?";
//                try(PreparedStatement stm = con.prepareStatement(queryString)){
//                    stm.setString(1, categoryID);
//                    try(ResultSet rs = stm.executeQuery() ){
//                        if(rs.next()){
//                            return true;
//                        }
//                    }
//                }
//            }
//        } catch (NamingException | SQLException ex) {
//            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return false;
//    }
    
    //category transaction instead
    public boolean editCategory(CategoryDTO updatedCategory){
        try(Connection con = DBHelper.getConnection()){
            if(con!=null){
                String queryString = "UPDATE dbo.tblCategory "
                        + "SET CategoryName = ? "
                        + "WHERE CategoryID = ?";
                try(PreparedStatement stm = con.prepareStatement(queryString)){
                    stm.setString(1, updatedCategory.categoryName);
                    stm.setInt(2, updatedCategory.categoryID);
                    int result = stm.executeUpdate();
                    return result == 1;
                }
            }
        } catch (NamingException | SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    //Hardcode role
    public CategoryDTO createCategory(CategoryDTO newCategory){
        try(Connection con = DBHelper.getConnection()){
            if(con!=null){
                String queryString = "INSERT INTO dbo.tblCategory (CategoryName) "
                        + "VALUES(?)";
                try(PreparedStatement stm = con.prepareStatement(queryString)){
                    stm.setString(1, newCategory.categoryName);
                    int result = stm.executeUpdate();
                    if(result == 1)
                        return newCategory;
                }
            }
        } catch (NamingException | SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public boolean unableCategory(int categoryID){
        try(Connection con = DBHelper.getConnection()){
            if(con!=null){
                String queryString = "UPDATE dbo.tblCategory "
                        + "SET IsEnable = 0 "
                        + "WHERE CategoryID = ?";
                try(PreparedStatement stm = con.prepareStatement(queryString)){
                    stm.setInt(1, categoryID);
                    int result = stm.executeUpdate();
                    return result == 1;
                }
            }
        } catch (NamingException | SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean enableCategory(int categoryID){
        try(Connection con = DBHelper.getConnection()){
            if(con!=null){
                String queryString = "UPDATE dbo.tblCategory "
                        + "SET IsEnable = 1 "
                        + "WHERE CategoryID = ?";
                try(PreparedStatement stm = con.prepareStatement(queryString)){
                    stm.setInt(1, categoryID);
                    int result = stm.executeUpdate();
                    return result == 1;
                }
            }
        } catch (NamingException | SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public List<CategoryDTO> getAvailableCategory() {
        try(Connection con = DBHelper.getConnection()){
            if(con!=null){
                String queryString = "SELECT CategoryID, CategoryName "
                        + "FROM dbo.tblCategory "
                        + "WHERE IsEnable = 1";
                try(Statement stm = con.createStatement()){
                    try(ResultSet rs = stm.executeQuery(queryString) ){
                        List<CategoryDTO> categoryList = new ArrayList<>();
                        while(rs.next()){
                            CategoryDTO category = new CategoryDTO(rs.getInt("CategoryID"), rs.getNString("CategoryName"));
                            categoryList.add(category);
                        }
                        return categoryList;
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            Logger.getLogger(CategoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
