package com.sys.model;

import java.time.LocalDate;


public class Fine {
    private int borrowingId;
    private int studentId;
    private float amount;
    private boolean isPaid;
    private LocalDate createdDate;
    private LocalDate paidDate;

    // //
    // private String studentName;
    // private String bookTitle;

    public Fine(int borrowingId, int studentId,
                float amount) {
        this.borrowingId = borrowingId;
        this.studentId = studentId;
        this.amount = amount;
        this.isPaid = false;
        this.createdDate = createdDate.now();
        this.paidDate = null;
    }
  
    public void markAsPaid() {
        this.isPaid = true;
        this.paidDate = LocalDate.now();
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
