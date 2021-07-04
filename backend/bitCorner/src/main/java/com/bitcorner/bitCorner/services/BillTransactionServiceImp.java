package com.bitcorner.bitCorner.services;

import com.bitcorner.bitCorner.models.Bill;
import com.bitcorner.bitCorner.models.BillTransaction;
import com.bitcorner.bitCorner.repositories.BillTransactionRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BillTransactionServiceImp implements BillTransactionService {

    private final BillTransactionRepository billTransactionRepository;
    private final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public BillTransactionServiceImp(BillTransactionRepository billTransactionRepository) {
        this.billTransactionRepository = billTransactionRepository;
    }

    @Override
    public List<BillTransaction> getBillTransactions() {
        return billTransactionRepository.findAll();
    }

    @Override
    public Map<String, Object> converBillTransactionToMap(BillTransaction billTransaction) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", billTransaction.getId());
        map.put("receiver_id", billTransaction.getReceiver_id());
        map.put("receiver_email", billTransaction.getReceiver_email());
        map.put("receiver_nickname", billTransaction.getReceiver_nickname());
        map.put("sender_id", billTransaction.getSender_id());
        map.put("sender_email", billTransaction.getSender_email());
        map.put("sender_nickname", billTransaction.getSender_nickname());
        map.put("status", billTransaction.getStatus());
        map.put("currency", billTransaction.getCurrency());
        map.put("amount", billTransaction.getAmount());
        map.put("description", billTransaction.getDescription());
        Date start_date = billTransaction.getStart_date();
        map.put("start_date", DATE_TIME_FORMAT.format(start_date));
        map.put("transaction_fee", billTransaction.getTransaction_fee());
        map.put("transaction_fee_currency_type", billTransaction.getTransaction_fee_currency_type());
        return map;
    }


    @Override
    public List<Map<String, Object>> convertBillTransactionListToMap(List<BillTransaction> billTransactions) {
        List<Map<String, Object>> res = new ArrayList<>();
        for(BillTransaction billTransaction : billTransactions){
            res.add(converBillTransactionToMap(billTransaction));
        }
        return res;
    }
}
