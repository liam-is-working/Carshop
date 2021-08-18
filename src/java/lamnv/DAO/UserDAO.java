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
import javax.naming.NamingException;
import lamnv.DTO.UserDTO;
import lamnv.Util.DBHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ACER
 */
public class UserDAO {
    private final Logger log = LogManager.getLogger();
    
    public List<UserDTO> getUserList(){
        try(Connection con = DBHelper.getConnection()){
            if(con!=null){
                String queryString = "SELECT UserID, FullName, RoleID "
                        + "FROM dbo.tblUser ";
                try(Statement stm = con.createStatement()){
                    try(ResultSet rs = stm.executeQuery(queryString) ){
                        List<UserDTO> userList = new ArrayList<>();
                        if(rs.next()){
                            UserDTO user = new UserDTO(rs.getNString("UserID"), rs.getNString("FullName"),rs.getInt("RoleID") );
                            userList.add(user);
                        }
                        return userList;
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            log.error(ex);
        }
        return null;
    }
    
    public UserDTO getUser(String userID, String password){
        try(Connection con = DBHelper.getConnection()){
            if(con!=null){
                String queryString = "SELECT FullName, RoleID "
                        + "FROM dbo.tblUser "
                        + "WHERE UserID = ? AND Password = ?";
                try(PreparedStatement stm = con.prepareStatement(queryString)){
                    stm.setString(1, userID);
                    stm.setString(2, password);
                    try(ResultSet rs = stm.executeQuery() ){
                        if(rs.next()){
                            UserDTO returnUser = new UserDTO(userID, rs.getString("FullName"),rs.getInt("RoleID") );
                            return returnUser;
                        }
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            log.error(ex);
        }
        return null;
    }
    
    public boolean checkUserID(String userID){
        try(Connection con = DBHelper.getConnection()){
            if(con!=null){
                String queryString = "SELECT UserID "
                        + "FROM dbo.tblUser "
                        + "WHERE UserID = ?";
                try(PreparedStatement stm = con.prepareStatement(queryString)){
                    stm.setString(1, userID);
                    try(ResultSet rs = stm.executeQuery() ){
                        if(rs.next()){
                            return true;
                        }
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            log.error(ex);
        }
        return false;
    }
    
    //user transaction instead
    public boolean editUser(UserDTO updatedUser){
        try(Connection con = DBHelper.getConnection()){
            if(con!=null){
                String queryString = "UPDATE dbo.tblUser "
                        + "SET Password = ?, FullName = ? "
                        + "WHERE UserID = ?";
                try(PreparedStatement stm = con.prepareStatement(queryString)){
                    stm.setString(1, updatedUser.password);
                    stm.setString(2, updatedUser.fullName);
                    int result = stm.executeUpdate();
                    if(result==1){
                        log.info("Update User with ID: " + updatedUser.userID);
                        return true;
                    }
                    return false;
                    
                }
            }
        } catch (NamingException | SQLException ex) {
            log.error(ex);
        }
        return false;
    }
    
    //Hardcode role
    public UserDTO createUser(UserDTO newUser){
        try(Connection con = DBHelper.getConnection()){
            if(con!=null){
                String queryString = "INSERT INTO dbo.tblUser (UserID, Password, FullName, RoleID) "
                        + "VALUES(?,?,?,1)";
                try(PreparedStatement stm = con.prepareStatement(queryString)){
                    stm.setString(1, newUser.userID);
                    stm.setString(2, newUser.password);
                    stm.setString(3, newUser.fullName);
                    int result = stm.executeUpdate();
                    if(result == 1){
                        log.info("UserID:" + newUser.userID + " is created successfully");
                        return newUser;
                    }
                        
                }
            }
        } catch (NamingException | SQLException ex) {
            log.error(ex);
        }
        return null;
    }
    
    public boolean unableUser(String userID){
        try(Connection con = DBHelper.getConnection()){
            if(con!=null){
                String queryString = "UPDATE dbo.tblUser "
                        + "SET IsEnable = 0 "
                        + "WHERE UserID = ?";
                try(PreparedStatement stm = con.prepareStatement(queryString)){
                    stm.setString(1, userID);
                    int result = stm.executeUpdate();
                    if(result==1){
                        log.info("Unable user with ID:" + userID);
                        return true;
                    }
                    return false;
                }
            }
        } catch (NamingException | SQLException ex) {
            log.error(ex);
        }
        return false;
    }
    
    public boolean enableUser(String userID){
        try(Connection con = DBHelper.getConnection()){
            if(con!=null){
                String queryString = "UPDATE dbo.tblUser "
                        + "SET IsEnable = 1 "
                        + "WHERE UserID = ?";
                try(PreparedStatement stm = con.prepareStatement(queryString)){
                    stm.setString(1, userID);
                    int result = stm.executeUpdate();
                    if(result==1){
                        log.info("Enable user with ID:" + userID);
                        return true;
                    }
                    return false;
                }
            }
        } catch (NamingException | SQLException ex) {
            log.error(ex);
        }
        return false;
    }
}
