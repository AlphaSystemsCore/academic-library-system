package com.sys.service;

import java.time.LocalDate;
import java.util.List;
import com.sys.dao.BookDao;
import com.sys.dao.BorrowingDao;
import com.sys.dao.FineDao;
import com.sys.dao.LibrarianDao;
import com.sys.dao.ReservationDao;
import com.sys.dao.StudentDao;
import com.sys.model.Book;
import com.sys.model.Borrowing;
import com.sys.model.Borrowing.StatusEnum;
import com.sys.model.Fine;
import com.sys.model.Reservation;
import com.sys.model.Reservation.ReservationStatus;

public class BorrowingService {

    private final BorrowingDao borrowingDao = new BorrowingDao();
    private final BookDao bookDao = new BookDao();
    private final StudentDao studentDao = new StudentDao();
    private final LibrarianDao librarianDao = new LibrarianDao();
    private final FineDao fineDao = new FineDao();
    private final ReservationDao reservationDao = new ReservationDao();

    // Fine rate per overdue day (KES or local currency)
    private static final float FINE_PER_DAY = 10.0f;
    // Default borrowing period in days
    private static final int BORROW_DAYS = 14;


    public void borrowBook(int studentId, int bookId, int librarianId) {  //frontend parameters

        if (studentId < 1)
            throw new IllegalArgumentException("Student ID must be a positive number.");

        if (bookId < 1)
            throw new IllegalArgumentException("Book ID must be a positive number.");

        if (librarianId < 1)
            throw new IllegalArgumentException("Librarian ID must be a positive number.");

        if (!studentDao.idExistsStudent(studentId))
            throw new IllegalArgumentException("Student not found.");

        if (!bookDao.idExistsBook(bookId))
            throw new IllegalArgumentException("Book not found.");

        if (!librarianDao.idExistsLibrarian(librarianId))
            throw new IllegalArgumentException("Librarian not found.");

        // Check book availability
        Book book = bookDao.findBookById(bookId);
        if (book.getAvailableCopies() < 1)
            throw new IllegalStateException("No available copies for this book. The student may reserve it.");

        // Check if student already has an active borrowing for this book
        List<Borrowing> activeBorrowings = borrowingDao.findBorrowingsByStudent(studentId);
        for (Borrowing b : activeBorrowings) {
            if (b.getBookId() == bookId &&
               (b.getStatus() == StatusEnum.BORROWED || b.getStatus() == StatusEnum.OVERDUE))
                throw new IllegalStateException("Student already has an active borrowing for this book.");
        }

        //  (block borrowing if so) unpaid fines no new borrow
        List<Fine> unpaidFines = fineDao.findUnpaidFinesByStudent(studentId);
        if (!unpaidFines.isEmpty())
            throw new IllegalStateException("Student has unpaid fines. Please clear fines before borrowing.");

        // Mark overdue records first (run on every transaction)
        borrowingDao.markOverdueBorrowings();

        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(BORROW_DAYS);

        Borrowing borrowing = new Borrowing(studentId, bookId, librarianId, borrowDate);
        borrowing.setStatus(StatusEnum.BORROWED);

        boolean saved = borrowingDao.saveBorrowing(borrowing);
        if (!saved)
            throw new RuntimeException("Failed to record borrowing. Please try again.");

        // Decrease available copies
        boolean updated = bookDao.updateAvailableCopies(bookId, book.getAvailableCopies() - 1);
        if (!updated)
            throw new RuntimeException("Failed to update book availability. Please try again.");
    }

    
    //  RETURN BOOK
public void returnBook(int borrowingId, int librarianId) {   //frontend parameters

        if (borrowingId < 1)
            throw new IllegalArgumentException("Borrowing ID must be a positive number.");

        if (librarianId < 1)
            throw new IllegalArgumentException("Librarian ID must be a positive number.");

        if (!borrowingDao.idExistsBorrowing(borrowingId))
            throw new IllegalArgumentException("Borrowing record not found.");

        if (!librarianDao.idExistsLibrarian(librarianId))
            throw new IllegalArgumentException("Librarian not found.");

        Borrowing borrowing = borrowingDao.findBorrowingById(borrowingId);

        if (borrowing.getStatus() == StatusEnum.RETURNED)
            throw new IllegalStateException("This book has already been returned.");

        LocalDate returnDate = LocalDate.now();
        borrowing.setReturnDate(returnDate);

        // Determine final status and compute fine if overdue
        if (returnDate.isAfter(borrowing.getDueDate())) {
            borrowing.setStatus(StatusEnum.RETURNED); // returned but was overdue
            long daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(borrowing.getDueDate(), returnDate);
            float fineAmount = daysOverdue * FINE_PER_DAY;

            // Create a fine only if one doesn't already exist for this borrowing
            if (!fineDao.fineExistsForBorrowing(borrowingId)) {
                Fine fine = new Fine(borrowingId, librarianId, fineAmount);
                boolean fineSaved = fineDao.saveFine(fine);
                if (!fineSaved)
                    throw new RuntimeException("Failed to record fine. Please try again.");
            }
        } else {
            borrowing.setStatus(StatusEnum.RETURNED);
        }

        boolean updated = borrowingDao.updateBorrowingOnReturn(borrowing);
        if (!updated)
            throw new RuntimeException("Failed to update borrowing record. Please try again.");

        // Increase available copies
        Book book = bookDao.findBookById(borrowing.getBookId());
        boolean bookUpdated = bookDao.updateAvailableCopies(borrowing.getBookId(), book.getAvailableCopies() + 1);
        if (!bookUpdated)
            throw new RuntimeException("Failed to update book availability. Please try again.");

        // Notif next student in resrvation queue (mark their reservation as Fulfiled)
        List<Reservation> pendingReservations = reservationDao.findPendingReservationsByBook(borrowing.getBookId());
        if (!pendingReservations.isEmpty()) {
            Reservation next = pendingReservations.get(0);
            next.setStatus(ReservationStatus.FULFILLED);
            reservationDao.updateReservationStatus(next);
        }
    }

    
    //  GET ALL BORROWINGS
    public List<Borrowing> getAllBorrowings() {
        List<Borrowing> list = borrowingDao.findAllBorrowings();
        if (list.isEmpty())
            throw new IllegalStateException("No borrowing records found.");
        return list;
    }

  
    //  GET BORROWINGS BY STUDENT

