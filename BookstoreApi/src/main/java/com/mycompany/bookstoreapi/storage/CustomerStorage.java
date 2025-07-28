/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookstoreapi.storage;

import com.mycompany.bookstoreapi.model.Customer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aathi
 */

public class CustomerStorage {
    private Map<Integer, Customer> customers = new HashMap<>();
    private static CustomerStorage instance = new CustomerStorage();
    private int idCounter = 1;

    private CustomerStorage() {}

    public static CustomerStorage getInstance() {
        return instance;
    }

    public int generateId() {
        return idCounter++;
    }

    public void addCustomer(Customer customer) {
        customers.put(customer.getId(), customer);
    }

    public Customer getCustomerById(int id) {
        return customers.get(id);
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    public void updateCustomer(Customer customer) {
        customers.put(customer.getId(), customer);
    }

    public void deleteCustomer(int id) {
        customers.remove(id);
    }

    public void clearAll() {
        customers.clear();
        idCounter = 1;
    }
}
