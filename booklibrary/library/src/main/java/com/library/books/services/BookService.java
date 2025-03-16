package com.library.books.services;

import com.library.books.model.Book;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookService {
    List<Book> getAllBooks();
    Optional<Book> getBookById(UUID book);
    Book addBook(Book book);
    Optional<Book> updateBook(UUID id, Book bookDetails);
    void deleteBook(UUID id);
}
