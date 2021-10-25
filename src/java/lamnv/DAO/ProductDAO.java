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
import lamnv.DTO.ProductDTO;
import lamnv.Util.DBHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author ACER
 */
public class ProductDAO {

    private final Logger log = LogManager.getLogger();

    public List<ProductDTO> getProductListByCategory(int catID) {
        try (Connection con = DBHelper.getConnection()) {
            if (con != null) {
                String queryString = "SELECT ProductID, ProductName, Quantity, Price "
                        + "FROM dbo.tblProduct "
                        + "WHERE CategoryID = ? AND IsEnable = 1";
                try (PreparedStatement stm = con.prepareStatement(queryString)) {
                    stm.setInt(1, catID);
                    try (ResultSet rs = stm.executeQuery()) {
                        stm.setInt(1, catID);
                        List<ProductDTO> productList = new ArrayList<>();
                        while (rs.next()) {
                            ProductDTO product = new ProductDTO(rs.getInt("ProductID"), rs.getInt("Quantity"),
                                    rs.getBigDecimal("Price"), rs.getNString("ProductName"), catID);
                            productList.add(product);
                        }
                        return productList;
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            log.error(ex);
        }
        return null;
    }

    public List<ProductDTO> getProductListByName(String searchName) {
        try (Connection con = DBHelper.getConnection()) {
            if (con != null) {
                String queryString = "SELECT ProductID, ProductName, Quantity, Price, CategoryID "
                        + "FROM dbo.tblProduct "
                        + "WHERE ProductName LIKE ? AND IsEnable = 1";
                try (PreparedStatement stm = con.prepareStatement(queryString)) {
                    stm.setString(1, "%" + searchName + "%");
                    try (ResultSet rs = stm.executeQuery()) {
                        List<ProductDTO> productList = new ArrayList<>();
                        while (rs.next()) {
                            ProductDTO product = new ProductDTO(rs.getInt("ProductID"), rs.getInt("Quantity"),
                                    rs.getBigDecimal("Price"), rs.getNString("ProductName"), rs.getInt("CategoryID"));
                            productList.add(product);
                        }
                        return productList;
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            log.error(ex);
        }
        return null;
    }
    
    public List<ProductDTO> getAllProductByName(String searchName) {
        try (Connection con = DBHelper.getConnection()) {
            if (con != null) {
                String queryString = "SELECT ProductID, ProductName, Quantity, Price, CategoryID, IsEnable "
                        + "FROM dbo.tblProduct "
                        + "WHERE ProductName LIKE ?";
                try (PreparedStatement stm = con.prepareStatement(queryString)) {
                    stm.setString(1, "%" + searchName + "%");
                    try (ResultSet rs = stm.executeQuery()) {
                        List<ProductDTO> productList = new ArrayList<>();
                        while (rs.next()) {
                            ProductDTO product = new ProductDTO(rs.getInt("ProductID"), rs.getInt("Quantity"),
                                    rs.getBigDecimal("Price"), rs.getNString("ProductName"), rs.getInt("CategoryID"), rs.getBoolean("IsEnable"));
                            productList.add(product);
                        }
                        return productList;
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            log.error(ex);
        }
        return null;
    }

    public List<ProductDTO> getProductList() {
        try (Connection con = DBHelper.getConnection()) {
            if (con != null) {
                String queryString = "SELECT ProductID, ProductName, Quantity, Price, CategoryID, IsEnable "
                        + "FROM dbo.tblProduct ";
                try (Statement stm = con.createStatement()) {
                    try (ResultSet rs = stm.executeQuery(queryString)) {
                        List<ProductDTO> productList = new ArrayList<>();
                        while (rs.next()) {
                            ProductDTO product = new ProductDTO(rs.getInt("ProductID"), rs.getInt("Quantity"),
                                    rs.getBigDecimal("Price"), rs.getNString("ProductName"),
                                    rs.getInt("CategoryID"), rs.getBoolean("IsEnable"));
                            productList.add(product);
                        }
                        return productList;
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            log.error(ex);
        }
        return null;
    }

    //product transaction instead
    public boolean editProduct(ProductDTO updatedProduct) {
        try (Connection con = DBHelper.getConnection()) {
            if (con != null) {
                String queryString = "UPDATE dbo.tblProduct "
                        + "SET ProductName = ?, Price = ?, CategoryID = ?, Quantity = ?, isEnable = ? "
                        + "WHERE ProductID = ?";
                try (PreparedStatement stm = con.prepareStatement(queryString)) {
                    stm.setString(1, updatedProduct.productName);
                    stm.setBigDecimal(2, updatedProduct.price);
                    stm.setInt(3, updatedProduct.categoryID);
                    stm.setInt(4, updatedProduct.quantity);
                    stm.setBoolean(5, updatedProduct.isEnable);
                    stm.setInt(6, updatedProduct.productID);
                    int result = stm.executeUpdate();
                    if (result == 1) {
                        log.info("ProductID:" + updatedProduct.productID + " has been updated");
                        return true;
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            log.error(ex);
        }
        return false;
    }

    //Hardcode role
    public Integer createProduct(ProductDTO newProduct) {
        try (Connection con = DBHelper.getConnection()) {
            if (con != null) {
                String queryString = "INSERT INTO dbo.tblProduct (ProductName, Quantity, Price, CategoryID, IsEnable) "
                        + "OUTPUT INSERTED.ProductID "
                        + "VALUES(?,?,?,?,?)";
                try (PreparedStatement stm = con.prepareStatement(queryString)) {
                    stm.setString(1, newProduct.productName);
                    stm.setInt(2, newProduct.quantity);
                    stm.setBigDecimal(3, newProduct.price);
                    stm.setInt(4, newProduct.categoryID);
                    stm.setBoolean(5, newProduct.isEnable);
                    try (ResultSet rs = stm.executeQuery()) {
                        if (rs.next()) {
                            log.info("ProductID:" + rs.getInt("ProductID") + " has been created");
                            return rs.getInt("ProductID");
                        }
                    } catch (Exception e) {
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            log.error(ex);
        }
        return null;
    }

    public boolean unableProduct(int productID) {
        try (Connection con = DBHelper.getConnection()) {
            if (con != null) {
                String queryString = "UPDATE dbo.tblProduct "
                        + "SET IsEnable = 0 "
                        + "WHERE ProductID = ?";
                try (PreparedStatement stm = con.prepareStatement(queryString)) {
                    stm.setInt(1, productID);
                    int result = stm.executeUpdate();
                    if (result == 1) {
                        log.info("ProductID:" + productID + " has been unabled");
                        return true;
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            log.error(ex);
        }
        return false;
    }

    public boolean enableProduct(int productID) {
        try (Connection con = DBHelper.getConnection()) {
            if (con != null) {
                String queryString = "UPDATE dbo.tblProduct "
                        + "SET IsEnable = 1 "
                        + "WHERE ProductID = ?";
                try (PreparedStatement stm = con.prepareStatement(queryString)) {
                    stm.setInt(1, productID);
                    int result = stm.executeUpdate();
                    if (result == 1) {
                        log.info("ProductID:" + productID + " has been enabled");
                        return true;
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            log.error(ex);
        }
        return false;
    }

    public List<ProductDTO> getAvailableProduct() {
        try (Connection con = DBHelper.getConnection()) {
            if (con != null) {
                String queryString = "SELECT ProductID, ProductName, Quantity, Price, CategoryID "
                        + "FROM dbo.tblProduct "
                        + "WHERE IsEnable = 1";
                try (Statement stm = con.createStatement()) {
                    try (ResultSet rs = stm.executeQuery(queryString)) {
                        List<ProductDTO> productList = new ArrayList<>();
                        while (rs.next()) {
                            ProductDTO product = new ProductDTO(rs.getInt("ProductID"), rs.getInt("Quantity"),
                                    rs.getBigDecimal("Price"), rs.getNString("ProductName"), rs.getInt("CategoryID"));
                            productList.add(product);
                        }
                        return productList;
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            log.error(ex);
        }
        return null;
    }

    public List<ProductDTO> getUnableProductList() {
        try (Connection con = DBHelper.getConnection()) {
            if (con != null) {
                String queryString = "SELECT ProductID, ProductName, Quantity, Price, CategoryID "
                        + "FROM dbo.tblProduct "
                        + "WHERE IsEnable = 0";
                try (Statement stm = con.createStatement()) {
                    try (ResultSet rs = stm.executeQuery(queryString)) {
                        List<ProductDTO> productList = new ArrayList<>();
                        while (rs.next()) {
                            ProductDTO product = new ProductDTO(rs.getInt("ProductID"), rs.getInt("Quantity"),
                                    rs.getBigDecimal("Price"), rs.getNString("ProductName"), rs.getInt("CategoryID"));
                            productList.add(product);
                        }
                        return productList;
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            log.error(ex);
        }
        return null;
    }

    public ProductDTO getProductByID(int proID) {
        try (Connection con = DBHelper.getConnection()) {
            if (con != null) {
                String queryString = "SELECT ProductID, ProductName, Quantity, Price, CategoryID, IsEnable "
                        + "FROM dbo.tblProduct "
                        + "WHERE ProductID = ?";
                try (PreparedStatement stm = con.prepareStatement(queryString)) {
                    stm.setInt(1, proID);
                    try (ResultSet rs = stm.executeQuery()) {
                        if (rs.next()) {
                            ProductDTO product = new ProductDTO(rs.getInt("ProductID"), rs.getInt("Quantity"),
                                    rs.getBigDecimal("Price"), rs.getNString("ProductName"),
                                    rs.getInt("CategoryID"), rs.getBoolean("IsEnable"));
                            return product;
                        }
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            log.error(ex);
        }
        return null;
    }

    public ProductDTO getAvailableProductByID(int proID) {
        try (Connection con = DBHelper.getConnection()) {
            if (con != null) {
                String queryString = "SELECT ProductID, ProductName, Quantity, Price, CategoryID "
                        + "FROM dbo.tblProduct "
                        + "WHERE ProductID = ? AND IsEnable=?";
                try (PreparedStatement stm = con.prepareStatement(queryString)) {
                    stm.setInt(1, proID);
                    stm.setBoolean(2, true);
                    try (ResultSet rs = stm.executeQuery()) {
                        if (rs.next()) {
                            ProductDTO product = new ProductDTO(rs.getInt("ProductID"), rs.getInt("Quantity"),
                                    rs.getBigDecimal("Price"), rs.getNString("ProductName"),
                                    rs.getInt("CategoryID"), true);
                            return product;
                        }
                    }
                }
            }
        } catch (NamingException | SQLException ex) {
            log.error(ex);
        }
        return null;
    }
}
