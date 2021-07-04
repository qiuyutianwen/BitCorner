package com.bitcorner.bitCorner.services;

import com.bitcorner.bitCorner.models.Bill;
import com.bitcorner.bitCorner.models.Orders;
import com.bitcorner.bitCorner.models.User;

import java.util.List;
import java.util.Map;

public interface BillService {
    Bill createBill(Bill bill);

    List<Bill> getBills();

    List<Bill> getBillsBySenderId(long id);

    Bill getBillById(long id);

    Map<String, Object> converBillToMap(Bill bill);

    List<Map<String, Object>> convertBillListToMap(List<Bill> bills);

    void updateBill(Bill bill);

    Bill cancelBill(Bill bill);

    Bill rejectBill(Bill bill);

    Bill payBill(long id, String paidType, Double paidAmount, double transaction_fee);

    void sendEmail(String email, String title, String msg);
}
