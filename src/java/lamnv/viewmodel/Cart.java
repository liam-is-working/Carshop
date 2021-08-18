/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lamnv.viewmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import lamnv.DTO.ProductDTO;

/**
 *
 * @author ACER
 */
public class Cart extends HashMap<ProductDTO, Integer> implements Serializable{
//    public BigDecimal getTotalPrice(){
//        BigDecimal total = new BigDecimal(0);
//        this.forEach((pro, quan) -> {
//            total.add(pro.getPrice().multiply(new BigDecimal(quan)));
//        });
//        return total;
//    }

    public Cart(List<ProductDTO> productList) {
        productList.forEach(p -> this.addProduct(p, p.quantity));
    }

    public Cart() {
    }

    public BigDecimal totalPrice = new BigDecimal(0);

    public void addProduct(ProductDTO product, int quantity) {
        if (this.containsKey(product)) {
            this.put(product, quantity + this.get(product));
        } else {
            this.put(product, quantity);
        }
        totalPrice = totalPrice.add(product.price.multiply(new BigDecimal(quantity)));
    }

    public void deleteProduct(ProductDTO product, int quantity) {

        Integer currentQuantity = this.get(product);

        if (currentQuantity != null) {
            if (quantity == -1) {
                this.remove(product);
                totalPrice = totalPrice.subtract(product.price.multiply(new BigDecimal(currentQuantity)));
                return;
            }
            if (currentQuantity - quantity <= 0) {
                this.remove(product);
                totalPrice = totalPrice.subtract(product.price.multiply(new BigDecimal(currentQuantity)));
            } else {
                this.put(product, currentQuantity - quantity);
                totalPrice = totalPrice.subtract(product.price.multiply(new BigDecimal(quantity)));
            }

        }
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

}
