/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookstoreapi.resource;

import com.mycompany.bookstoreapi.model.Order;
import com.mycompany.bookstoreapi.model.Book;
import com.mycompany.bookstoreapi.model.Cart;
import com.mycompany.bookstoreapi.model.CartItem;
import com.mycompany.bookstoreapi.storage.BookStorage;
import com.mycompany.bookstoreapi.storage.CartStorage;
import com.mycompany.bookstoreapi.storage.CustomerStorage;
import com.mycompany.bookstoreapi.storage.OrderStorage;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author aathi
 */

@Path("customers/{customerId}/orders")
public class OrderResource {
    
    private OrderStorage orderStorage = OrderStorage.getInstance();
    private CustomerStorage customerStorage = CustomerStorage.getInstance();
    private CartStorage cartStorage = CartStorage.getInstance();
    private BookStorage bookStorage = BookStorage.getInstance();
    private static final Logger LOGGER = Logger.getLogger(OrderResource.class.getName());
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOrder(@PathParam("customerId") int customerId) {
        LOGGER.log(Level.INFO, "Received POST request to create order for customer ID: {0}", customerId);
        // Check if customer exists
        if (customerStorage.getCustomerById(customerId) == null) {
            LOGGER.log(Level.WARNING, "Customer with ID {0} not found for creating order", customerId);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Customer Not Found\", \"message\": \"Customer with ID " + customerId + " does not exist.\"}")
                    .build();
        }
        // Check if cart exists and is not empty
        Cart cart = cartStorage.getCartByCustomerId(customerId);
        if (cart == null || cart.getItems().isEmpty()) {
            LOGGER.log(Level.WARNING, "Cart not found or empty for customer ID: {0} while creating order", customerId);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Cart Not Found or Empty\", \"message\": \"Cart for customer with ID " + customerId + " is empty or does not exist.\"}")
                    .build();
        }
        // Check stock for all items in cart
        for (CartItem item : cart.getItems()) {
            Book book = bookStorage.getBookById(item.getBookId());
            if (book == null) {
                LOGGER.log(Level.WARNING, "Book with ID {0} not found in cart for customer ID {1} while creating order", new Object[]{item.getBookId(), customerId});
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Book Not Found\", \"message\": \"Book with ID " + item.getBookId() + " does not exist.\"}")
                        .build();
            }
            if (book.getStock() < item.getQuantity()) {
                LOGGER.log(Level.WARNING, "Insufficient stock for book ID {0} (requested: {1}, available: {2}) for customer ID {3} while creating order", 
                        new Object[]{item.getBookId(), item.getQuantity(), book.getStock(), customerId});
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Out of Stock\", \"message\": \"Insufficient stock for book with ID " + item.getBookId() + ".\"}")
                        .build();
            }
        }
        // Calculate total amount and update stock
        double totalAmount = 0.0;
        for (CartItem item : cart.getItems()) {
            Book book = bookStorage.getBookById(item.getBookId());
            totalAmount += book.getPrice() * item.getQuantity();
            book.setStock(book.getStock() - item.getQuantity());
            bookStorage.updateBook(book);
            LOGGER.log(Level.INFO, "Updated stock for book ID {0} to {1} after order creation for customer ID {2}", 
                    new Object[]{item.getBookId(), book.getStock(), customerId});
        }
        // Create order
        int orderId = orderStorage.generateId();
        String orderDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Order order = new Order(orderId, customerId, cart.getItems(), totalAmount, orderDate);
        orderStorage.addOrder(order);
        // Clear cart after order creation
        cart.getItems().clear();
        cartStorage.updateCart(cart);
        LOGGER.log(Level.INFO, "Created order with ID {0} for customer ID {1} with total amount {2}", 
                new Object[]{orderId, customerId, totalAmount});
        return Response.status(Response.Status.CREATED).entity(order).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllOrders(@PathParam("customerId") int customerId) {
        LOGGER.log(Level.INFO, "Received GET request for all orders of customer ID: {0}", customerId);
        // Check if customer exists
        if (customerStorage.getCustomerById(customerId) == null) {
            LOGGER.log(Level.WARNING, "Customer with ID {0} not found for retrieving orders", customerId);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Customer Not Found\", \"message\": \"Customer with ID " + customerId + " does not exist.\"}")
                    .build();
        }
        // Retrieve all orders for customer
        List<Order> orders = orderStorage.getOrdersByCustomerId(customerId);
        LOGGER.log(Level.INFO, "Returning {0} orders for customer ID: {1}", new Object[]{orders.size(), customerId});
        return Response.ok(orders).build();
    }

    @GET
    @Path("{orderId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderById(@PathParam("customerId") int customerId, @PathParam("orderId") int orderId) {
        LOGGER.log(Level.INFO, "Received GET request for order ID {0} of customer ID: {1}", new Object[]{orderId, customerId});
        // Check if customer exists
        if (customerStorage.getCustomerById(customerId) == null) {
            LOGGER.log(Level.WARNING, "Customer with ID {0} not found for retrieving order ID {1}", new Object[]{customerId, orderId});
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Customer Not Found\", \"message\": \"Customer with ID " + customerId + " does not exist.\"}")
                    .build();
        }
        // Retrieve order by ID and customer ID
        Order order = orderStorage.getOrderByIdAndCustomerId(orderId, customerId);
        if (order == null) {
            LOGGER.log(Level.WARNING, "Order with ID {0} not found for customer ID {1}", new Object[]{orderId, customerId});
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Order Not Found\", \"message\": \"Order with ID " + orderId + " for customer with ID " + customerId + " does not exist.\"}")
                    .build();
        }
        LOGGER.log(Level.INFO, "Returning order with ID {0} for customer ID: {1}", new Object[]{orderId, customerId});
        return Response.ok(order).build();
    }
}