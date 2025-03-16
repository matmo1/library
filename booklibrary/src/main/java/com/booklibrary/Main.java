package com.booklibrary;

import com.books.BooksFrontApplication;
import com.library.books.BooksApplication;
import org.springframework.boot.SpringApplication;

public class Main {
    public static void main(String[] args) {
        new Thread(() -> {
            SpringApplication.run(BooksApplication.class, args);
        }).start();

        BooksFrontApplication.main(args);
    }
}