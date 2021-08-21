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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.naming.NamingException;
import lamnv.DTO.OrderDTO;
import lamnv.DTO.ProductDTO;
import lamnv.DTO.UserDTO;
import lamnv.Util.DBHelper;
import lamnv.exception.OrderExc;
import lamnv.viewmodel.Cart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ACER
 */
public class OrderDAO {

    private final Logger log = LogManager.getLogger();

    public UserDTO getOrderOwner(int orderID) {
        try (Connection con = DBHelper.getConnection()) {
            String sql = "SELECT o.UserID, RoleID "
                    + "FROM tblOrder o INNER JOIN tblUser u "
                    + "ON o.UserID = u.UserID "
                    + "WHERE OrderID = ?";

            try (PreparedStatement stm = con.prepareStatement(sql)) {
                stm.setInt(1, orderID);
                try (ResultSet rs = stm.executeQuery()) {
                    if (rs.next()) {
                        UserDTO user = new UserDTO();
                        user.setRoleID(rs.getInt("RoleID"));
                        user.setUserID(rs.getNString("UserID"));
                        return user;
                    }

                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    public List<OrderDTO> getCustomerOrders(String userID) {
        try (Connection con = DBHelper.getConnection()) {
            String sql = "SELECT OrderID, OrderDate, Address, IsVerified "
                    + "FROM tblOrder "
                    + "WHERE UserID = ?";

            try (PreparedStatement stm = con.prepareStatement(sql)) {
                stm.setNString(1, userID);
                try (ResultSet rs = stm.executeQuery()) {
                    List<OrderDTO> orderList = new ArrayList<>();
                    while (rs.next()) {
                        orderList.add(new OrderDTO(rs.getInt("OrderID"), userID, rs.getTimestamp("OrderDate"),
                                 rs.getNString("Address"), rs.getBoolean("IsVerified")));
                    }
                    return orderList;
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    public Integer createOrder(Cart cart, UserDTO userInf, String address, Timestamp orderTime) throws OrderExc {
        try (Connection con = DBHelper.getConnection()) {
            boolean success = true;
            String updateProduct = "UPDATE tblProduct "
                    + "SET Quantity = Quantity - ? "
                    + "WHERE ProductID = ? AND IsEnable = ?";
            String updateOrder = "INSERT INTO tblOrder(UserID, Address, OrderDate ) "
                    + "OUTPUT INSERTED.OrderID "
                    + "VALUES(?,?,?)";
            String updateOrderDetail = "INSERT INTO tblOrderDetail(OrderID, ProductID, Quantity) "
                    + "VALUES(?,?,?)";
            try (PreparedStatement productStm = con.prepareStatement(updateProduct);
                    PreparedStatement orderStm = con.prepareStatement(updateOrder);
                    PreparedStatement orderDetailStm = con.prepareStatement(updateOrderDetail)) {
                con.setAutoCommit(false);
                //Savepoint before = con.setSavepoint();

                //insert new order
                int orderID = 0;
                orderStm.setNString(1, userInf.getUserID());
                orderStm.setNString(2, address);
                orderStm.setTimestamp(3, orderTime);
                try (ResultSet rs = orderStm.executeQuery();) {
                    if (rs.next()) {
                        orderID = rs.getInt("OrderID");
                    }
                }

                //update product
                productStm.setBoolean(3, true);
                for (ProductDTO pro : cart.keySet()) {
                    productStm.setInt(1, cart.get(pro));
                    productStm.setInt(2, pro.productID);
                    productStm.addBatch();
                }
                int[] productResult = productStm.executeBatch();

                for (int r : productResult) {
                    if (r == 0) {
                        success = false;
                    }
                }

                if (success) {
                    orderDetailStm.setInt(1, orderID);
                    for (ProductDTO pro : cart.keySet()) {
                        orderDetailStm.setInt(2, pro.productID);
                        orderDetailStm.setInt(3, cart.get(pro));
                        orderDetailStm.addBatch();
                    }
                    int[] orderDetailResult = orderDetailStm.executeBatch();

                    for (int r : orderDetailResult) {
                        if (r == 0) {
                            success = false;
                        }
                    }
                }

                if (success) {
                    con.commit();
                    log.info("OrderID: " + orderID + " is created");
                    return orderID;
                } else {
                    throw new OrderExc("Attemp to order an unabled product");
                }

            }
        } catch (NamingException | SQLException ex) {
            log.error(ex);
            if (ex.getMessage().contains("CHK_ProductQuantity")) {
                throw new OrderExc("Not enought products in stock");
            }
        }
        return null;
    }

    public List<OrderDTO> AllOrder() {
        try (Connection con = DBHelper.getConnection()) {
            String sql = "SELECT OrderID, OrderDate, Address, UserID, IsVerified "
                    + "FROM tblOrder ";

            try (PreparedStatement stm = con.prepareStatement(sql)) {
                try (ResultSet rs = stm.executeQuery()) {
                    List<OrderDTO> orderList = new ArrayList<>();
                    while (rs.next()) {
                        orderList.add(new OrderDTO(rs.getInt("OrderID"), rs.getNString("UserID"),
                                rs.getTimestamp("OrderDate"), rs.getNString("Address"), rs.getBoolean("IsVerified")));
                    }
                    return orderList;
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    public boolean verifyOrder(int orderID) {
        try (Connection con = DBHelper.getConnection()) {
            String sql = "UPDATE tblOrder "
                    + "SET IsVerified = 1"
                    + "WHERE OrderID = ?";

            try (PreparedStatement stm = con.prepareStatement(sql)) {
                stm.setInt(1, orderID);
                return stm.executeUpdate() == 1;
            }
        } catch (NamingException | SQLException ex) {
            log.error(ex);
        }
        return false;
    }
}

