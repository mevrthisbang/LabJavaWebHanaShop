/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phuchgt.dto;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author mevrthisbang
 */
public class CartObj implements Serializable {

    private String customer;
    private HashMap<String, FoodDTO> cart;

    public float getTotal() {
        float result = 0;
        for (FoodDTO food : this.cart.values()) {
            result += food.getPrice() * food.getQuantity();
        }
        return result;
    }

    public CartObj(String customer) {
        this.customer = customer;
        this.cart = new HashMap<>();
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public HashMap<String, FoodDTO> getCart() {
        return cart;
    }

    public void addToCart(FoodDTO food) throws Exception {
        food.setQuantity(1);
        if (this.cart.containsKey(food.getId())) {
            int quantity = this.cart.get(food.getId()).getQuantity();
            food.setQuantity(quantity + 1);
        }
        this.cart.put(food.getId(), food);
    }

    public void removeFromCart(String foodID) throws Exception {
        if (this.cart.containsKey(foodID)) {
            cart.remove(foodID);
        }
    }

    public void updateCart(String foodID, int quantity) throws Exception {
        if(this.cart.containsKey(foodID)){
            this.cart.get(foodID).setQuantity(quantity);
            this.cart.get(foodID).setDescription("");
        }
    }

}
