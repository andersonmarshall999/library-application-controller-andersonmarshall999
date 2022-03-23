package edu.wctc.tinydemo.refactored;

import javax.swing.*;

public class Controller {
    // The controller has a reference to the window
    private GreetingWindow greetingWindow;

    public Controller() {
        // It creates the window and passes it a reference to
        // itself (the controller)
        greetingWindow = new GreetingWindow(this);

        greetingWindow.setVisible(true);
    }

    // This method contains business logic (well, not much, but still...)
    // so it belongs in the controller, not the window
    public void sayHello() {
        // If the controller needs data from the window, it asks
        String firstName = greetingWindow.getFirstName().toUpperCase();

        String greeting = "Hello, " + firstName + "!";

        // This creates a modal window that blocks the greeting
        // window while it is visible
        JOptionPane.showMessageDialog(greetingWindow, greeting);

        greetingWindow.clearFirstName();
    }
}
