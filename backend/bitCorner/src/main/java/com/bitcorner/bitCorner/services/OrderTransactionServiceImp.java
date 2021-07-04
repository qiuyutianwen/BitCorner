package com.bitcorner.bitCorner.services;

import com.bitcorner.bitCorner.models.OrderTransaction;
import com.bitcorner.bitCorner.repositories.OrderTransactionRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderTransactionServiceImp implements OrderTransactionService{
    private final OrderTransactionRepository orderTransactionRepository;

    public OrderTransactionServiceImp(OrderTransactionRepository orderTransactionRepository){
        this.orderTransactionRepository = orderTransactionRepository;
    }

    @Override
    public long createOrderTransactionReturnID(OrderTransaction orderTransaction) {
        return orderTransactionRepository.save(orderTransaction).getId();
    }

    @Override
    public OrderTransaction getLatestOrderTransaction() {
        return orderTransactionRepository.findLatestTransaction();
    }

    @Override
    public List<OrderTransaction> getAllRecords() {
        return orderTransactionRepository.findAll();
    }

}
