package com.sys.service;

import java.time.LocalDate;
import java.util.List;
import com.sys.dao.BorrowingDao;
import com.sys.dao.FineDao;
import com.sys.dao.StudentDao;
import com.sys.model.Fine;

public class FineService {

    private final FineDao fineDao = new FineDao();
    private final StudentDao studentDao = new StudentDao();
    private final BorrowingDao borrowingDao = new BorrowingDao();

  
    //  PAY FINE
 
    public void payFine(int fineId) {

        if (fineId < 1)
            throw new IllegalArgumentException("Fine ID must be a positive number.");

        if (!fineDao.idExistsFine(fineId))
            throw new IllegalArgumentException("Fine not found.");

        Fine fine = fineDao.findFineById(fineId);

        if (fine.isPaid())
            throw new IllegalStateException("This fine has already been paid.");

        fine.markAsPaid();
        fine.setPaidDate(LocalDate.now());

        boolean updated = fineDao.markFineAsPaid(fine);
        if (!updated)
            throw new RuntimeException("Failed to mark fine as paid. Please try again.");
    }

    
    //  GET ALL FINES
    
    public List<Fine> getAllFines() {
        List<Fine> fines = fineDao.findAllFines();
        if (fines.isEmpty())
            throw new IllegalStateException("No fines found.");
        return fines;
    }

    //  GET FINES BY STUDENT
  
    public List<Fine> getFinesByStudent(int studentId) {
        if (studentId < 1)
            throw new IllegalArgumentException("Student ID must be a positive number.");

        if (!studentDao.idExistsStudent(studentId))
            throw new IllegalArgumentException("Student not found.");

        List<Fine> fines = fineDao.findFinesByStudent(studentId);
        if (fines.isEmpty())
            throw new IllegalStateException("No fines found for this student.");
        return fines;
    }

    //  GET UNPAID FINES BY STUDENT

    public List<Fine> getUnpaidFinesByStudent(int studentId) {  //frontend parameters--srydent
        if (studentId < 1)
            throw new IllegalArgumentException("Student ID must be a positive number.");

        if (!studentDao.idExistsStudent(studentId))
            throw new IllegalArgumentException("Student not found.");

        List<Fine> fines = fineDao.findUnpaidFinesByStudent(studentId);
        if (fines.isEmpty())
            throw new IllegalStateException("No unpaid fines for this student.");
        return fines;
    }

    
    //  GET FINE BY ID

    public Fine getFineById(int fineId) {
        if (fineId < 1)
            throw new IllegalArgumentException("Fine ID must be a positive number.");

        Fine fine = fineDao.findFineById(fineId);
        if (fine == null)
            throw new IllegalStateException("Fine not found for ID: " + fineId);
        return fine;
    }

   
    //  GET FINE BY BORROWING
  
    public Fine getFineByBorrowing(int borrowingId) {
        if (borrowingId < 1)
            throw new IllegalArgumentException("Borrowing ID must be a positive number.");

        if (!borrowingDao.idExistsBorrowing(borrowingId))
            throw new IllegalArgumentException("Borrowing record not found.");

        Fine fine = fineDao.findFineByBorrowing(borrowingId);
        if (fine == null)
            throw new IllegalStateException("No fine found for borrowing ID: " + borrowingId);
        return fine;
    }


    //  DELETE FINE (admin ) /
 
    public void deleteFine(int fineId) { //frontend parameters==admin
        if (fineId < 1)
            throw new IllegalArgumentException("Fine ID must be a positive number.");

        if (!fineDao.idExistsFine(fineId))
            throw new IllegalArgumentException("Fine not found.");

        boolean deleted = fineDao.deleteFine(fineId);
        if (!deleted)
            throw new RuntimeException("Failed to delete fine. Please try again.");
    }

   
    //  CHECK IF STUDENT HAS UNPAID FINES
  
    public boolean studentHasUnpaidFines(int studentId) {
        if (studentId < 1)
            throw new IllegalArgumentException("Student ID must be a positive number.");

        return !fineDao.findUnpaidFinesByStudent(studentId).isEmpty();
    }
}
