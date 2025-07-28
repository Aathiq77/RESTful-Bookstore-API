/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookstoreapi.resource;

import com.mycompany.bookstoreapi.model.Customer;
import com.mycompany.bookstoreapi.storage.CustomerStorage;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author aathi
 */

@Path("customers")
public class CustomerResource {
        
    private CustomerStorage customerStorage = CustomerStorage.getInstance();
    private static final Logger LOGGER = Logger.getLogger(CustomerResource.class.getName());
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCustomer(Customer customer) {
        LOGGER.log(Level.INFO, "Received POST request to create customer: {0}", customer.getFirstName());
        // Validate input with specific error messages
        StringBuilder errorMessage = new StringBuilder();
        boolean hasError = false;
        
        if (customer.getFirstName() == null || customer.getFirstName().isEmpty()) {
            errorMessage.append("First name cannot be empty or null. ");
            hasError = true;
        }
        if (customer.getLastName() == null || customer.getLastName().isEmpty()) {
            errorMessage.append("Last name cannot be empty or null. ");
            hasError = true;
        }
        if (customer.getEmail() == null || customer.getEmail().isEmpty()) {
            errorMessage.append("Email cannot be empty or null. ");
            hasError = true;
        }
        if (customer.getPassword() == null || customer.getPassword().isEmpty()) {
            errorMessage.append("Password cannot be empty or null. ");
            hasError = true;
        }
        
        if (hasError) {
            LOGGER.log(Level.WARNING, "Invalid input for customer creation: {0}", errorMessage.toString());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid Input\", \"message\": \"" + errorMessage.toString().trim() + "\"}")
                    .build();
        }
        // Generate ID and save customer
        int newId = customerStorage.generateId();
        customer.setId(newId);
        customerStorage.addCustomer(customer);
        LOGGER.log(Level.INFO, "Created customer with ID: {0}", newId);
        return Response.status(Response.Status.CREATED).entity(customer).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCustomers() {
        LOGGER.log(Level.INFO, "Received GET request to retrieve all customers");
        List<Customer> customers = customerStorage.getAllCustomers();
        LOGGER.log(Level.INFO, "Returning {0} customers", customers.size());
        return Response.ok(customers).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerById(@PathParam("id") int id) {
        LOGGER.log(Level.INFO, "Received GET request for customer with ID: {0}", id);
        Customer customer = customerStorage.getCustomerById(id);
        if (customer == null) {
            LOGGER.log(Level.WARNING, "Customer with ID {0} not found", id);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Customer Not Found\", \"message\": \"Customer with ID " + id + " does not exist.\"}")
                    .build();
        }
        LOGGER.log(Level.INFO, "Returning customer with ID: {0}", id);
        return Response.ok(customer).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomer(@PathParam("id") int id, Customer updatedCustomer) {
        LOGGER.log(Level.INFO, "Received PUT request to update customer with ID: {0}", id);
        Customer existingCustomer = customerStorage.getCustomerById(id);
        if (existingCustomer == null) {
            LOGGER.log(Level.WARNING, "Customer with ID {0} not found for update", id);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Customer Not Found\", \"message\": \"Customer with ID " + id + " does not exist.\"}")
                    .build();
        }
        // Validate input with specific error messages
        StringBuilder errorMessage = new StringBuilder();
        boolean hasError = false;
        
        if (updatedCustomer.getFirstName() == null || updatedCustomer.getFirstName().isEmpty()) {
            errorMessage.append("First name cannot be empty or null. ");
            hasError = true;
        }
        if (updatedCustomer.getLastName() == null || updatedCustomer.getLastName().isEmpty()) {
            errorMessage.append("Last name cannot be empty or null. ");
            hasError = true;
        }
        if (updatedCustomer.getEmail() == null || updatedCustomer.getEmail().isEmpty()) {
            errorMessage.append("Email cannot be empty or null. ");
            hasError = true;
        }
        if (updatedCustomer.getPassword() == null || updatedCustomer.getPassword().isEmpty()) {
            errorMessage.append("Password cannot be empty or null. ");
            hasError = true;
        }
        
        if (hasError) {
            LOGGER.log(Level.WARNING, "Invalid input for updating customer ID {0}: {1}", new Object[]{id, errorMessage.toString()});
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid Input\", \"message\": \"" + errorMessage.toString().trim() + "\"}")
                    .build();
        }
        updatedCustomer.setId(id);
        customerStorage.updateCustomer(updatedCustomer);
        LOGGER.log(Level.INFO, "Updated customer with ID: {0}", id);
        return Response.ok(updatedCustomer).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        LOGGER.log(Level.INFO, "Received DELETE request for customer with ID: {0}", id);
        Customer customer = customerStorage.getCustomerById(id);
        if (customer == null) {
            LOGGER.log(Level.WARNING, "Customer with ID {0} not found for deletion", id);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Customer Not Found\", \"message\": \"Customer with ID " + id + " does not exist.\"}")
                    .build();
        }
        customerStorage.deleteCustomer(id);
        LOGGER.log(Level.INFO, "Deleted customer with ID: {0}", id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