    public List<Borrowing> getBorrowingsByStudent(int studentId) {  //frontend parameters
        if (studentId < 1)
            throw new IllegalArgumentException("Student ID must be a positive number.");

        if (!studentDao.idExistsStudent(studentId))
            throw new IllegalArgumentException("Student not found.");

        List<Borrowing> list = borrowingDao.findBorrowingsByStudent(studentId);
        if (list.isEmpty())
            throw new IllegalStateException("No borrowing records found for this student.");
        return list;
    }

   
    //  GET ACTIVE BORROWINGS
  
    public List<Borrowing> getActiveBorrowings() {
        borrowingDao.markOverdueBorrowings();
        List<Borrowing> list = borrowingDao.findActiveBorrowings();
        if (list.isEmpty())
            throw new IllegalStateException("No active borrowings found.");
        return list;
    }


    //  GET OVERDUE BORROWINGS

    public List<Borrowing> getOverdueBorrowings() {
        borrowingDao.markOverdueBorrowings();
        List<Borrowing> list = borrowingDao.findOverdueBorrowings();
        if (list.isEmpty())
            throw new IllegalStateException("No overdue borrowings found.");
        return list;
    }


    //  GET BY ID
   
    public Borrowing getBorrowingById(int borrowingId) {  //frontend parameters
        if (borrowingId < 1)
            throw new IllegalArgumentException("Borrowing ID must be a positive number.");

        Borrowing borrowing = borrowingDao.findBorrowingById(borrowingId);
        if (borrowing == null)
            throw new IllegalStateException("Borrowing record not found for ID: " + borrowingId);
        return borrowing;
    }

   
    //  DELETE BORROWING RECORD
   
    public void deleteBorrowing(int borrowingId) {   //frontend parameters
        if (borrowingId < 1)
            throw new IllegalArgumentException("Borrowing ID must be a positive number.");

        if (!borrowingDao.idExistsBorrowing(borrowingId))
            throw new IllegalArgumentException("Borrowing record not found.");

        boolean deleted = borrowingDao.deleteBorrowing(borrowingId);
        if (!deleted)
            throw new RuntimeException("Failed to delete borrowing record. Please try again.");
    }
}
