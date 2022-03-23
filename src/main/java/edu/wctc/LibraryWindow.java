package edu.wctc;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.wctc.data.Book;
import edu.wctc.data.Borrower;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LibraryWindow extends JFrame {

    // Array lists of data to populate the JLists
    private List<Borrower> borrowerList;
    private List<Book> availableBookList;

    // GUI widgets -- you don't need to change these
    private JList<Borrower> lstBorrowers = new JList<>();
    private JList<Book> lstBooksOnLoan = new JList<>();
    private JList<Book> lstBooksAvailable = new JList<>();
    private JButton btnCheckoutBook = new JButton("Checkout Selected Book");
    private JButton btnReturnBook = new JButton("Return Selected Book");
    private JTextField txtBorrowerSearch = new JTextField();
    private JButton btnBorrowerSearch = new JButton("Search Borrowers");
    private JTextField txtBookSearch = new JTextField();
    private JButton btnBookSearch = new JButton("Search Books");
    private JButton btnSaveAndExit = new JButton("Save and Exit");

    public LibraryWindow() {
        super("Library Lending System");

        // Read JSON files into the data lists
        readDataFiles();
        // Put the lists into the window components for display
        displayAvailableBooks(availableBookList);
        displayBorrowers(borrowerList);

        // Set up the GUI and layout components
        init();
        // Set window size and fit components
        setPreferredSize(new Dimension(800, 600));
        pack();
        // When the GUI is closed, the program ends
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void checkoutBook() {
        // Get selections from the JLists (window widgets)
        Borrower borrower = getSelectedBorrower();
        Book book = getSelectedAvailableBook();

        // Ensure one of each is selected
        if (book == null || borrower == null) {
            JOptionPane.showMessageDialog(this, "You must select a borrower and a book to checkout.");
        } else {
            // Remove book from data list and add to selected borrower
            availableBookList.remove(book);
            borrower.checkoutBook(book);

            // Update the screen to show the book is checked out
            refreshBookDisplay(availableBookList);
        }
    }

    // Get search term from the text box
    public String getBookSearchTerm() {
        return txtBookSearch.getText();
    }

    // Get search term from the text box
    public String getBorrowerSearchTerm() {
        return txtBorrowerSearch.getText();
    }

    // Get selection from the JList
    public Book getSelectedAvailableBook() {
        return lstBooksAvailable.getSelectedValue();
    }

    // Get selection from the JList
    public Book getSelectedBookOnLoan() {
        return lstBooksOnLoan.getSelectedValue();
    }

    // Get selection from the JList
    public Borrower getSelectedBorrower() {
        return lstBorrowers.getSelectedValue();
    }

    private void init() {
        // Add actions to the buttons that will call a method when clicked
        btnCheckoutBook.addActionListener(e -> checkoutBook());
        btnReturnBook.addActionListener(e -> returnBook());

        btnBookSearch.addActionListener(e -> searchBooks());
        btnBorrowerSearch.addActionListener(e -> searchBorrowers());

        btnSaveAndExit.addActionListener(e -> saveAndExit());

        // When user selects borrower from the list, shows their borrowed books
        lstBorrowers.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                displayBooksOnLoan();
            }
        });

        // Restrict JLists to a single selected item
        lstBorrowers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstBooksAvailable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstBooksOnLoan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // The rest of this method lays out the GUI components
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        JPanel pnlLeft = new JPanel(new GridBagLayout());
        pnlLeft.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        pnlLeft.add(txtBorrowerSearch, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
        pnlLeft.add(btnBorrowerSearch, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        JLabel lbl = new JLabel("Borrowers", SwingConstants.LEFT);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
        pnlLeft.add(lbl, new GridBagConstraints(0, 1, 2, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 0), 0, 0));
        pnlLeft.add(new JScrollPane(lstBorrowers), new GridBagConstraints(0, 2, 2, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 0, 0, 0), 0, 0));
        splitPane.add(pnlLeft, JSplitPane.LEFT);

        JPanel pnlRight = new JPanel(new GridBagLayout());
        pnlRight.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        pnlRight.add(txtBookSearch, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
        pnlRight.add(btnBookSearch, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        lbl = new JLabel("Books in the Library", SwingConstants.LEFT);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
        pnlRight.add(lbl, new GridBagConstraints(0, 1, 2, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 0), 0, 0));
        pnlRight.add(new JScrollPane(lstBooksAvailable), new GridBagConstraints(0, 2, 2, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 0, 0, 0), 0, 0));
        pnlRight.add(btnCheckoutBook, new GridBagConstraints(0, 3, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 0), 0, 0));
        lbl = new JLabel("Books on Loan", SwingConstants.LEFT);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
        pnlRight.add(lbl, new GridBagConstraints(0, 4, 2, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 0), 0, 0));
        pnlRight.add(new JScrollPane(lstBooksOnLoan), new GridBagConstraints(0, 5, 2, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 0, 0, 0), 0, 0));
        pnlRight.add(btnReturnBook, new GridBagConstraints(0, 6, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 0), 0, 0));
        splitPane.add(pnlRight, JSplitPane.RIGHT);

        splitPane.setDividerLocation(350);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBottom.add(btnSaveAndExit);

        JPanel pnlMain = new JPanel(new BorderLayout());
        pnlMain.add(splitPane, BorderLayout.CENTER);
        pnlMain.add(pnlBottom, BorderLayout.SOUTH);

        getContentPane().add(pnlMain);
    }

    // Display books on loan to the selected borrower
    public void displayBooksOnLoan() {
        Borrower borrower = lstBorrowers.getSelectedValue();
        if (borrower != null) {
            lstBooksOnLoan.setListData(borrower.getBooksOnLoan());
        } else {
            lstBooksOnLoan.setListData(new Book[0]);
        }
    }

    // Read the JSON files into the data lists and display them
    public void readDataFiles() {
        // Create empty lists
        availableBookList = new ArrayList<>();
        borrowerList = new ArrayList<>();

        // Create a JSON object mapper
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Create Java objects from the JSON data and stream into the lists
            Stream.of(mapper.readValue(new File("books.json"), Book[].class)).forEach(availableBookList::add);
            Stream.of(mapper.readValue(new File("borrowers.json"), Borrower[].class)).forEach(borrowerList::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void returnBook() {
        // Get selections from the JLists (window widgets)
        Borrower borrower = getSelectedBorrower();
        Book book = getSelectedBookOnLoan();

        // Ensure one of each is selected
        if (book == null || borrower == null) {
            JOptionPane.showMessageDialog(this, "You must select a borrower and a book to return.");
        } else {
            // Add the book to the data list and remove from the borrower
            availableBookList.add(book);
            borrower.returnBook(book);

            // Update the screen to show the book is checked out
            refreshBookDisplay(availableBookList);
        }
    }

    public void refreshBookDisplay(List<Book> availableBookList) {
        // Update both book lists (available and on loan) in the window
        displayAvailableBooks(availableBookList);
        displayBooksOnLoan();
    }

    public void saveAndExit() {
        // Write the borrower and book lists to JSON files
        writeDataFiles();

        // Close this window, which will exit the program
        this.dispose();
    }

    public void searchBooks() {
        // Get search string from the text box
        String searchTerm = getBookSearchTerm();

        // Filter available books to those matching the search string
        List<Book> matches = availableBookList.stream()
                .filter(b ->
                        b.getAuthor().toString().toLowerCase().contains(searchTerm.toLowerCase()) ||
                                b.getTitle().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());

        // Display matches
        displayAvailableBooks(matches);
    }

    public void searchBorrowers() {
        // Get search string from the text box
        String searchTerm = getBorrowerSearchTerm();

        // Filter borrowers to those matching the search string
        List<Borrower> matches = borrowerList.stream()
                .filter(b -> b.toString().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());

        // Display matches
        displayBorrowers(matches);
    }

    public void displayAvailableBooks(List<Book> list) {
        // Set the data in the JList (window widget)
        lstBooksAvailable.setListData(list.toArray(new Book[0]));
    }

    public void displayBorrowers(List<Borrower> list) {
        // Set the data in the JList (window widget)
        lstBorrowers.setListData(list.toArray(new Borrower[0]));
    }

    public void writeDataFiles() {
        // Create a JSON mapper
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Write the data lists as JSON to the files
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("books.json"), availableBookList);
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("borrowers.json"), borrowerList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
