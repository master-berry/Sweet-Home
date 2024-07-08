package com.upgrad.Booking.services;

import com.upgrad.Booking.dao.BookingDAO;
import com.upgrad.Booking.entities.BookingInfoEntity;
import com.upgrad.Booking.exception.InvalidArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    BookingDAO bookingDAO;

    private String bookingAppUrl;

    public String getBookingAppUrl() {
        return bookingAppUrl;
    }

    /**
     * Accepts and processes a booking.
     * Generates random room numbers, validates the booking dates, calculates the room price,
     * sets the booking date to the current date and time, and saves the booking to the database.
     *
     * @param booking The booking details to be processed.
     * @return The saved booking details.
     */
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
        booking.setRoomPrice(calculateRoomPrice(booking.getNumOfRooms(), noOfDays));
        booking.setBookedOn(LocalDateTime.now());
        return bookingDAO.save(booking);
    }

    /**
     * Retrieves booking details based on the booking ID.
     * Throws an InvalidArgumentException if the booking ID is invalid.
     *
     * @param id The booking ID.
     * @return The booking details associated with the given ID.
     */
    @Override
    public BookingInfoEntity getBookingDetails(int id) {
        return bookingDAO.findById(id).orElseThrow(
                () -> new InvalidArgumentException("Invalid Booking Id"));
    }

    /**
     * Generates a list of random room numbers.
     *
     * @param count The number of random room numbers to generate.
     * @return A list of random room numbers as strings.
     */
    public static List<String> getRandomNumbers(int count) {
        Random rand = new Random();
        int upperBound = 100;
        ArrayList<String> numberList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            numberList.add(String.valueOf(rand.nextInt(upperBound)));
        }

        return numberList;
    }

    /**
     * Calculates the room price based on the number of rooms and the number of days.
     *
     * @param noOfRooms The number of rooms booked.
     * @param noOfDays The number of days the rooms are booked for.
     * @return The total room price.
     */
    public static int calculateRoomPrice(int noOfRooms, int noOfDays) {
        return 1000 * noOfRooms * noOfDays;
    }
}
