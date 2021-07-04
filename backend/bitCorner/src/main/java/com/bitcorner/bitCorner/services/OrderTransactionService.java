package com.bitcorner.bitCorner.services;

import com.bitcorner.bitCorner.models.OrderTransaction;

import java.util.List;
import java.util.Map;

public interface OrderTransactionService {
    long createOrderTransactionReturnID(OrderTransaction orderTransaction);
    OrderTransaction getLatestOrderTransaction();
    List<OrderTransaction> getAllRecords();

}
