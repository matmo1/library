package com.booklibrary.backend.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.booklibrary.backend.model.Book;

public interface BookService {
    List<Book> getAllBooks();
    Optional<Book> getBookById(UUID book);
    Book addBook(Book book);
    Optional<Book> updateBook(UUID id, Book bookDetails);
    void deleteBook(UUID id);
}
