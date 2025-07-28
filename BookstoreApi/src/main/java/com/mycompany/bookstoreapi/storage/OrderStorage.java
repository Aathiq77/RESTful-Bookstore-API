/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookstoreapi.storage;

import com.mycompany.bookstoreapi.model.Order;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aathi
 */

public class OrderStorage {
    private Map<Integer, List<Order>> orders = new HashMap<>(); // Key is customerId, Value is list of orders
    private static OrderStorage instance = new OrderStorage();
    private int idCounter = 1;

    private OrderStorage() {}

    public static OrderStorage getInstance() {
        return instance;
    }

    public int generateId() {
        return idCounter++;
    }

    public void addOrder(Order order) {
        int customerId = order.getCustomerId();
        orders.computeIfAbsent(customerId, k -> new ArrayList<>()).add(order);
    }

    public List<Order> getOrdersByCustomerId(int customerId) {
        return orders.getOrDefault(customerId, new ArrayList<>());
    }

    public Order getOrderByIdAndCustomerId(int orderId, int customerId) {
        List<Order> customerOrders = orders.getOrDefault(customerId, new ArrayList<>());
        for (Order order : customerOrders) {
            if (order.getId() == orderId) {
                return order;
            }
        }
        return null;
    }

    public void clearAll() {
        orders.clear();
        idCounter = 1;
    }
}
