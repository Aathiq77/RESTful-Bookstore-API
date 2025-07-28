/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookstoreapi.resource;

import com.mycompany.bookstoreapi.model.Author;
import com.mycompany.bookstoreapi.model.Book;
import com.mycompany.bookstoreapi.storage.AuthorStorage;
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

@Path("authors")
public class AuthorResource {
    
    private AuthorStorage authorStorage = AuthorStorage.getInstance();
    private BookStorage bookStorage = BookStorage.getInstance();
    private static final Logger LOGGER = Logger.getLogger(AuthorResource.class.getName());
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAuthor(Author author) {
        LOGGER.log(Level.INFO, "Received POST request to create author: {0} {1}", new Object[]{author.getFirstName(), author.getLastName()});
        // Validate input
        if (author.getFirstName() == null || author.getFirstName().isEmpty() ||
            author.getLastName() == null || author.getLastName().isEmpty()) {
            LOGGER.log(Level.WARNING, "Invalid input data for author creation: {0}", author);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid Input\", \"message\": \"Invalid author data provided.\"}")
                    .build();
        }
        // Generate ID and save author
        int newId = authorStorage.generateId();
        author.setId(newId);
        authorStorage.addAuthor(author);
        LOGGER.log(Level.INFO, "Created author with ID: {0}", newId);
        return Response.status(Response.Status.CREATED).entity(author).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAuthors() {
        LOGGER.log(Level.INFO, "Received GET request to retrieve all authors");
        List<Author> authors = authorStorage.getAllAuthors();
        LOGGER.log(Level.INFO, "Returning {0} authors", authors.size());
        return Response.ok(authors).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthorById(@PathParam("id") int id) {
        LOGGER.log(Level.INFO, "Received GET request for author with ID: {0}", id);
        Author author = authorStorage.getAuthorById(id);
        if (author == null) {
            LOGGER.log(Level.WARNING, "Author with ID {0} not found", id);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Author Not Found\", \"message\": \"Author with ID " + id + " does not exist.\"}")
                    .build();
        }
        LOGGER.log(Level.INFO, "Returning author with ID: {0}", id);
        return Response.ok(author).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAuthor(@PathParam("id") int id, Author updatedAuthor) {
        LOGGER.log(Level.INFO, "Received PUT request to update author with ID: {0}", id);
        Author existingAuthor = authorStorage.getAuthorById(id);
        if (existingAuthor == null) {
            LOGGER.log(Level.WARNING, "Author with ID {0} not found for update", id);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Author Not Found\", \"message\": \"Author with ID " + id + " does not exist.\"}")
                    .build();
        }
        // Validate input
        if (updatedAuthor.getFirstName() == null || updatedAuthor.getFirstName().isEmpty() ||
            updatedAuthor.getLastName() == null || updatedAuthor.getLastName().isEmpty()) {
            LOGGER.log(Level.WARNING, "Invalid input data for updating author ID {0}", id);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Invalid Input\", \"message\": \"Invalid author data provided.\"}")
                    .build();
        }
        updatedAuthor.setId(id);
        authorStorage.updateAuthor(updatedAuthor);
        LOGGER.log(Level.INFO, "Updated author with ID: {0}", id);
        return Response.ok(updatedAuthor).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteAuthor(@PathParam("id") int id) {
        LOGGER.log(Level.INFO, "Received DELETE request for author with ID: {0}", id);
        Author author = authorStorage.getAuthorById(id);
        if (author == null) {
            LOGGER.log(Level.WARNING, "Author with ID {0} not found for deletion", id);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Author Not Found\", \"message\": \"Author with ID " + id + " does not exist.\"}")
                    .build();
        }
        authorStorage.deleteAuthor(id);
        LOGGER.log(Level.INFO, "Deleted author with ID: {0}", id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("{id}/books")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooksByAuthor(@PathParam("id") int id) {
        LOGGER.log(Level.INFO, "Received GET request for books by author with ID: {0}", id);
        Author author = authorStorage.getAuthorById(id);
        if (author == null) {
            LOGGER.log(Level.WARNING, "Author with ID {0} not found for retrieving books", id);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Author Not Found\", \"message\": \"Author with ID " + id + " does not exist.\"}")
                    .build();
        }
        List<Book> books = bookStorage.getBooksByAuthorId(id);
        LOGGER.log(Level.INFO, "Returning {0} books for author ID: {1}", new Object[]{books.size(), id});
        return Response.ok(books).build();
    }
}
