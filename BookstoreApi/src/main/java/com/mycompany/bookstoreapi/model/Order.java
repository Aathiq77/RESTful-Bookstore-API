/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookstoreapi.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aathi
 */

@XmlRootElement
public class Order {
    private int id;
    private int customerId;
    private List<CartItem> items;
    private double totalAmount;
    private String orderDate;

    public Order() {
        this.items = new ArrayList<>();
    }

    public Order(int id, int customerId, List<CartItem> items, double totalAmount, String orderDate) {
        this.id = id;
        this.customerId = customerId;
        this.items = items != null ? new ArrayList<>(items): new ArrayList<>();
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
    
    
}
