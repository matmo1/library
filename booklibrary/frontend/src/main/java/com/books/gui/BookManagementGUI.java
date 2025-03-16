package com.books.gui;

import com.library.books.model.Book;
import com.books.services.BookServiceClient;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.UUID;

public class BookManagementGUI extends JFrame {
    private final BookServiceClient bookServiceClient;
    private JTable bookTable;

    public BookManagementGUI(BookServiceClient bookServiceClient) {
        this.bookServiceClient = bookServiceClient;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Book Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a main panel with a BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        // Add a table to display books
        bookTable = createBookTable();
        JScrollPane scrollPane = new JScrollPane(bookTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add buttons for actions
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Book");
        JButton editButton = new JButton("Edit Book");
        JButton deleteButton = new JButton("Delete Book");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners for buttons
        addButton.addActionListener(e -> openAddBookDialog());
        editButton.addActionListener(e -> openEditBookDialog(bookTable));
        deleteButton.addActionListener(e -> deleteBook(bookTable));
    }

    private JTable createBookTable() {
        // Fetch books from the service client
        List<Book> books = bookServiceClient.getAllBooks();

        // Define column names
        String[] columnNames = {"ID", "Title", "Author", "ISBN", "Year Published"};

        // Create a 2D array for the table data
        Object[][] data = new Object[books.size()][5];
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            data[i][0] = book.getId();
            data[i][1] = book.getTitle();
            data[i][2] = book.getAuthor();
            data[i][3] = book.getIsbn();
            data[i][4] = book.getYearPublished();
        }

        // Create and return the table
        return new JTable(data, columnNames);
    }

    private void openAddBookDialog() {
        // Create a dialog for adding a new book
        JDialog addDialog = new JDialog(this, "Add Book", true);
        addDialog.setSize(400, 300);
        addDialog.setLayout(new GridLayout(5, 2));

        // Add input fields
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField isbnField = new JTextField();
        JTextField yearField = new JTextField();

        addDialog.add(new JLabel("Title:"));
        addDialog.add(titleField);
        addDialog.add(new JLabel("Author:"));
        addDialog.add(authorField);
        addDialog.add(new JLabel("ISBN:"));
        addDialog.add(isbnField);
        addDialog.add(new JLabel("Year Published:"));
        addDialog.add(yearField);

        // Add a save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // Create a new book and save it
            Book book = new Book(
                titleField.getText(),
                authorField.getText(),
                isbnField.getText(),
                Integer.parseInt(yearField.getText())
            );
            bookServiceClient.addBook(book);
            addDialog.dispose();
            refreshTable();
        });

        addDialog.add(saveButton);
        addDialog.setVisible(true);
    }

    private void openEditBookDialog(JTable bookTable) {
        // Get the selected book
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to edit.");
            return;
        }

        // Get the book ID from the table
        UUID bookId = (UUID) bookTable.getValueAt(selectedRow, 0);

        // Fetch the book details from the service client
        Book book = bookServiceClient.getBookById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        // Create a dialog for editing the book
        JDialog editDialog = new JDialog(this, "Edit Book", true);
        editDialog.setSize(400, 300);
        editDialog.setLayout(new GridLayout(5, 2));

        // Add input fields with current book data
        JTextField titleField = new JTextField(book.getTitle());
        JTextField authorField = new JTextField(book.getAuthor());
        JTextField isbnField = new JTextField(book.getIsbn());
        JTextField yearField = new JTextField(String.valueOf(book.getYearPublished()));

        editDialog.add(new JLabel("Title:"));
        editDialog.add(titleField);
        editDialog.add(new JLabel("Author:"));
        editDialog.add(authorField);
        editDialog.add(new JLabel("ISBN:"));
        editDialog.add(isbnField);
        editDialog.add(new JLabel("Year Published:"));
        editDialog.add(yearField);

        // Add a save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // Update the book and save it
            book.setTitle(titleField.getText());
            book.setAuthor(authorField.getText());
            book.setIsbn(isbnField.getText());
            book.setYearPublished(Integer.parseInt(yearField.getText()));
            bookServiceClient.updateBook(bookId, book);
            editDialog.dispose();
            refreshTable();
        });

        editDialog.add(saveButton);
        editDialog.setVisible(true);
    }

    private void deleteBook(JTable bookTable) {
        // Get the selected book
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.");
            return;
        }

        // Get the book ID from the table
        UUID bookId = (UUID) bookTable.getValueAt(selectedRow, 0);

        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this book?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            bookServiceClient.deleteBook(bookId);
            refreshTable();
        }
    }

    private void refreshTable() {
        // Refresh the table with updated data
        bookTable.setModel(createBookTable().getModel());
        revalidate();
        repaint();
    }
}