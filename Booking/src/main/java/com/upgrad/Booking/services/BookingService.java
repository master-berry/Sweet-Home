package com.upgrad.Booking.services;

import com.upgrad.Booking.entities.BookingInfoEntity;

import java.util.ArrayList;

public interface BookingService {

    public BookingInfoEntity acceptBooking(BookingInfoEntity booking);

    public BookingInfoEntity getBookingDetails(int id);

}
