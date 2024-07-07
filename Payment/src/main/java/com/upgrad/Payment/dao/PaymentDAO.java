package com.upgrad.Payment.dao;

import com.upgrad.Payment.entities.TransactionDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDAO extends JpaRepository<TransactionDetailsEntity, Integer> {
}
