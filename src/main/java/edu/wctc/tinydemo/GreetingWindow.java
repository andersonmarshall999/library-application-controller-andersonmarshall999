package edu.wctc.tinydemo;

import javax.swing.*;
import java.awt.*;

public class GreetingWindow extends JFrame {
    private JTextArea txtFirstName = new JTextArea();
    private JButton btnShowGreeting = new JButton("Show Greeting");

    public GreetingWindow() {
        super("Greeting App");

        // Setup GUI
        init();

        // Size components to fit window
        pack();
        // End program when X button clicked
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Return the text in the input box
    public String getFirstName() {
        return txtFirstName.getText();
    }

    // Set a blank string in the input box to clear it
    public void clearFirstName() {
        txtFirstName.setText("");
    }

    private void init() {
        btnShowGreeting.addActionListener(e -> sayHello());

        JPanel pnlMain = new JPanel(new BorderLayout(5, 15));
        pnlMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlMain.add(new JLabel("What's your name? "), BorderLayout.WEST);
        pnlMain.add(txtFirstName, BorderLayout.CENTER);
        pnlMain.add(btnShowGreeting, BorderLayout.SOUTH);

        getContentPane().add(pnlMain);
    }

    public void sayHello() {
        String firstName = getFirstName().toUpperCase();

        String greeting = "Hello, " + firstName + "!";

        // This creates a modal window that blocks the greeting
        // window while it is visible
        JOptionPane.showMessageDialog(this, greeting);

        clearFirstName();
    }

}
