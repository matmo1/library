package com.books;

import com.books.gui.BookManagementGUI;
import com.books.services.BookServiceClient;

import javax.swing.*;

public class BooksFrontApplication {
    public static void main(String[] args) {
        // Initialize the service client
        BookServiceClient bookServiceClient = new BookServiceClient();

        // Run the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            BookManagementGUI gui = new BookManagementGUI(bookServiceClient);
            gui.setVisible(true);
        });
    }
}
