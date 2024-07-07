package com.upgrad.Booking.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name="booking")
public class BookingInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int bookingId;
    private Date fromDate;
    private Date toDate ;
    private String aadharNumber ;
    private int numOfRooms ;
    @ElementCollection
    private List<String> roomNumbers = new ArrayList<>();
    @Column(nullable = false, columnDefinition = "int default 1000")
    private int roomPrice ;
    private int transactionId ;
    private LocalDateTime bookedOn;

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public int getNumOfRooms() {
        return numOfRooms;
    }

    public void setNumOfRooms(int numOfRooms) {
        this.numOfRooms = numOfRooms;
    }

    public List<String> getRoomNumbers() {
        return roomNumbers;
    }

    public void setRoomNumbers(List<String> roomNumbers) {
        this.roomNumbers = roomNumbers;
    }

    public int getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(int roomPrice) {
        this.roomPrice = roomPrice;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getBookedOn() {
        return bookedOn;
    }

    public void setBookedOn(LocalDateTime bookedOn) {
        this.bookedOn = bookedOn;
    }

    public BookingInfoEntity(int bookingId, Date fromDate, Date toDate, String aadharNumber,
                             int numOfRooms, List<String> roomNumbers, int roomPrice,
                             int transactionId, LocalDateTime bookedOn) {
        this.bookingId = bookingId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.aadharNumber = aadharNumber;
        this.numOfRooms = numOfRooms;
        this.roomNumbers = roomNumbers;
        this.roomPrice = roomPrice;
        this.transactionId = transactionId;
        this.bookedOn = bookedOn;
    }

    public BookingInfoEntity() {
    }
}
