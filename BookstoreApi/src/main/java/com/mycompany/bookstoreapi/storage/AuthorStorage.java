/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookstoreapi.storage;

import com.mycompany.bookstoreapi.model.Author;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author aathi
 */

public class AuthorStorage {
    private Map<Integer, Author> authors = new HashMap<>();
    private static AuthorStorage instance = new AuthorStorage();
    private int idCounter = 1;

    private AuthorStorage() {}

    public static AuthorStorage getInstance() {
        return instance;
    }

    public int generateId() {
        return idCounter++;
    }

    public void addAuthor(Author author) {
        authors.put(author.getId(), author);
    }

    public Author getAuthorById(int id) {
        return authors.get(id);
    }

    public List<Author> getAllAuthors() {
        return new ArrayList<>(authors.values());
    }

    public void updateAuthor(Author author) {
        authors.put(author.getId(), author);
    }

    public void deleteAuthor(int id) {
        authors.remove(id);
    }

    public void clearAll() {
        authors.clear();
        idCounter = 1;
    }
}
