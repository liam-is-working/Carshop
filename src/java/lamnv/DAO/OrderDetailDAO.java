/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lamnv.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import lamnv.DTO.ProductDTO;
import lamnv.Util.DBHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ACER
 */
public class OrderDetailDAO {
    private final Logger log = LogManager.getLogger();
    public List<ProductDTO> getOrderDetails(int orderID){
        try(Connection con = DBHelper.getConnection()) {
            String sql = "SELECT p.ProductID, ProductName, d.Quantity , Price, CategoryID "
                    + "FROM tblOrderDetail d INNER JOIN tblProduct p "
                    + "ON d.ProductID = p.ProductID "
                    + "WHERE OrderID = ?";
            
            try(PreparedStatement stm = con.prepareStatement(sql)) {
                stm.setInt(1, orderID);
                try (ResultSet rs = stm.executeQuery()){
                    List<ProductDTO> proList = new ArrayList<>();
                    while(rs.next()){
                        proList.add(new ProductDTO(rs.getInt("ProductID"), rs.getInt("Quantity")
                                , rs.getBigDecimal("Price"), rs.getNString("ProductName"), rs.getInt("CategoryID")));
                    }
                    return proList;
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }
}
