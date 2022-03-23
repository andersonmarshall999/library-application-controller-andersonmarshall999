package edu.wctc.tinydemo.refactored;

import javax.swing.*;
import java.awt.*;

public class GreetingWindow extends JFrame {
    private final JTextArea txtFirstName = new JTextArea();
    private final JButton btnShowGreeting = new JButton("Show Greeting");

    // The window receives the controller as a constructor argument.
    // The controller is notified when the button is clicked. (See init())
    private final Controller controller;

    // The only methods that remain in the window are those that
    // reference the GUI widgets (text boxes, buttons, etc.)
    public GreetingWindow(Controller controller) {
        super("Greeting App");

        this.controller = controller;

        // Setup GUI
        init();

        // Size components to fit window
        pack();
        // End program when X button clicked
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    // Set a blank string in the input box to clear it
    public void clearFirstName() {
        txtFirstName.setText("");
    }

    // Return the text in the input box
    public String getFirstName() {
        return txtFirstName.getText();
    }

    private void init() {
        // Now the controller contains the method with the logic, so
        // the window tells the controller to execute sayHello()
        btnShowGreeting.addActionListener(e -> controller.sayHello());

        JPanel pnlMain = new JPanel(new BorderLayout(5, 15));
        pnlMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlMain.add(new JLabel("What's your name? "), BorderLayout.WEST);
        pnlMain.add(txtFirstName, BorderLayout.CENTER);
        pnlMain.add(btnShowGreeting, BorderLayout.SOUTH);

        getContentPane().add(pnlMain);
    }


}
