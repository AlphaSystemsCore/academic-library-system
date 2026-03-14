package com.sys.service;

import java.util.List;
import com.sys.dao.BookDao;
import com.sys.model.Book;

public class BookService {

    private final BookDao bookDao = new BookDao();
    //  ADD BOOK
    
    public void addBook(String isbn, String title, String author, String publisher,
                        int publishedYear, String category, int totalCopies) {  //frontend parameters

        if (isbn == null || isbn.isBlank())
            throw new IllegalArgumentException("ISBN cannot be blank.");

        if (title == null || title.isBlank())
            throw new IllegalArgumentException("Book title cannot be blank.");

        if (author == null || author.isBlank())
            throw new IllegalArgumentException("Author cannot be blank.");

        if (publisher == null || publisher.isBlank())
            throw new IllegalArgumentException("Publisher cannot be blank.");

        if (publishedYear < 1000 || publishedYear > java.time.LocalDate.now().getYear())
            throw new IllegalArgumentException("Invalid published year.");

        if (category == null || category.isBlank())
            throw new IllegalArgumentException("Category cannot be blank.");

        if (totalCopies < 1)
            throw new IllegalArgumentException("Total copies must be at least 1.");

        if (bookDao.isbnExistsBook(isbn))
            throw new IllegalArgumentException("A book with this ISBN already exists.");

        // When adding a new book, available copies = total copies
        Book book = new Book(isbn, title, author, publisher, publishedYear, category, totalCopies);

        boolean saved = bookDao.saveBook(book);
        if (!saved)
            throw new RuntimeException("Failed to save book. Please try again.");
    }


    //  GET ALL BOOKS
   
    public List<Book> getAllBooks() {
        List<Book> books = bookDao.findAllBooks();
        if (books.isEmpty())
            throw new IllegalStateException("No books found.");
        return books;
    }
    
    //  GET AVAILABLE BOOKS
    // atleast  ONE COPY

    public List<Book> getAvailableBooks() {
        List<Book> books = bookDao.findAvailableBooks();
        if (books.isEmpty())
            throw new IllegalStateException("No available books at the moment.");
        return books;
    }

   
    //  SEARCH BOOKS BY TITLE OR AUTHOR
   
    public List<Book> searchBooks(String keyword) {  //frontend parameters
        if (keyword == null || keyword.isBlank())
            throw new IllegalArgumentException("Search keyword cannot be blank.");

        List<Book> books = bookDao.searchBooks(keyword);
        if (books.isEmpty())
            throw new IllegalStateException("No books found matching: " + keyword);
        return books;
    }

    //  GET BY ID
  
    public Book getBookById(int bookId) {   //frontend parameters
        if (bookId < 1)
            throw new IllegalArgumentException("Book ID must be a positive number.");

        Book book = bookDao.findBookById(bookId);
        if (book == null)
            throw new IllegalStateException("Book not found for ID: " + bookId);
        return book;
    }

    //  GET BY ISBN
  
    public Book getBookByIsbn(String isbn) {  //frontend parameters
        if (isbn == null || isbn.isBlank())
            throw new IllegalArgumentException("ISBN cannot be blank.");

        Book book = bookDao.findBookByIsbn(isbn);
        if (book == null)
            throw new IllegalStateException("No book found with ISBN: " + isbn);
        return book;
    }


    //  UPDATE BOOK
  
    public void updateBook(int bookId, String isbn, String title, String author,
                           String publisher, int publishedYear, String category, int totalCopies) {  //frontend parameters

        if (bookId < 1)
            throw new IllegalArgumentException("Book ID must be a positive number.");

        if (!bookDao.idExistsBook(bookId))
            throw new IllegalArgumentException("Book not found.");

        if (isbn == null || isbn.isBlank())
            throw new IllegalArgumentException("ISBN cannot be blank.");

        if (title == null || title.isBlank())
            throw new IllegalArgumentException("Book title cannot be blank.");

        if (author == null || author.isBlank())
            throw new IllegalArgumentException("Author cannot be blank.");

        if (publisher == null || publisher.isBlank())
            throw new IllegalArgumentException("Publisher cannot be blank.");

        if (publishedYear < 1000 || publishedYear > java.time.LocalDate.now().getYear())
            throw new IllegalArgumentException("Invalid published year.");

        if (category == null || category.isBlank())
            throw new IllegalArgumentException("Category cannot be blank.");

        if (totalCopies < 1)
            throw new IllegalArgumentException("Total copies must be at least 1.");

        // Allow same ISBN only if it belongs to THIS book
        Book existingByIsbn = bookDao.findBookByIsbn(isbn);  
        if (existingByIsbn != null && existingByIsbn.getBookId() != bookId)
            throw new IllegalArgumentException("ISBN is already used by another book.");

        boolean updated = bookDao.updateBook(bookId, isbn, title, author, publisher, publishedYear, category, totalCopies);
        if (!updated)
            throw new RuntimeException("Failed to update book. Please try again.");
    }

  
    //  DELETE BOOK
   
    public void deleteBook(int bookId) {  //frontend parameters
        if (bookId < 1)
            throw new IllegalArgumentException("Book ID must be a positive number.");

        if (!bookDao.idExistsBook(bookId))
            throw new IllegalArgumentException("Book not found.");

        boolean deleted = bookDao.deleteBook(bookId);
        if (!deleted)
            throw new RuntimeException("Failed to delete book. Please try again.");
    }
}
