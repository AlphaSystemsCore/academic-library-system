package com.sys.service;

import java.time.LocalDate;
import java.util.List;
import com.sys.dao.BookDao;
import com.sys.dao.ReservationDao;
import com.sys.dao.StudentDao;
import com.sys.model.Book;
import com.sys.model.Reservation;
import com.sys.model.Reservation.ReservationStatus;

public class ReservationService {

    private final ReservationDao reservationDao = new ReservationDao();
    private final BookDao bookDao = new BookDao();
    private final StudentDao studentDao = new StudentDao();


    //  RESERVE A BOOK

    public void reserveBook(int studentId, int bookId) {

        if (studentId < 1)
            throw new IllegalArgumentException("Student ID must be a positive number.");

        if (bookId < 1)
            throw new IllegalArgumentException("Book ID must be a positive number.");

        if (!studentDao.idExistsStudent(studentId))
            throw new IllegalArgumentException("Student not found.");

        if (!bookDao.idExistsBook(bookId))
            throw new IllegalArgumentException("Book not found.");

        // Can only reserve if no copies are available
        Book book = bookDao.findBookById(bookId);
        if (book.getAvailableCopies() > 0)
            throw new IllegalStateException("Copies are available. Please borrow the book directly instead of reserving.");

        // Prevent duplicate reservation
        if (reservationDao.reservationExists(studentId, bookId))
            throw new IllegalStateException("Student already has a pending reservation for this book.");

        Reservation reservation = new Reservation(bookId, studentId, bookId, null, ReservationStatus.PENDING);

        boolean saved = reservationDao.saveReservation(reservation);
        if (!saved)
            throw new RuntimeException("Failed to save reservation. Please try again.");
    }


    //  CANCEL RESERVATION
  
    public void cancelReservation(int reservationId) {
        if (reservationId < 1)
            throw new IllegalArgumentException("Reservation ID must be a positive number.");

        if (!reservationDao.idExistsReservation(reservationId))
            throw new IllegalArgumentException("Reservation not found.");

        Reservation reservation = reservationDao.findReservationById(reservationId);

        if (reservation.getStatus() == ReservationStatus.CANCELLED)
            throw new IllegalStateException("Reservation is already cancelled.");

        reservation.setStatus(ReservationStatus.CANCELLED);
        boolean updated = reservationDao.updateReservationStatus(reservation);
        if (!updated)
            throw new RuntimeException("Failed to cancel reservation. Please try again.");
    }


    //  GET ALL RESERVATIONS
   
    public List<Reservation> getAllReservations() {
        List<Reservation> list = reservationDao.findAllReservations();
        if (list.isEmpty())
            throw new IllegalStateException("No reservations found.");
        return list;
    }

  
    //  GET BY STUDENT 

    public List<Reservation> getReservationsByStudent(int studentId) {
        if (studentId < 1)
            throw new IllegalArgumentException("Student ID must be a positive number.");

        if (!studentDao.idExistsStudent(studentId))
            throw new IllegalArgumentException("Student not found.");

        List<Reservation> list = reservationDao.findReservationsByStudent(studentId);
        if (list.isEmpty())
            throw new IllegalStateException("No reservations found for this student.");
        return list;
    }

   
    //  GET PENDING RESERVATIONS FOR A BOOK (queue)
  
    public List<Reservation> getPendingReservationsByBook(int bookId) {
        if (bookId < 1)
            throw new IllegalArgumentException("Book ID must be a positive number.");

        if (!bookDao.idExistsBook(bookId))
            throw new IllegalArgumentException("Book not found.");

        List<Reservation> list = reservationDao.findPendingReservationsByBook(bookId);
        if (list.isEmpty())
            throw new IllegalStateException("No pending reservations for this book.");
        return list;
    }


    //  GET BY ID
    
    public Reservation getReservationById(int reservationId) {
        if (reservationId < 1)
            throw new IllegalArgumentException("Reservation ID must be a positive number.");

        Reservation reservation = reservationDao.findReservationById(reservationId);
        if (reservation == null)
            throw new IllegalStateException("Reservation not found for ID: " + reservationId);
        return reservation;
    }

    
    //  DELETE RESERVATION
   
    public void deleteReservation(int reservationId) {
        if (reservationId < 1)
            throw new IllegalArgumentException("Reservation ID must be a positive number.");

        if (!reservationDao.idExistsReservation(reservationId))
            throw new IllegalArgumentException("Reservation not found.");

        boolean deleted = reservationDao.deleteReservation(reservationId);
        if (!deleted)
            throw new RuntimeException("Failed to delete reservation. Please try again.");
    }
}
