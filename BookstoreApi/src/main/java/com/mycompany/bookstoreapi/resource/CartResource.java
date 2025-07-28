/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookstoreapi.resource;

import com.mycompany.bookstoreapi.model.Book;
import com.mycompany.bookstoreapi.model.Cart;
import com.mycompany.bookstoreapi.model.CartItem;
import com.mycompany.bookstoreapi.storage.BookStorage;
import com.mycompany.bookstoreapi.storage.CartStorage;
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
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author aathi
 */

@Path("customers/{customerId}/cart")
public class CartResource {
    
    private CartStorage cartStorage = CartStorage.getInstance();
    private CustomerStorage customerStorage = CustomerStorage.getInstance();
    private BookStorage bookStorage = BookStorage.getInstance();
    private static final Logger LOGGER = Logger.getLogger(CartResource.class.getName());
    
    @POST
    @Path("items")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addItemToCart(@PathParam("customerId") int customerId, CartItem item) {
        LOGGER.log(Level.INFO, "Received POST request to add item to cart for customer ID: {0}, Book ID: {1}", new Object[]{customerId, item.getBookId()});
        // Check if customer exists
        if (customerStorage.getCustomerById(customerId) == null) {
            LOGGER.log(Level.WARNING, "Customer with ID {0} not found for adding item to cart", customerId);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Customer Not Found\", \"message\": \"Customer with ID " + customerId + " does not exist.\"}")
                    .build();
        }
        // Validate quantity
        if (item.getQuantity() <= 0) {
            LOGGER.log(Level.WARNING, "Invalid quantity {0} for book ID {1} in cart for customer ID {2}", new Object[]{item.getQuantity(), item.getBookId(), customerId});
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid Input\", \"message\": \"Quantity must be greater than zero.\"}")
                    .build();
        }
        // Check if book exists and has sufficient stock
        Book book = bookStorage.getBookById(item.getBookId());
        if (book == null) {
            LOGGER.log(Level.WARNING, "Book with ID {0} not found for adding to cart for customer ID {1}", new Object[]{item.getBookId(), customerId});
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Book Not Found\", \"message\": \"Book with ID " + item.getBookId() + " does not exist.\"}")
                    .build();
        }
        if (book.getStock() < item.getQuantity()) {
            LOGGER.log(Level.WARNING, "Insufficient stock for book ID {0} (requested: {1}, available: {2}) for customer ID {3}", 
                    new Object[]{item.getBookId(), item.getQuantity(), book.getStock(), customerId});
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Out of Stock\", \"message\": \"Insufficient stock for book with ID " + item.getBookId() + ".\"}")
                    .build();
        }
        // Add item to cart
        Cart cart = cartStorage.getCartByCustomerId(customerId);
        if (cart == null) {
            cart = new Cart(cartStorage.generateId(), customerId);
            cartStorage.addCart(cart);
            LOGGER.log(Level.INFO, "Created new cart for customer ID: {0}", customerId);
        }
        cartStorage.addItemToCart(customerId, item);
        LOGGER.log(Level.INFO, "Added item (Book ID: {0}, Quantity: {1}) to cart for customer ID: {2}", 
                new Object[]{item.getBookId(), item.getQuantity(), customerId});
        return Response.status(Response.Status.CREATED).entity(item).build();
    }

    // Other endpoints remain unchanged for this update
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCart(@PathParam("customerId") int customerId) {
        LOGGER.log(Level.INFO, "Received GET request for cart of customer ID: {0}", customerId);
        // Check if customer exists
        if (customerStorage.getCustomerById(customerId) == null) {
            LOGGER.log(Level.WARNING, "Customer with ID {0} not found for retrieving cart", customerId);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Customer Not Found\", \"message\": \"Customer with ID " + customerId + " does not exist.\"}")
                    .build();
        }
        // Retrieve cart
        Cart cart = cartStorage.getCartByCustomerId(customerId);
        if (cart == null) {
            LOGGER.log(Level.WARNING, "Cart not found for customer ID: {0}", customerId);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Cart Not Found\", \"message\": \"Cart for customer with ID " + customerId + " does not exist.\"}")
                    .build();
        }
        LOGGER.log(Level.INFO, "Returning cart with {0} items for customer ID: {1}", new Object[]{cart.getItems().size(), customerId});
        return Response.ok(cart).build();
    }

    @PUT
    @Path("items/{bookId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCartItem(@PathParam("customerId") int customerId, @PathParam("bookId") int bookId, CartItem updatedItem) {
        LOGGER.log(Level.INFO, "Received PUT request to update item (Book ID: {0}) in cart for customer ID: {1}", new Object[]{bookId, customerId});
        // Check if customer exists
        if (customerStorage.getCustomerById(customerId) == null) {
            LOGGER.log(Level.WARNING, "Customer with ID {0} not found for updating cart item", customerId);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Customer Not Found\", \"message\": \"Customer with ID " + customerId + " does not exist.\"}")
                    .build();
        }
        // Check if cart exists
        Cart cart = cartStorage.getCartByCustomerId(customerId);
        if (cart == null) {
            LOGGER.log(Level.WARNING, "Cart not found for customer ID: {0} while updating item", customerId);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Cart Not Found\", \"message\": \"Cart for customer with ID " + customerId + " does not exist.\"}")
                    .build();
        }
        // Check if book exists and has sufficient stock
        Book book = bookStorage.getBookById(bookId);
        if (book == null) {
            LOGGER.log(Level.WARNING, "Book with ID {0} not found for updating cart item for customer ID {1}", new Object[]{bookId, customerId});
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Book Not Found\", \"message\": \"Book with ID " + bookId + " does not exist.\"}")
                    .build();
        }
        // Validate quantity
        if (updatedItem.getQuantity() <= 0) {
            LOGGER.log(Level.WARNING, "Invalid quantity {0} for updating book ID {1} in cart for customer ID {2}", new Object[]{updatedItem.getQuantity(), bookId, customerId});
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid Input\", \"message\": \"Quantity must be greater than zero.\"}")
                    .build();
        }
        if (book.getStock() < updatedItem.getQuantity()) {
            LOGGER.log(Level.WARNING, "Insufficient stock for book ID {0} (requested: {1}, available: {2}) for customer ID {3}", 
                    new Object[]{bookId, updatedItem.getQuantity(), book.getStock(), customerId});
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Out of Stock\", \"message\": \"Insufficient stock for book with ID " + bookId + ".\"}")
                    .build();
        }
        // Check if item exists in cart
        boolean itemExists = cart.getItems().stream().anyMatch(i -> i.getBookId() == bookId);
        if (!itemExists) {
            LOGGER.log(Level.WARNING, "Book with ID {0} not found in cart for customer ID {1}", new Object[]{bookId, customerId});
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Book Not Found\", \"message\": \"Book with ID " + bookId + " not found in cart.\"}")
                    .build();
        }
        // Update item in cart
        cartStorage.updateCartItem(customerId, bookId, updatedItem.getQuantity());
        LOGGER.log(Level.INFO, "Updated item (Book ID: {0}, New Quantity: {1}) in cart for customer ID: {2}", 
                new Object[]{bookId, updatedItem.getQuantity(), customerId});
        return Response.ok(updatedItem).build();
    }

    @DELETE
    @Path("items/{bookId}")
    public Response deleteCartItem(@PathParam("customerId") int customerId, @PathParam("bookId") int bookId) {
        LOGGER.log(Level.INFO, "Received DELETE request to remove item (Book ID: {0}) from cart for customer ID: {1}", new Object[]{bookId, customerId});
        // Check if customer exists
        if (customerStorage.getCustomerById(customerId) == null) {
            LOGGER.log(Level.WARNING, "Customer with ID {0} not found for deleting cart item", customerId);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Customer Not Found\", \"message\": \"Customer with ID " + customerId + " does not exist.\"}")
                    .build();
        }
        // Check if cart exists
        Cart cart = cartStorage.getCartByCustomerId(customerId);
        if (cart == null) {
            LOGGER.log(Level.WARNING, "Cart not found for customer ID: {0} while deleting item", customerId);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Cart Not Found\", \"message\": \"Cart for customer with ID " + customerId + " does not exist.\"}")
                    .build();
        }
        // Check if item exists in cart
        boolean itemExists = cart.getItems().stream().anyMatch(i -> i.getBookId() == bookId);
        if (!itemExists) {
            LOGGER.log(Level.WARNING, "Book with ID {0} not found in cart for customer ID {1}", new Object[]{bookId, customerId});
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Book Not Found\", \"message\": \"Book with ID " + bookId + " not found in cart.\"}")
                    .build();
        }
        // Remove item from cart
        cartStorage.deleteCartItem(customerId, bookId);
        LOGGER.log(Level.INFO, "Deleted item (Book ID: {0}) from cart for customer ID: {1}", new Object[]{bookId, customerId});
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}