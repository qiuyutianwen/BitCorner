package com.bitcorner.bitCorner.services;

import com.bitcorner.bitCorner.models.BillTransaction;

import java.util.List;
import java.util.Map;

public interface BillTransactionService {
    List<BillTransaction> getBillTransactions();

//    Bill getBillTransactionById(long id);

    Map<String, Object> converBillTransactionToMap(BillTransaction billTransaction);

    List<Map<String, Object>> convertBillTransactionListToMap(List<BillTransaction> billTransactions);
}
