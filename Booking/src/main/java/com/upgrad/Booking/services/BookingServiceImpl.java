package com.upgrad.Booking.services;

import com.upgrad.Booking.dao.BookingDAO;
import com.upgrad.Booking.entities.BookingInfoEntity;
import com.upgrad.Booking.exception.InvalidArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class BookingServiceImpl implements BookingService{

    @Autowired
    BookingDAO bookingDAO;

    private String bookingAppUrl;

    public String getBookingAppUrl() { return bookingAppUrl; }

    @Override
    public BookingInfoEntity acceptBooking(BookingInfoEntity booking) {
        booking.setRoomNumbers(getRandomNumbers(booking.getNumOfRooms()));
        System.out.println("booking" + booking);
        if (booking.getFromDate() == null || booking.getToDate() == null) {
            throw new IllegalArgumentException("From date and to date must be specified.");
        }
        LocalDateTime fromDate = LocalDateTime.parse(
                booking.getFromDate().toString(), DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z yyyy"));
        LocalDateTime toDate = LocalDateTime.parse(
                booking.getToDate().toString(), DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z yyyy"));
        int noOfDays = (int) ChronoUnit.DAYS.between(fromDate, toDate);
        booking.setRoomPrice(calculateRoomPrice(booking.getNumOfRooms(),noOfDays));
        booking.setBookedOn(LocalDateTime.now());
        return bookingDAO.save(booking);
    }

    @Override
    public BookingInfoEntity getBookingDetails(int id) {
        return bookingDAO.findById(id).orElseThrow(
                ()-> new InvalidArgumentException("Invalid Booking Id"));
    }

    public static List<String> getRandomNumbers(int count) {
        Random rand = new Random();
        int upperBound = 100;
        ArrayList<String>numberList = new ArrayList<String>();

        for (int i=0; i<count; i++){
            numberList.add(String.valueOf(rand.nextInt(upperBound)));
        }

        return numberList;
    }

    public static int calculateRoomPrice(int noOfRooms, int noOfDays){
        return 1000 * noOfRooms * noOfDays;
    }
}
