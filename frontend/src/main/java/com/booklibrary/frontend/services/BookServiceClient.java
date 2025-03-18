package com.booklibrary.frontend.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.booklibrary.backend.model.Book;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BookServiceClient {
    private static final String BASE_URL = "http://localhost:8080/api/books";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Book> getAllBooks() {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL))
            .GET()
            .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return mapper.readValue(response.body(), new TypeReference<>() {});
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to fetch books", e);
        }
    }

    public Book addBook(Book book) {
        try {
            String json = mapper.writeValueAsString(book);
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return mapper.readValue(response.body(), Book.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to add book", e);
        }
    }

    public Book updateBook(UUID id, Book book) {
        try {
            String json = mapper.writeValueAsString(book);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + id))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return mapper.readValue(response.body(), Book.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to update book", e);
        }
    }

    public void deleteBook(UUID id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + id))
                    .DELETE()
                    .build();

            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to delete book", e);
        }
    }

    public Optional<Book> getBookById(UUID id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return Optional.of(mapper.readValue(response.body(), Book.class));
            } else {
                return Optional.empty();
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to fetch book", e);
        }
    }
}