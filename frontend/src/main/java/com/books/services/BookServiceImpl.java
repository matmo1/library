package com.library.booksFront.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.library.books.model.Book;
import org.springframework.stereotype.Service;

import com.library.books.repository.BookRepository;
import com.library.books.services.BookService;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> getBookById(UUID id) {
        return bookRepository.findById(id);
    }

    @Override
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(UUID id, Book bookDetails) {
        return bookRepository.findById(id).map(existingBook -> {
            existingBook.setTitle(bookDetails.getTitle());
            existingBook.setAuthor(bookDetails.getAuthor());
            existingBook.setIsbn(bookDetails.getIsbn());
            existingBook.setYearPublished(bookDetails.getYearPublished());
            return bookRepository.save(existingBook);
        }).orElseThrow(() -> new RuntimeException("No book found with id: "+bookDetails.getId()));
    }

    @Override
    public void deleteBook(UUID id) {
        bookRepository.deleteById(id);
    }
}
