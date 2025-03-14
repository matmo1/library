package com.library.books.services;

import com.library.books.model.Book;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookService {
    List<Book> getAllBooks();
    Optional<Book> getBookById(UUID book);
    Book addBook(Book book);
    Book updateBook(UUID id, Book book);
    void deleteBook(UUID id);
}
