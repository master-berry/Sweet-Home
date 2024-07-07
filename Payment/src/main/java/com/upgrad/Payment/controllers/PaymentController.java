package com.upgrad.Payment.controllers;


import com.upgrad.Payment.dto.PaymentDTO;
import com.upgrad.Payment.entities.TransactionDetailsEntity;
import com.upgrad.Payment.exception.InvalidArgumentException;
import com.upgrad.Payment.services.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    ModelMapper mapper;

    @PostMapping(value = "/transaction", consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createTransaction(@RequestBody PaymentDTO paymentDTO){
        System.out.println("We are here");
        TransactionDetailsEntity transactionDetails = mapper.map(paymentDTO, TransactionDetailsEntity.class);

        TransactionDetailsEntity savedTransaction = paymentService.acceptPaymentDetails(transactionDetails);
        PaymentDTO paymentDTO1 = mapper.map(savedTransaction,PaymentDTO.class);

        return new ResponseEntity(paymentDTO1.getTransactionId(), HttpStatus.CREATED);
    }


    @GetMapping(value = "/transaction/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTransaction(@PathVariable int id){
        TransactionDetailsEntity transactionDetails = paymentService.getTransactionDetails(id);
        System.out.println("transactionDetails"+transactionDetails);
        if(transactionDetails==null){
            throw new InvalidArgumentException("Invalid Transaction Id");
        }

        PaymentDTO paymentDTO = mapper.map(transactionDetails, PaymentDTO.class);

        return new ResponseEntity(paymentDTO,HttpStatus.OK);
    }
}
