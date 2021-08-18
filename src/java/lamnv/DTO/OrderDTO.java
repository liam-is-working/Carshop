/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lamnv.DTO;

import java.sql.Timestamp;

/**
 *
 * @author ACER
 */
public class OrderDTO {
    public int orderID;

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String userID;
    public Timestamp orderDate;
    public String address;

    public OrderDTO() {
    }

    public OrderDTO(int orderID, String userID, Timestamp orderDate, String address) {
        this.orderID = orderID;
        this.userID = userID;
        this.orderDate = orderDate;
        this.address = address;
    }

    public OrderDTO(int orderID, String userID, Timestamp orderDate) {
        this.orderID = orderID;
        this.userID = userID;
        this.orderDate = orderDate;
    }
    
    
}
