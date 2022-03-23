package edu.wctc.data;

import java.util.ArrayList;
import java.util.List;

public class Borrower {
    private String firstName;
    private String lastName;
    private List<Book> booksOnLoan = new ArrayList<>();

    public void checkoutBook(Book book) {
        booksOnLoan.add(book);
    }

    public Book[] getBooksOnLoan() {
        return booksOnLoan.toArray(new Book[0]);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void returnBook(Book book) {
        booksOnLoan.remove(book);
    }

    public void setBooksOnLoan(List<Book> booksOnLoan) {
        this.booksOnLoan = booksOnLoan;
    }

    @Override
    public String toString() {
        return lastName + ", " + firstName;
    }
}
