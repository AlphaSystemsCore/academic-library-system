package com.sys.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

//  * Created when a student borrows a book.
//  * Updated when the book is returned.

public class Borrowing {
    private int borrowingId;
    private int studentId;
    private int bookId;
    private int librarianId;
    private LocalDate borrowDate;
    private LocalDate dueDate;          // borrowDate + 14 days
    private LocalDate returnDate;       // null until returned
    private StatusEnum status;

    // populated by JOIN queries for display
    private String studentName;
    private String admissionNumber;
    private String bookTitle;
    private String bookIsbn;

    public static final int BORROW_PERIOD_DAYS = 14;
    public static final float FINE_PER_DAY = 5.00f; // ksh 5 per day

    public enum StatusEnum {
        BORROWED, RETURNED, OVERDUE
    }

   
    public Borrowing(int studentId, int bookId,
                     int librarianId, LocalDate borrowDate) {
        this.borrowingId = 0;
        this.studentId = studentId;
        this.bookId = bookId;
        this.librarianId = librarianId;
        this.borrowDate = borrowDate;
        this.dueDate = borrowDate.plusDays(BORROW_PERIOD_DAYS);
        this.returnDate = null;
        this.status = StatusEnum.BORROWED;
    }

    
    public Borrowing(int borrowingId, int studentId, int bookId,
                     int librarianId, LocalDate borrowDate) {
        this.borrowingId = borrowingId;
        this.studentId = studentId;
        this.bookId = bookId;
        this.librarianId = librarianId;
        this.borrowDate = borrowDate;
        this.dueDate = borrowDate.plusDays(BORROW_PERIOD_DAYS);
        this.returnDate = null;
        this.status = StatusEnum.BORROWED;
    }

    public void returnBook() {
        this.returnDate = LocalDate.now();
        this.status = StatusEnum.RETURNED;
    }

    public boolean isOverdue() {
        if (status == StatusEnum.RETURNED) return false;
        return LocalDate.now().isAfter(dueDate);
    }

    public float calculateFine() {
        LocalDate effectiveReturn = returnDate != null ? returnDate : LocalDate.now();
        long daysLate = ChronoUnit.DAYS.between(dueDate, effectiveReturn);
        return daysLate > 0 ? daysLate * FINE_PER_DAY : 0f;
    }

    public long getDaysRemaining() {
        if (status == StatusEnum.RETURNED) return 0;
        return ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
    }

    public int getBorrowingId(){
         return borrowingId; 
    }

    public int getStudentId(){ 
        return studentId; 
    }

    public int getBookId(){ 
        return bookId; 
    }

    public int getLibrarianId(){ 
        return librarianId; 
    }

    public LocalDate getBorrowDate(){ 
        return borrowDate; 
    }

    public LocalDate getDueDate(){ 
        return dueDate; 
    }

    public LocalDate getReturnDate(){ 
        return returnDate; 
    }

    public StatusEnum getStatus(){ 
        return status; 
    }

    public void setBorrowingId(int borrowingId){ 
        this.borrowingId = borrowingId; 
    }

    public void setStatus(StatusEnum status){ 
        this.status = status; 
    }

    public void setReturnDate(LocalDate date){ 
        this.returnDate = date; 
    }

    // Display fields
    public String getStudentName(){ 
        return studentName;

    }

    public String getAdmissionNumber(){
         return admissionNumber;         
    }

    public String getBookTitle(){ 
        return bookTitle; 
    }

    public String getBookIsbn(){ 
        return bookIsbn; 
    }

    public void setStudentName(String name){ 
        this.studentName = name; 
    }

    public void setAdmissionNumber(String number){
         this.admissionNumber = number;
     }

    public void setBookTitle(String title){ 
        this.bookTitle = title; 
    }

    public void setBookIsbn(String isbn){ 
        this.bookIsbn = isbn; 
    }
}
