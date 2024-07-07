package com.upgrad.Payment.services;

import com.upgrad.Payment.dao.PaymentDAO;
import com.upgrad.Payment.dto.BookingDTO;
import com.upgrad.Payment.entities.TransactionDetailsEntity;
import com.upgrad.Payment.exception.InvalidArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService{
    private final String CARD = "CARD";
    private final String UPI = "UPI";

//    @Value("${booking.url}")
//    private String bookingAppUrl;
//
//
//
//    public String getTransactionAppUrl() { return transactionAppUrl; }

    @Autowired
    PaymentDAO paymentDAO;

    @Autowired
    RestTemplate restTemplate;


    @Override
    public TransactionDetailsEntity acceptPaymentDetails(TransactionDetailsEntity transaction) {
        BookingDTO bookingDTO;
        Map<String, String> transactionUriMap = new HashMap<>();
        transactionUriMap.put("bookingId",String.valueOf(transaction.getBookingId()));
        System.out.println("transactionUriMap"+transactionUriMap);
        if(!(transaction.getPaymentMode().equals(CARD) || transaction.getPaymentMode().equals(UPI))){
          throw new InvalidArgumentException("Invalid mode of payment");
        }
        try {
            bookingDTO =
                    restTemplate.getForObject("${booking.url}"+"/"+transaction.getBookingId(), BookingDTO.class);
        } catch (Exception e) {
            throw new InvalidArgumentException("Invalid booking Id");
        }

        String message = "Booking confirmed for user with aadhaar number: "
                + bookingDTO.getAadharNumber()
                +    "    |    "
                + "Here are the booking details:    " + bookingDTO.toString();
        System.out.println(message);
        return paymentDAO.save(transaction);
    }

    @Override
    public TransactionDetailsEntity getTransactionDetails(int id) {
        TransactionDetailsEntity payment;
       try {
           payment = paymentDAO.findById(id).get();
       } catch (Exception e){
           throw new InvalidArgumentException("Invalid Transaction Id");
       }
        return payment;
    }


}
