/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookstoreapi.storage;

import com.mycompany.bookstoreapi.model.Cart;
import com.mycompany.bookstoreapi.model.CartItem;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author aathi
 */

public class CartStorage {
    private Map<Integer, Cart> carts = new HashMap<>(); // Key is customerId
    private static CartStorage instance = new CartStorage();
    private int idCounter = 1;

    private CartStorage() {}

    public static CartStorage getInstance() {
        return instance;
    }

    public int generateId() {
        return idCounter++;
    }

    public void addCart(Cart cart) {
        carts.put(cart.getCustomerId(), cart);
    }

    public Cart getCartByCustomerId(int customerId) {
        return carts.get(customerId);
    }

    public void updateCart(Cart cart) {
        carts.put(cart.getCustomerId(), cart);
    }

    public void deleteCart(int customerId) {
        carts.remove(customerId);
    }

    public void addItemToCart(int customerId, CartItem item) {
        Cart cart = carts.get(customerId);
        if (cart != null) {
            // Check if item already exists in cart
            for (CartItem existingItem : cart.getItems()) {
                if (existingItem.getBookId() == item.getBookId()) {
                    existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                    return;
                }
            }
            // If item doesn't exist, add it
            cart.getItems().add(item);
        }
    }

    public void updateCartItem(int customerId, int bookId, int quantity) {
        Cart cart = carts.get(customerId);
        if (cart != null) {
            for (CartItem item : cart.getItems()) {
                if (item.getBookId() == bookId) {
                    item.setQuantity(quantity);
                    return;
                }
            }
        }
    }

    public void deleteCartItem(int customerId, int bookId) {
        Cart cart = carts.get(customerId);
        if (cart != null) {
            cart.getItems().removeIf(item -> item.getBookId() == bookId);
        }
    }

    public void clearAll() {
        carts.clear();
        idCounter = 1;
    }
}
