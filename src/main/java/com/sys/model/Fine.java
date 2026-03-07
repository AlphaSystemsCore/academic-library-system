package com.sys.model;

import java.time.LocalDate;


public class Fine {
    private int fineId;
    private int borrowingId;
    private int studentId;
    private float amount;
    private boolean isPaid;
    private LocalDate createdDate;
    private LocalDate paidDate;

    // //
    // private String studentName;
    // private String bookTitle;

    public Fine(int fineId, int borrowingId, int studentId,
                float amount, LocalDate createdDate) {
        this.fineId = fineId;
        this.borrowingId = borrowingId;
        this.studentId = studentId;
        this.amount = amount;
        this.isPaid = false;
        this.createdDate = createdDate;
        this.paidDate = null;
    }
    public Fine(int borrowingId, int studentId, float amount) {
        this(0, borrowingId, studentId, amount, LocalDate.now());
    }

    public void markAsPaid() {
        this.isPaid = true;
        this.paidDate = LocalDate.now();
    }

    public int getFineId(){ 
        return fineId; 
    }
    public int getBorrowingId(){ 
        return borrowingId; 
    }
    public int getStudentId(){ 
        return studentId; 
    }
    public float getAmount(){
         return amount; 
        }
    public boolean isPaid(){ 
        return isPaid; 
    }
    public LocalDate getCreatedDate(){ 
        return createdDate; 
    }
    public LocalDate getPaidDate()  { 
        return paidDate; 
    }

    public void setFineId(int fineId){ 
        this.fineId = fineId; 
    }
    public void setAmount(float amount){ 
        this.amount = amount; 
    }
    // public String getStudentName(){ 
    //     return studentName; 
    // }
    // public String getBookTitle(){
    //      return bookTitle; 
    // }
    // public void setStudentName(String name){ 
    //     this.studentName = name; 
    // }
    // public void setBookTitle(String title){ 
    //     this.bookTitle = title; 
    // }

    
}
