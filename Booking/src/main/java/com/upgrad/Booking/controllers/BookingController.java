package com.upgrad.Booking.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgrad.Booking.dao.BookingDAO;
import com.upgrad.Booking.dto.BookingDTO;
import com.upgrad.Booking.dto.PaymentDTO;
import com.upgrad.Booking.entities.BookingInfoEntity;
import com.upgrad.Booking.exception.dto.ErrorResponse;
import com.upgrad.Booking.services.BookingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @Value("${transaction.url}")
    private String transactionUrl;

    /**
     * Creates a new booking.
     * Maps the incoming BookingDTO to a BookingInfoEntity, processes it using the booking service,
     * maps the saved entity back to a BookingDTO, and returns it with HTTP status CREATED.
     *
     * @param bookingDTO The booking details received in the request body.
     * @return ResponseEntity with the saved booking details and HTTP status CREATED.
     */
    @PostMapping(value = "/booking", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@RequestBody BookingDTO bookingDTO) {
        BookingInfoEntity newBooking = modelMapper.map(bookingDTO, BookingInfoEntity.class);
        BookingInfoEntity savedBooking = bookingService.acceptBooking(newBooking);
        BookingDTO newBookingDTO = modelMapper.map(savedBooking, BookingDTO.class);
        return new ResponseEntity(newBookingDTO, HttpStatus.CREATED);
    }

    /**
     * Retrieves booking details based on the booking ID.
     * Maps the retrieved BookingInfoEntity to a BookingDTO and returns it with HTTP status OK.
     *
     * @param id The booking ID.
     * @return ResponseEntity with the booking details and HTTP status OK.
     */
    @GetMapping(value = "/booking/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getBookingDetails(@PathVariable(name = "id") int id) {
        BookingInfoEntity booking = bookingService.getBookingDetails(id);
        BookingDTO bookingDTO = modelMapper.map(booking, BookingDTO.class);
        return new ResponseEntity<>(bookingDTO, HttpStatus.OK);
    }

    /**
     * Creates a new transaction for a specific booking.
     * Sends the payment details to the payment service, retrieves the transaction ID,
     * updates the booking with the transaction ID, handles exceptions, and returns the updated booking details.
     *
     * @param paymentDTO The payment details received in the request body.
     * @param bookingId The booking ID to which the transaction is related.
     * @return ResponseEntity with the updated booking details and HTTP status CREATED.
     * @throws JsonProcessingException If there is an error processing JSON during exception handling.
     */
    @PostMapping(value = "booking/{bookingId}/transaction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createTransaction(@RequestBody PaymentDTO paymentDTO, @PathVariable(name = "bookingId") int bookingId)
            throws JsonProcessingException {

        BookingDTO bookingDTO = null;
        ErrorResponse error;
        try{
            int transactionId =
                    restTemplate.postForObject(transactionUrl,paymentDTO, int.class);

            BookingInfoEntity booking = bookingService.getBookingDetails(bookingId);
            booking.setTransactionId(transactionId);
            bookingDTO = modelMapper.map(booking, BookingDTO.class);

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
