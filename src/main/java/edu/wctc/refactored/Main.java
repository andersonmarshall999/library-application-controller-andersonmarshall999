package edu.wctc.refactored;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

/*
The refactored package contains copies of the original Main and LibraryWindow.
You should not need to change anything in the data package.
 */
public class Main {

    public static void main(String[] args) {
        // Sets a nicer default font, is all
        setUIFont(new FontUIResource("Sans", Font.PLAIN, 18));

        // Create the window and show it
        LibraryWindow window = new LibraryWindow();
        window.setVisible(true);
    }

    public static void setUIFont(FontUIResource f) {
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource)
                UIManager.put(key, f);
        }
    }
}
