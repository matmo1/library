package com.booklibrary.frontend;

import javax.swing.*;

import com.booklibrary.frontend.gui.BookManagementGUI;
import com.booklibrary.frontend.services.BookServiceClient;
import com.booklibrary.backend.BackendMain;

public class FrontendMain {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("java.awt.headless", "false");
        // Global exception handler for uncaught exceptions
    Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
        System.err.println("Uncaught exception in thread: " + thread.getName());
        throwable.printStackTrace();
    });

    new Thread(() -> BackendMain.main(args)).start();
    
    Thread.sleep(10000);

    // Initialize the service client
    BookServiceClient bookServiceClient = new BookServiceClient();

    // Run the GUI on the Event Dispatch Thread
    SwingUtilities.invokeLater(() -> {
        BookManagementGUI gui = new BookManagementGUI(bookServiceClient);
        gui.setVisible(true);
    });
    }
}