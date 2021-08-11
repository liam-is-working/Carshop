/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lamnv.DTO;

import java.math.BigDecimal;

/**
 *
 * @author ACER
 */
public class ProductDTO {
    public int productID;
    public int quantity;

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public ProductDTO() {
    }

    public ProductDTO(int productID, int quantity, BigDecimal price, String productName, int categoryID) {
        this.productID = productID;
        this.quantity = quantity;
        this.price = price;
        this.productName = productName;
        this.categoryID = categoryID;
    }

    public ProductDTO(int quantity, BigDecimal price, String productName, int categoryID) {
        this.quantity = quantity;
        this.price = price;
        this.productName = productName;
        this.categoryID = categoryID;
    }
    public BigDecimal price;
    public String productName;
    public int categoryID;
}
