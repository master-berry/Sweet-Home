package com.upgrad.Booking.dao;

import com.upgrad.Booking.entities.BookingInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingDAO extends JpaRepository<BookingInfoEntity, Integer> {
}
