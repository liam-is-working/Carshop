/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lamnv.DTO;

/**
 *
 * @author ACER
 */
public class UserDTO {

    public UserDTO(String userID, String password, String fullName) {
        this.userID = userID;
        this.password = password;
        this.fullName = fullName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleID() {
        return RoleID;
    }

    public void setRoleID(int RoleID) {
        this.RoleID = RoleID;
    }

    public UserDTO(String userID, String fullName, int RoleID) {
        this.userID = userID;
        this.fullName = fullName;
        this.RoleID = RoleID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public UserDTO(){}
    public String userID;
    public String password;
    public int RoleID;
    public String fullName;
}
