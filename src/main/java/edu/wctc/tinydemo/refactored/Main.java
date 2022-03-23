package edu.wctc.tinydemo.refactored;

public class Main {
    public static void main(String[] args) {
        // Main creates the controller instead of the window.
        // The controller will create and manage the window.
        Controller controller = new Controller();

    }
}
