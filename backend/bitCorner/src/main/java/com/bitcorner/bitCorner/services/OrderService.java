package com.bitcorner.bitCorner.services;

import com.bitcorner.bitCorner.models.OrderTransaction;
import com.bitcorner.bitCorner.models.Orders;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Orders createOrder(Orders order);
    void updateOrder(Orders order);
    Map<String, Object> convertOrderToMap(Orders order);
    Orders getOrderById(long id);
    List<Orders> getOrdersByUsername(String username);
    List<Map<String, Object>> convertOrderListToMap(List<Orders> orders);
    List<Orders> getOrdersByTypeAndStatus(String type, String status);
    List<Orders> getAllOrders();
    String matchOrders(Orders new_order);
    List<Orders> getOpeningOrdersByUsername(String username);
    List<Map<String, Object>> convertOrderTransactionListToMap(List<OrderTransaction> orderTransactions);
    Orders getLatestNotCancelledBuyOrder();
    Orders getLatestNotCancelledSellOrder();
    void sendEmail(String email, String title, String msg);
}
