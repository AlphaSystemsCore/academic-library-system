package com.sys.service;

import com.sys.dao.*;
import com.sys.model.Reservation;
import com.sys.model.Reservation.ReservationStatus;
import java.util.List;

public class ReservationService{

    private ReservationDao reservationDao = new ReservationDao();
    private StudentDao     studentDao     = new StudentDao();
    private BookDao        bookDao        = new BookDao();

   
}