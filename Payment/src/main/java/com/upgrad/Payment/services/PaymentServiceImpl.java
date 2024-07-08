package com.upgrad.Payment.services;

import com.upgrad.Payment.dao.PaymentDAO;
import com.upgrad.Payment.dto.BookingDTO;
import com.upgrad.Payment.entities.TransactionDetailsEntity;
import com.upgrad.Payment.exception.InvalidArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final String CARD = "CARD";
    private final String UPI = "UPI";

    @Value("${booking.url}")
    private String bookingUrl;

    @Autowired
    PaymentDAO paymentDAO;

    @Autowired
    RestTemplate restTemplate;

    /**
     * Accepts and processes payment details.
     * Validates the payment mode, retrieves booking details from the booking service,
     * logs the booking details, and saves the transaction details in the database.
     *
     * @param transaction The transaction details to be processed.
     * @return The saved transaction details.
     */
    @Override
    public TransactionDetailsEntity acceptPaymentDetails(TransactionDetailsEntity transaction) {
        BookingDTO bookingDTO;
        Map<String, String> transactionUriMap = new HashMap<>();
        transactionUriMap.put("bookingId", String.valueOf(transaction.getBookingId()));
        System.out.println("transactionUriMap" + transactionUriMap);

        // Validate payment mode
        if (!(transaction.getPaymentMode().equals(CARD) || transaction.getPaymentMode().equals(UPI))) {
            throw new InvalidArgumentException("Invalid mode of payment");
        }

        try {
            // Retrieve booking details from the booking service
            bookingDTO =
                    restTemplate.getForObject(bookingUrl+"/"+transaction.getBookingId(), BookingDTO.class);
        } catch (Exception e) {
            throw new InvalidArgumentException("Invalid booking Id");
        }

        // Log booking details
        String message = "Booking confirmed for user with aadhaar number: "
                + bookingDTO.getAadharNumber()
                + "    |    "
                + "Here are the booking details:    " + bookingDTO.toString();
        System.out.println(message);

        // Save transaction details
        return paymentDAO.save(transaction);
    }

    /**
     * Retrieves transaction details by transaction ID.
     * If the transaction ID is invalid, an exception is thrown.
     *
     * @param id The transaction ID.
     * @return The transaction details associated with the given ID.
     */
    @Override
    public TransactionDetailsEntity getTransactionDetails(int id) {
        TransactionDetailsEntity payment;
        try {
            // Retrieve transaction details by ID
            payment = paymentDAO.findById(id).get();
        } catch (Exception e) {
            throw new InvalidArgumentException("Invalid Transaction Id");
        }
        return payment;
    }
}
