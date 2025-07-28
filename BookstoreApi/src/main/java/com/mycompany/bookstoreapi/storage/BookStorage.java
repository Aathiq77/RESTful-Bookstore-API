/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bookstoreapi.storage;

import com.mycompany.bookstoreapi.model.Book;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author aathi
 */

public class BookStorage {
    private Map<Integer, Book> books = new HashMap<>();
    private static BookStorage instance = new BookStorage();
    private int idCounter = 1;
    
    private BookStorage(){}
    
    public static BookStorage getInstance() {
        return instance;
    }

    public int generateId() {
        return idCounter++;
    }

    public void addBook(Book book) {
        books.put(book.getId(), book);
    }

    public Book getBookById(int id) {
        return books.get(id);
    }

    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    public void updateBook(Book book) {
        books.put(book.getId(), book);
    }

    public void deleteBook(int id) {
        books.remove(id);
    }

    public List<Book> getBooksByAuthorId(int authorId) {
        List<Book> authorBooks = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getAuthorId() == authorId) {
                authorBooks.add(book);
            }
        }
        return authorBooks;
    }

    public void clearAll() {
        books.clear();
        idCounter = 1;
    }
    
}
