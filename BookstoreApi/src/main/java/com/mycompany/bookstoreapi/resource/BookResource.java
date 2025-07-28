/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookstoreapi.resource;

import com.mycompany.bookstoreapi.model.Book;
import com.mycompany.bookstoreapi.storage.BookStorage;
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

@Path("books")
public class BookResource {
    private BookStorage bookStorage = BookStorage.getInstance();
    private static final Logger LOGGER = Logger.getLogger(BookResource.class.getName());
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBook(Book book) {
        LOGGER.log(Level.INFO, "Received POST request to create book: {0}", book.getTitle());
        // Validate input
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            LOGGER.log(Level.WARNING, "Invalid input: Title is empty or null for book creation");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid Input\", \"message\": \"Title cannot be empty or null.\"}")
                    .build();
        }
        if (book.getIsbn() == null || book.getIsbn().isEmpty()) {
            LOGGER.log(Level.WARNING, "Invalid input: ISBN is empty or null for book creation");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid Input\", \"message\": \"ISBN cannot be empty or null.\"}")
                    .build();
        }
        if (book.getPrice() <= 0) {
            LOGGER.log(Level.WARNING, "Invalid input: Price is less than or equal to zero for book creation");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid Input\", \"message\": \"Price must be greater than zero.\"}")
                    .build();
        }
        if (book.getStock() < 0) {
            LOGGER.log(Level.WARNING, "Invalid input: Stock is negative for book creation");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid Input\", \"message\": \"Stock cannot be negative.\"}")
                    .build();
        }
        if (book.getPublicationYear() > 2025) {
            LOGGER.log(Level.WARNING, "Invalid input: Publication year is in the future for book creation");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid Input\", \"message\": \"Publication year cannot be in the future.\"}")
                    .build();
        }
        // ISBN length validation
        String isbnCleaned = book.getIsbn().replaceAll("[^0-9X]", ""); 
        if (isbnCleaned.length() != 10 && isbnCleaned.length() != 13) {
            LOGGER.log(Level.WARNING, "Invalid ISBN length for book creation: {0}", book.getIsbn());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid Input\", \"message\": \"ISBN must be 10 or 13 digits long.\"}")
                    .build();
        }
        // Generate ID and save book
        int newId = bookStorage.generateId();
        book.setId(newId);
        bookStorage.addBook(book);
        LOGGER.log(Level.INFO, "Created book with ID: {0}", newId);
        return Response.status(Response.Status.CREATED).entity(book).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBooks() {
        LOGGER.log(Level.INFO, "Received GET request to retrieve all books");
        List<Book> books = bookStorage.getAllBooks();
        LOGGER.log(Level.INFO, "Returning {0} books", books.size());
        return Response.ok(books).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookById(@PathParam("id") int id) {
        LOGGER.log(Level.INFO, "Received GET request for book with ID: {0}", id);
        Book book = bookStorage.getBookById(id);
        if (book == null) {
            LOGGER.log(Level.WARNING, "Book with ID {0} not found", id);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Book Not Found\", \"message\": \"Book with ID " + id + " does not exist.\"}")
                    .build();
        }
        LOGGER.log(Level.INFO, "Returning book with ID: {0}", id);
        return Response.ok(book).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("id") int id, Book updatedBook) {
        LOGGER.log(Level.INFO, "Received PUT request to update book with ID: {0}", id);
        Book existingBook = bookStorage.getBookById(id);
        if (existingBook == null) {
            LOGGER.log(Level.WARNING, "Book with ID {0} not found for update", id);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Book Not Found\", \"message\": \"Book with ID " + id + " does not exist.\"}")
                    .build();
        }
        // Validate input
        if (updatedBook.getTitle() == null || updatedBook.getTitle().isEmpty()) {
            LOGGER.log(Level.WARNING, "Invalid input: Title is empty or null for updating book ID {0}", id);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid Input\", \"message\": \"Title cannot be empty or null.\"}")
                    .build();
        }
        if (updatedBook.getIsbn() == null || updatedBook.getIsbn().isEmpty()) {
            LOGGER.log(Level.WARNING, "Invalid input: ISBN is empty or null for updating book ID {0}", id);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid Input\", \"message\": \"ISBN cannot be empty or null.\"}")
                    .build();
        }
        if (updatedBook.getPrice() <= 0) {
            LOGGER.log(Level.WARNING, "Invalid input: Price is less than or equal to zero for updating book ID {0}", id);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid Input\", \"message\": \"Price must be greater than zero.\"}")
                    .build();
        }
        if (updatedBook.getStock() < 0) {
            LOGGER.log(Level.WARNING, "Invalid input: Stock is negative for updating book ID {0}", id);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid Input\", \"message\": \"Stock cannot be negative.\"}")
                    .build();
        }
        if (updatedBook.getPublicationYear() > 2025) {
            LOGGER.log(Level.WARNING, "Invalid input: Publication year is in the future for updating book ID {0}", id);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid Input\", \"message\": \"Publication year cannot be in the future.\"}")
                    .build();
        }
        // ISBN length validation
        String isbnCleaned = updatedBook.getIsbn().replaceAll("[^0-9X]", ""); // Remove hyphens, spaces, etc.
        if (isbnCleaned.length() != 10 && isbnCleaned.length() != 13) {
            LOGGER.log(Level.WARNING, "Invalid ISBN length for updating book ID {0}: {1}", new Object[]{id, updatedBook.getIsbn()});
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid Input\", \"message\": \"ISBN must be 10 or 13 digits long.\"}")
                    .build();
        }
        updatedBook.setId(id);
        bookStorage.updateBook(updatedBook);
        LOGGER.log(Level.INFO, "Updated book with ID: {0}", id);
        return Response.ok(updatedBook).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteBook(@PathParam("id") int id) {
        LOGGER.log(Level.INFO, "Received DELETE request for book with ID: {0}", id);
        Book book = bookStorage.getBookById(id);
        if (book == null) {
            LOGGER.log(Level.WARNING, "Book with ID {0} not found for deletion", id);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Book Not Found\", \"message\": \"Book with ID " + id + " does not exist.\"}")
                    .build();
        }
        bookStorage.deleteBook(id);
        LOGGER.log(Level.INFO, "Deleted book with ID: {0}", id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
