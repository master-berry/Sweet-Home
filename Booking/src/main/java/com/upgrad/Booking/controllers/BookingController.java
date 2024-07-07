package com.upgrad.Booking.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgrad.Booking.dao.BookingDAO;
import com.upgrad.Booking.dto.BookingDTO;
import com.upgrad.Booking.dto.PaymentDTO;
import com.upgrad.Booking.entities.BookingInfoEntity;
import com.upgrad.Booking.exception.InvalidArgumentException;
import com.upgrad.Booking.exception.dto.ErrorResponse;
import com.upgrad.Booking.services.BookingService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/hotel")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @Autowired
    BookingDAO bookingDAO;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    RestTemplate restTemplate;

    @PostMapping(value = "/booking", consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@RequestBody BookingDTO bookingDTO){
        BookingInfoEntity newBooking = modelMapper.map(bookingDTO, BookingInfoEntity.class);
        BookingInfoEntity savedBooking = bookingService.acceptBooking(newBooking);
        BookingDTO newBookingDTO = modelMapper.map(savedBooking, BookingDTO.class);
        return new ResponseEntity(newBookingDTO, HttpStatus.CREATED);
    }

    @GetMapping(value = "/booking/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getBookingDetails(@PathVariable(name="id") int id) {
        BookingInfoEntity booking = bookingService.getBookingDetails(id);
        BookingDTO bookingDTO = modelMapper.map(booking, BookingDTO.class);

        return new ResponseEntity<>(bookingDTO, HttpStatus.OK);
    }

    @PostMapping(value = "booking/{bookingId}/transaction", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createTransaction(@RequestBody PaymentDTO paymentDTO, @PathVariable(name="bookingId") int bookingId)
            throws JsonProcessingException {

        BookingDTO bookingDTO = null;
                ErrorResponse error;
        try{
            PaymentDTO payment =
                    restTemplate.postForObject("${transaction.url}",paymentDTO, PaymentDTO.class);
            System.out.println("payment"+payment.getTransactionId());
            BookingInfoEntity booking = bookingService.getBookingDetails(bookingId);
            booking.setTransactionId(payment.getTransactionId());
            bookingDTO = modelMapper.map(booking, BookingDTO.class);
//        BookingInfoEntity newBooking = modelMapper.map(booking,)
//        bookingDTO.setTransactionId(transaction.getTransactionId());
//        System.out.println(bookingDTO.getTransactionId());
//        HttpEntity<BookingDTO> updatedBooking = new HttpEntity<>(bookingDTO,null);
//        restTemplate.exchange(bookingAppUrl+"/"+bookingDTO.getBookingId(),
//                HttpMethod.PUT,updatedBooking,Void.class);
//      BookingDTO booking = restTemplate.getForObject(bookingAppUrl+"/"+paymentDTO.getBookingId(), BookingDTO.class);
            bookingDAO.save(booking);

        } catch(Exception e){
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = e.getLocalizedMessage();
            int startIndex = jsonString.indexOf(":") + 2; // Adding 2 to skip colon and space

            // Extract the substring from startIndex to the end
            String extractedJson = jsonString.substring(startIndex).replaceAll("^\"|\"$", "");

            // Extracting the ErrorResponse from exception
            ErrorResponse[] errorResponsesArray = objectMapper.readValue(extractedJson, ErrorResponse[].class);
            List<ErrorResponse> errorResponses = Arrays.asList(errorResponsesArray);

            return new ResponseEntity(errorResponses,HttpStatus.BAD_REQUEST);
        }

       return new ResponseEntity(bookingDTO, HttpStatus.CREATED);
    }
}
