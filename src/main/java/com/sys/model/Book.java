package com.sys.model;

/**
 * Represents a book in the library catalog.
 * availableCopies decreases when borrowed, increases when returned.
 *
 * DB: books(book_id, isbn, title, author, publisher,
 *           published_year, category, total_copies, available_copies)
 */
public class Book {
    private int bookId;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private int publishedYear;
    private String category;
    private int totalCopies;
    private int availableCopies;

    public Book(int bookId, String isbn, String title, String author,
                String publisher, int publishedYear, String category,
                int totalCopies, int availableCopies) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishedYear = publishedYear;
        this.category = category;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    public Book(String isbn, String title, String author, String publisher,
                int publishedYear, String category, int totalCopies) {
        this(0, isbn, title, author, publisher, publishedYear, category, totalCopies, totalCopies);
    }

    public boolean isAvailable() { return availableCopies > 0; }

    public void borrowCopy() {
        if (availableCopies <= 0)
            throw new IllegalStateException("No copies available to borrow");
        availableCopies--;
    }

    public void returnCopy() {
        if (availableCopies >= totalCopies)
            throw new IllegalStateException("All copies already returned");
        availableCopies++;
    }

    public int getBookId(){ 
        return bookId; 
    }
    public String getIsbn(){ 
        return isbn; 
    }
    public String getTitle(){ 
        return title; 
    }
    public String getAuthor(){ 
        return author;
    }
    public String getPublisher(){
         return publisher; 
    }
    public int getPublishedYear(){ 
        return publishedYear; 
    }
    public String getCategory(){ 
        return category; 
    }
    public int getTotalCopies(){ 
        return totalCopies; 
    }
    public int getAvailableCopies(){ 
        return availableCopies; 
    }

    public void setBookId(int bookId){
        this.bookId = bookId; 
    }
    public void setAvailableCopies(int available){ 
        this.availableCopies = available; 
    }
    public void setTotalCopies(int total) { 
        this.totalCopies = total;
    }

}
