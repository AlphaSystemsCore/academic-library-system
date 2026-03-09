package com.sys.model;

import java.time.LocalDate;

public class Reservation {
    public enum ReservationStatus {
        PENDING,    
        FULFILLED,  // a copy is  available
        CANCELLED   // cancelled
    }

    
    private int reservationId;
    private int studentId;
    private int bookId;
    private LocalDate reservedDate;
    private ReservationStatus status;

    public Reservation(int studentId, int bookId) {
        this.studentId    = studentId;
        this.bookId       = bookId;
        this.reservedDate = LocalDate.now();
        this.status       = ReservationStatus.PENDING;
    }

 
    public Reservation(int reservationId, int studentId, int bookId,
                       LocalDate reservedDate, ReservationStatus status) {
        this.reservationId = reservationId;
        this.studentId     = studentId;
        this.bookId        = bookId;
        this.reservedDate  = reservedDate;
        this.status        = status;
    }

    public void fulfil() {
        this.status = ReservationStatus.FULFILLED;
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }

    public int getReservationId(){ 
        return reservationId; 
    }
    
    public void setReservationId(int id){ 
        this.reservationId = id; 
    }

    public int getStudentId(){ 
        return studentId; 
    }

    public int getBookId(){ 
        return bookId; 
    }

    public LocalDate getReservedDate(){ 
        return reservedDate; 
    }

    public ReservationStatus getStatus(){ 
        return status; 
    }

    public void setStatus(ReservationStatus s){ 
        this.status = s; 
    }
}