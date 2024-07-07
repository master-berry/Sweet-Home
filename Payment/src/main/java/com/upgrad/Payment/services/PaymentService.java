package com.upgrad.Payment.services;

import com.upgrad.Payment.entities.TransactionDetailsEntity;
import org.springframework.stereotype.Service;



public interface PaymentService {

    public TransactionDetailsEntity acceptPaymentDetails(TransactionDetailsEntity transaction);

    public TransactionDetailsEntity getTransactionDetails(int id);
}
