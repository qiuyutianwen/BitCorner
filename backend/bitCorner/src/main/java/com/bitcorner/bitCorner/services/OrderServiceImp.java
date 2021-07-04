package com.bitcorner.bitCorner.services;

import com.bitcorner.bitCorner.dto.BalancePatchDTO;
import com.bitcorner.bitCorner.models.Balance;
import com.bitcorner.bitCorner.models.OrderTransaction;
import com.bitcorner.bitCorner.models.OrderTransactionFee;
import com.bitcorner.bitCorner.models.Orders;
import com.bitcorner.bitCorner.repositories.OrderRepository;
import com.bitcorner.bitCorner.repositories.OrderTransactionFeeRepository;
import com.bitcorner.bitCorner.utility.EmailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
public class OrderServiceImp implements OrderService{
    private final OrderRepository orderRepository;
    private final OrderTransactionFeeRepository orderTransactionFeeRepository;

    private final UserService userService;
    private final OrderTransactionService orderTransactionService;
    private final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final EmailSender emailSender;

    private final BigDecimal EURtoUSD = new BigDecimal("1.22");
    private final BigDecimal GBPtoUSD = new BigDecimal("1.42");
    private final BigDecimal INRtoUSD = new BigDecimal("0.014");
    private final BigDecimal CNYtoUSD = new BigDecimal("0.16");

    public OrderServiceImp(OrderRepository orderRepository, UserService userService, OrderTransactionService orderTransactionService,
                           OrderTransactionFeeRepository orderTransactionFeeRepository, EmailSender emailSender){
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.orderTransactionService = orderTransactionService;
        this.orderTransactionFeeRepository = orderTransactionFeeRepository;
        this.emailSender = emailSender;
    }

    @Override
    public Orders createOrder(Orders order) {
        return orderRepository.save(order);
    }

    @Override
    public void updateOrder(Orders order) {
        orderRepository.save(order);
    }

    @Override
    public Map<String, Object> convertOrderToMap(Orders order) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", order.getId());
        map.put("username", order.getUsername());
        map.put("type", order.getType());
        map.put("transaction_type", order.getTransaction_type());
        map.put("currency", order.getCurrency());
        map.put("price", order.getPrice());
        map.put("btc_quantity", order.getBtc_quantity());
        Date start_date = order.getStart_date();
        map.put("start_date", DATE_TIME_FORMAT.format(start_date));
        map.put("status", order.getStatus());
        return map;
    }

    @Override
    public Orders getOrderById(long id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Orders> getOrdersByUsername(String username){
        return orderRepository.findOrderByUsernameNamedParams(username);
    }

    @Override
    public List<Map<String, Object>> convertOrderListToMap(List<Orders> orders) {
        List<Map<String, Object>> res = new ArrayList<>();
        for(Orders order : orders){
            res.add(convertOrderToMap(order));
        }
        return res;
    }

    @Override
    public List<Map<String, Object>> convertOrderTransactionListToMap(List<OrderTransaction> orderTransactions) {
        List<Map<String, Object>> res = new ArrayList<>();
        for(OrderTransaction orderTransaction: orderTransactions){
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", orderTransaction.getId());
            map.put("buy_id_1", orderTransaction.getBuy_id1());
            map.put("buy_id_2", orderTransaction.getBuy_id2());
            map.put("sell_id_1", orderTransaction.getSell_id1());
            map.put("sell_id_2", orderTransaction.getSell_id2());
            map.put("transaction_price", orderTransaction.getTransaction_price());
            Date transaction_date = orderTransaction.getTransaction_date();
            map.put("transaction_date", DATE_TIME_FORMAT.format(transaction_date));
            map.put("MarketMaker_involved", Boolean.toString(orderTransaction.isMm_involved()));
            map.put("Involved_type", orderTransaction.getInvolved_type());
            map.put("Involved_quantity", orderTransaction.getInvolved_quantity());
            res.add(map);
        }
        return res;
    }

    @Override
    public Orders getLatestNotCancelledBuyOrder() {
        return orderRepository.findLatestOrderByNotStatusAndType("Cancelled", "BUY");
    }

    @Override
    public Orders getLatestNotCancelledSellOrder() {
        return orderRepository.findLatestOrderByNotStatusAndType("Cancelled", "SELL");
    }

    @Override
    public void sendEmail(String email, String title, String msg) {
        emailSender.sendEmail(email, title, msg);
    }

    @Override
    public List<Orders> getOrdersByTypeAndStatus(String type, String status) {
        return orderRepository.findOrdersByTypeAndStatus(type, status);
    }

    @Override
    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    private BigDecimal ConvertPriceToUSD(Orders order){
        if("Market Order".equals(order.getTransaction_type())){
            OrderTransaction latestOrderTransaction = orderTransactionService.getLatestOrderTransaction();
            if(latestOrderTransaction == null) return new BigDecimal("50000.00");
            return new BigDecimal(latestOrderTransaction.getTransaction_price());  //get LTP
        }
        BigDecimal res = new BigDecimal(order.getPrice());
        switch (order.getCurrency()){
            case "EUR":
                return res.multiply(EURtoUSD);
            case "GBP":
                return res.multiply(GBPtoUSD);
            case "INR":
                return res.multiply(INRtoUSD);
            case "CNY":
                return res.multiply(CNYtoUSD);
            default:
                return res;
        }
    }

    //0: match, exactly
    //-1: match, but need more buy quantity
    //-2: not match, buy quantity is much less than sell quantity
    //1: match, but need more sell quantity
    //2: not match, sell quantity is much less than buy quantity
    private Integer quantity_match(BigDecimal quantity_buy, BigDecimal quantity_sell){
        BigDecimal quantity_subtract_abs = quantity_buy.subtract(quantity_sell).abs();
        int scale = 2;
        if(quantity_buy.compareTo(quantity_sell) > 0){
            BigDecimal difference = quantity_subtract_abs.divide(quantity_buy, scale, RoundingMode.CEILING);
            if(difference.compareTo(new BigDecimal("0.1")) <= 0){
                return 1;
            } else {
                return 2;
            }
        } else if(quantity_buy.compareTo(quantity_sell) < 0){
            BigDecimal difference = quantity_subtract_abs.divide(quantity_sell, scale, RoundingMode.CEILING);
            if(difference.compareTo(new BigDecimal("0.1")) <= 0){
                return -1;
            } else {
                return -2;
            }
        } else{
            return 0;
        }


    }

    private void changeBalance(Orders order, BigDecimal transaction_price, long transaction_id){
        String username = order.getUsername();
        String btc_quantity = order.getBtc_quantity();
        System.out.println(btc_quantity);
        String money_operation = order.getType().equals("BUY")?"withdraw":"deposit";
        String btc_operation = order.getType().equals("SELL")?"withdraw":"deposit";
        Double btc_amount = Double.parseDouble(btc_quantity);
        System.out.println(btc_amount);
        Double money_amount = 0D;
        Double fee = 0D;
        String currency = "";
        int scale = 2;
        System.out.println(order.getCurrency());
        BigDecimal money_amount_USD = transaction_price.multiply(new BigDecimal(btc_quantity));
        switch(order.getCurrency()){
            case "EUR":
                currency = "eur";
                money_amount = getMoney_amount(order, money_amount, scale, money_amount_USD, EURtoUSD, transaction_id)[0];
                fee = getMoney_amount(order, money_amount, scale, money_amount_USD, EURtoUSD, transaction_id)[1];
                if(order.getType().equals("SELL")){
                    orderTransactionFeeRepository.save(new OrderTransactionFee(transaction_id, "EUR", fee.toString()));
                }
                break;
            case "GBP":
                currency = "gbp";
                money_amount = getMoney_amount(order, money_amount, scale, money_amount_USD, GBPtoUSD, transaction_id)[0];
                fee = getMoney_amount(order, money_amount, scale, money_amount_USD, GBPtoUSD, transaction_id)[1];
                if(order.getType().equals("SELL")){
                    orderTransactionFeeRepository.save(new OrderTransactionFee(transaction_id, "GBP", fee.toString()));
                }
                break;
            case "INR":
                currency = "inr";
                money_amount = getMoney_amount(order, money_amount, scale, money_amount_USD, INRtoUSD, transaction_id)[0];
                fee = getMoney_amount(order, money_amount, scale, money_amount_USD, INRtoUSD, transaction_id)[1];
                if(order.getType().equals("SELL")){
                    orderTransactionFeeRepository.save(new OrderTransactionFee(transaction_id, "INR", fee.toString()));
                }
                break;
            case "CNY":
                currency = "cny";
                money_amount = getMoney_amount(order, money_amount, scale, money_amount_USD, CNYtoUSD, transaction_id)[0];
                fee = getMoney_amount(order, money_amount, scale, money_amount_USD, CNYtoUSD, transaction_id)[1];
                if(order.getType().equals("SELL")){
                    orderTransactionFeeRepository.save(new OrderTransactionFee(transaction_id, "CNY", fee.toString()));
                }
                break;
            default:
                currency = "usd";
                money_amount = getMoney_amount(order, money_amount, scale, money_amount_USD, new BigDecimal("1"), transaction_id)[0];
                fee = getMoney_amount(order, money_amount, scale, money_amount_USD, new BigDecimal("1"), transaction_id)[1];
                if(order.getType().equals("SELL")){
                    orderTransactionFeeRepository.save(new OrderTransactionFee(transaction_id, "USD", fee.toString()));
                }
        }
        BalancePatchDTO changeMoney = BalancePatchDTO.builder()
                .username(username)
                .amount(money_amount)
                .currency(currency)
                .operation(money_operation).build();
        BalancePatchDTO changeBtc = BalancePatchDTO.builder()
                .username(username)
                .amount(btc_amount)
                .currency("btc")
                .operation(btc_operation).build();
        Balance moneyChanged = userService.updateBalance(changeMoney);
        System.out.println(moneyChanged.toString());
        Balance btcChanged = userService.updateBalance(changeBtc);
        System.out.println(btcChanged.toString());
        //send email
        String nickname = userService.findByUsername(username).getNickname();
        String msg = String.format("Hello %s, \n\nYour Order has been matched!\n\nThank you,\nTeam BitCorner", nickname);
        sendEmail(username, "Order matched", msg);
    }

    private Double[] getMoney_amount(Orders order, Double money_amount, int scale, BigDecimal money_amount_USD, BigDecimal CurrencyToUSD, long transaction_id) {
        BigDecimal fee_USD = new BigDecimal("0");
        System.out.println("money_amount_USD: " + money_amount_USD.toString());
        if(order.getType().equals("SELL")){
            if(money_amount_USD.compareTo(new BigDecimal("10000")) < 0){
                money_amount_USD = money_amount_USD.multiply(new BigDecimal("0.9999"));
                fee_USD = money_amount_USD.multiply(new BigDecimal("0.0001"));
            }else{
                money_amount_USD = money_amount_USD.subtract(new BigDecimal("1"));
                fee_USD = new BigDecimal("1");
            }
        }
        money_amount = money_amount_USD.divide(CurrencyToUSD, scale, RoundingMode.CEILING).doubleValue();
        System.out.println("money_amount: " + money_amount);
        Double fee = fee_USD.divide(CurrencyToUSD, scale, RoundingMode.CEILING).doubleValue();
        System.out.println("fee: " + fee);
        return new Double[]{money_amount, fee};
    }

    @Override
    public String matchOrders(Orders new_order) {
        String new_order_type = new_order.getType();

        //match orders by timestamp
        List<Orders> buy_order = orderRepository.findOrdersByTypeAndStatus("BUY", "Open");
        List<Orders> sell_order = orderRepository.findOrdersByTypeAndStatus("SELL", "Open");

        if("SELL".equals(new_order_type)){
            List<Orders> filtered_buy_order = buy_order.stream()
                    .filter(o -> !new_order.getUsername().equals(o.getUsername()))
                    .filter(o -> ConvertPriceToUSD(o).compareTo(ConvertPriceToUSD(new_order)) >= 0)
                    .collect(toList());
            for(Orders order: filtered_buy_order){
                System.out.println("id: " + order.getId() + " time: " + order.getStart_date());
            }

            for(int i = 0; i < filtered_buy_order.size(); ++i){
                Orders order = filtered_buy_order.get(i);
                Integer quantity_match_res = quantity_match(new BigDecimal(order.getBtc_quantity()), new BigDecimal(new_order.getBtc_quantity()));
                if(quantity_match_res == 1){
                    //match, quantity buy > sell

                    //match price
                    BigDecimal transaction_price = ConvertPriceToUSD(new_order);
                    //change involved orders status
                    order.setStatus("Fulfilled");
                    new_order.setStatus("Fulfilled");
                    orderRepository.save(order);
                    orderRepository.save(new_order);

                    //create transaction record return transaction_id
                    Date current_date = new Date();
                    BigDecimal diff = new BigDecimal(order.getBtc_quantity()).subtract(new BigDecimal(new_order.getBtc_quantity()));
                    OrderTransaction new_record = new OrderTransaction(order.getId(), null, new_order.getId(), null, transaction_price.toString(), current_date, true, "SELL", diff.toString());
                    long transaction_id = orderTransactionService.createOrderTransactionReturnID(new_record);

                    //change involved user balance and create fee_record
                    changeBalance(order, transaction_price, transaction_id);
                    changeBalance(new_order, transaction_price, transaction_id);
                    return "One buy One sell, buy > sell";
                } else if(quantity_match_res == -1){
                    //match, quantity buy < sell

                    //match price
                    BigDecimal transaction_price = ConvertPriceToUSD(new_order);
                    //change involved orders status
                    order.setStatus("Fulfilled");
                    new_order.setStatus("Fulfilled");
                    orderRepository.save(order);
                    orderRepository.save(new_order);
                    //create transaction record return transaction_id
                    Date current_date = new Date();
                    BigDecimal diff = new BigDecimal(new_order.getBtc_quantity()).subtract(new BigDecimal(order.getBtc_quantity()));
                    OrderTransaction new_record = new OrderTransaction(order.getId(), null, new_order.getId(), null, transaction_price.toString(), current_date, true, "BUY", diff.toString());
                    long transaction_id = orderTransactionService.createOrderTransactionReturnID(new_record);
                    //change involved user balance
                    changeBalance(order, transaction_price, transaction_id);
                    changeBalance(new_order, transaction_price, transaction_id);
                    return "One buy One sell, buy < sell";
                } else if(quantity_match_res == 2){
                    //not match, quantity buy >> sell
                    for(int j = 0; j < sell_order.size(); ++i){
                        Orders another_sell = sell_order.get(j);
                        if(ConvertPriceToUSD(another_sell).compareTo(ConvertPriceToUSD(order)) > 0) continue;
                        Integer quantity_match_res_twoSell = quantity_match(new BigDecimal(order.getBtc_quantity()),
                                new BigDecimal(new_order.getBtc_quantity()).add(new BigDecimal(another_sell.getBtc_quantity())));
                        if(quantity_match_res_twoSell == 0) {
                            //match quantity buy == sell1+sell2
                            BigDecimal transaction_price = ConvertPriceToUSD(new_order).max(ConvertPriceToUSD(another_sell));
                            order.setStatus("Fulfilled");
                            another_sell.setStatus("Fulfilled");
                            new_order.setStatus("Fulfilled");
                            orderRepository.save(order);
                            orderRepository.save(another_sell);
                            orderRepository.save(new_order);

                            //create transaction record return transaction_id
                            Date current_date = new Date();
                            OrderTransaction new_record = new OrderTransaction(order.getId(), null, new_order.getId(), another_sell.getId(), transaction_price.toString(), current_date, false, null, null);
                            long transaction_id = orderTransactionService.createOrderTransactionReturnID(new_record);
                            //change involved user balance
                            changeBalance(order, transaction_price, transaction_id);
                            changeBalance(another_sell, transaction_price, transaction_id);
                            changeBalance(new_order, transaction_price, transaction_id);
                            return "buy = sell1+sell2";
                        }else if(quantity_match_res_twoSell == 1){
                            //match quantity buy > sell1+sell2
                            BigDecimal transaction_price = ConvertPriceToUSD(new_order).max(ConvertPriceToUSD(another_sell));
                            order.setStatus("Fulfilled");
                            another_sell.setStatus("Fulfilled");
                            new_order.setStatus("Fulfilled");
                            orderRepository.save(order);
                            orderRepository.save(another_sell);
                            orderRepository.save(new_order);

                            //create transaction record return transaction_id
                            Date current_date = new Date();
                            BigDecimal diff = new BigDecimal(order.getBtc_quantity()).subtract(new BigDecimal(new_order.getBtc_quantity()).add(new BigDecimal(another_sell.getBtc_quantity())));
                            OrderTransaction new_record = new OrderTransaction(order.getId(), null, new_order.getId(), another_sell.getId(), transaction_price.toString(), current_date, true, "SELL", diff.toString());
                            long transaction_id = orderTransactionService.createOrderTransactionReturnID(new_record);
                            //change involved user balance
                            changeBalance(order, transaction_price, transaction_id);
                            changeBalance(another_sell, transaction_price, transaction_id);
                            changeBalance(new_order, transaction_price, transaction_id);
                            return "buy > sell1+sell2";
                        }else if(quantity_match_res_twoSell == -1){
                            //match quantity buy < sell1+sell2
                            BigDecimal transaction_price = ConvertPriceToUSD(new_order).max(ConvertPriceToUSD(another_sell));
                            order.setStatus("Fulfilled");
                            another_sell.setStatus("Fulfilled");
                            new_order.setStatus("Fulfilled");
                            orderRepository.save(order);
                            orderRepository.save(another_sell);
                            orderRepository.save(new_order);

                            //create transaction record return transaction_id
                            Date current_date = new Date();
                            BigDecimal diff = new BigDecimal(new_order.getBtc_quantity()).add(new BigDecimal(another_sell.getBtc_quantity())).subtract(new BigDecimal(order.getBtc_quantity()));
                            OrderTransaction new_record = new OrderTransaction(order.getId(), null, new_order.getId(), another_sell.getId(), transaction_price.toString(), current_date, true, "BUY", diff.toString());
                            long transaction_id = orderTransactionService.createOrderTransactionReturnID(new_record);
                            //change involved user balance
                            changeBalance(order, transaction_price, transaction_id);
                            changeBalance(another_sell, transaction_price, transaction_id);
                            changeBalance(new_order, transaction_price, transaction_id);
                            return "buy < sell1+sell2";
                        }
                    }
                } else if(quantity_match_res == -2){
                    //not match, quantity buy << sell
                    for (int j = i + 1; j < filtered_buy_order.size(); ++j) {
                        Orders another_buy = filtered_buy_order.get(j);
                        Integer quantity_match_res_twoBuy = quantity_match(new BigDecimal(order.getBtc_quantity()).add(new BigDecimal(another_buy.getBtc_quantity())),
                                new BigDecimal(new_order.getBtc_quantity()));
                        if (quantity_match_res_twoBuy == 0) {
                            //match quantity buy1+buy2 = sell
                            BigDecimal transaction_price = ConvertPriceToUSD(new_order);
                            order.setStatus("Fulfilled");
                            another_buy.setStatus("Fulfilled");
                            new_order.setStatus("Fulfilled");
                            orderRepository.save(order);
                            orderRepository.save(another_buy);
                            orderRepository.save(new_order);
                            //create transaction record return transaction_id
                            Date current_date = new Date();
                            OrderTransaction new_record = new OrderTransaction(order.getId(), another_buy.getId(), new_order.getId(), null, transaction_price.toString(), current_date, false, null, null);
                            long transaction_id = orderTransactionService.createOrderTransactionReturnID(new_record);
                            //change involved user balance
                            changeBalance(order, transaction_price, transaction_id);
                            changeBalance(another_buy, transaction_price, transaction_id);
                            changeBalance(new_order, transaction_price, transaction_id);
                            return "buy1+buy2 = sell";
                        } else if(quantity_match_res_twoBuy == 1) {
                            //match quantity buy1+buy2 > sell
                            BigDecimal transaction_price = ConvertPriceToUSD(new_order);
                            order.setStatus("Fulfilled");
                            another_buy.setStatus("Fulfilled");
                            new_order.setStatus("Fulfilled");
                            orderRepository.save(order);
                            orderRepository.save(another_buy);
                            orderRepository.save(new_order);
                            //create transaction record return transaction_id
                            Date current_date = new Date();
                            BigDecimal diff = new BigDecimal(order.getBtc_quantity()).add(new BigDecimal(another_buy.getBtc_quantity())).subtract(new BigDecimal(new_order.getBtc_quantity()));
                            OrderTransaction new_record = new OrderTransaction(order.getId(), another_buy.getId(), new_order.getId(), null, transaction_price.toString(), current_date, true, "SELL", diff.toString());
                            long transaction_id = orderTransactionService.createOrderTransactionReturnID(new_record);
                            //change involved user balance
                            changeBalance(order, transaction_price, transaction_id);
                            changeBalance(another_buy, transaction_price, transaction_id);
                            changeBalance(new_order, transaction_price, transaction_id);
                            return "buy1+buy2 > sell";
                        } else if(quantity_match_res_twoBuy == -1) {
                            //match quantity buy1+buy2 < sell
                            BigDecimal transaction_price = ConvertPriceToUSD(new_order);
                            order.setStatus("Fulfilled");
                            another_buy.setStatus("Fulfilled");
                            new_order.setStatus("Fulfilled");
                            orderRepository.save(order);
                            orderRepository.save(another_buy);
                            orderRepository.save(new_order);

                            //create transaction record return transaction_id
                            Date current_date = new Date();
                            BigDecimal diff = new BigDecimal(new_order.getBtc_quantity()).subtract(new BigDecimal(order.getBtc_quantity()).add(new BigDecimal(another_buy.getBtc_quantity())));
                            OrderTransaction new_record = new OrderTransaction(order.getId(), another_buy.getId(), new_order.getId(), null, transaction_price.toString(), current_date, true, "BUY", diff.toString());
                            long transaction_id = orderTransactionService.createOrderTransactionReturnID(new_record);
                            //change involved user balance
                            changeBalance(order, transaction_price, transaction_id);
                            changeBalance(another_buy, transaction_price, transaction_id);
                            changeBalance(new_order, transaction_price, transaction_id);
                            return "buy1+buy2 < sell";
                        } else return "not match";
                    }
                } else {
                    //match buy == sell

                    //match price
                    BigDecimal transaction_price = ConvertPriceToUSD(new_order);
                    //change involved orders status
                    order.setStatus("Fulfilled");
                    new_order.setStatus("Fulfilled");
                    orderRepository.save(order);
                    orderRepository.save(new_order);
                    //create transaction record return transaction_id
                    Date current_date = new Date();
                    OrderTransaction new_record = new OrderTransaction(order.getId(), null, new_order.getId(), null, transaction_price.toString(), current_date, false, null, null);
                    long transaction_id = orderTransactionService.createOrderTransactionReturnID(new_record);

                    //change involved user balance
                    changeBalance(order, transaction_price, transaction_id);
                    changeBalance(new_order, transaction_price, transaction_id);

                    return "One buy One sell, buy = sell";
                }
            }
            return "not match";
        } else if("BUY".equals(new_order_type)){
            List<Orders> filtered_sell_order = sell_order.stream()
                    .filter(o -> !new_order.getUsername().equals(o.getUsername()))
                    .filter(o -> ConvertPriceToUSD(o).compareTo(ConvertPriceToUSD(new_order)) <= 0)
                    .collect(toList());
            for(Orders order: filtered_sell_order){
                System.out.println("id: " + order.getId() + " time: " + order.getStart_date());
            }

            for(int i = 0; i < filtered_sell_order.size(); ++i){
                Orders order = filtered_sell_order.get(i);
                Integer quantity_match_res = quantity_match(new BigDecimal(new_order.getBtc_quantity()), new BigDecimal(order.getBtc_quantity()));
                if(quantity_match_res == 1){
                    //match, quantity buy > sell

                    //match price
                    BigDecimal transaction_price = ConvertPriceToUSD(order);
                    //change involved orders status
                    order.setStatus("Fulfilled");
                    new_order.setStatus("Fulfilled");
                    orderRepository.save(order);
                    orderRepository.save(new_order);

                    //create transaction record return transaction_id
                    Date current_date = new Date();
                    BigDecimal diff = new BigDecimal(new_order.getBtc_quantity()).subtract(new BigDecimal(order.getBtc_quantity()));
                    OrderTransaction new_record = new OrderTransaction(new_order.getId(), null, order.getId(), null, transaction_price.toString(), current_date, true, "SELL", diff.toString());
                    long transaction_id = orderTransactionService.createOrderTransactionReturnID(new_record);

                    //change involved user balance and create fee_record
                    changeBalance(order, transaction_price, transaction_id);
                    changeBalance(new_order, transaction_price, transaction_id);
                    return "One buy One sell, buy > sell";
                } else if(quantity_match_res == -1){
                    //match, quantity buy < sell

                    //match price
                    BigDecimal transaction_price = ConvertPriceToUSD(order);
                    //change involved orders status
                    order.setStatus("Fulfilled");
                    new_order.setStatus("Fulfilled");
                    orderRepository.save(order);
                    orderRepository.save(new_order);
                    //create transaction record return transaction_id
                    Date current_date = new Date();
                    BigDecimal diff = new BigDecimal(order.getBtc_quantity()).subtract(new BigDecimal(new_order.getBtc_quantity()));
                    OrderTransaction new_record = new OrderTransaction(new_order.getId(), null, order.getId(), null, transaction_price.toString(), current_date, true, "BUY", diff.toString());
                    long transaction_id = orderTransactionService.createOrderTransactionReturnID(new_record);
                    //change involved user balance
                    changeBalance(order, transaction_price, transaction_id);
                    changeBalance(new_order, transaction_price, transaction_id);
                    return "One buy One sell, buy < sell";
                } else if(quantity_match_res == 2){
                    //not match, quantity buy >> sell
                    for(int j = i+1; j < filtered_sell_order.size(); ++i){
                        Orders another_sell = filtered_sell_order.get(j);
                        Integer quantity_match_res_twoSell = quantity_match(new BigDecimal(new_order.getBtc_quantity()),
                                new BigDecimal(order.getBtc_quantity()).add(new BigDecimal(another_sell.getBtc_quantity())));
                        if(quantity_match_res_twoSell == 0) {
                            //match quantity buy == sell1+sell2
                            BigDecimal transaction_price = ConvertPriceToUSD(order).max(ConvertPriceToUSD(another_sell));
                            order.setStatus("Fulfilled");
                            another_sell.setStatus("Fulfilled");
                            new_order.setStatus("Fulfilled");
                            orderRepository.save(order);
                            orderRepository.save(another_sell);
                            orderRepository.save(new_order);

                            //create transaction record return transaction_id
                            Date current_date = new Date();
                            OrderTransaction new_record = new OrderTransaction(new_order.getId(), null, order.getId(), another_sell.getId(), transaction_price.toString(), current_date, false, null, null);
                            long transaction_id = orderTransactionService.createOrderTransactionReturnID(new_record);
                            //change involved user balance
                            changeBalance(order, transaction_price, transaction_id);
                            changeBalance(another_sell, transaction_price, transaction_id);
                            changeBalance(new_order, transaction_price, transaction_id);
                            return "buy = sell1+sell2";
                        }else if(quantity_match_res_twoSell == 1){
                            //match quantity buy > sell1+sell2
                            BigDecimal transaction_price = ConvertPriceToUSD(order).max(ConvertPriceToUSD(another_sell));
                            order.setStatus("Fulfilled");
                            another_sell.setStatus("Fulfilled");
                            new_order.setStatus("Fulfilled");
                            orderRepository.save(order);
                            orderRepository.save(another_sell);
                            orderRepository.save(new_order);

                            //create transaction record return transaction_id
                            Date current_date = new Date();
                            BigDecimal diff = new BigDecimal(new_order.getBtc_quantity()).subtract(new BigDecimal(order.getBtc_quantity()).add(new BigDecimal(another_sell.getBtc_quantity())));
                            OrderTransaction new_record = new OrderTransaction(new_order.getId(), null, order.getId(), another_sell.getId(), transaction_price.toString(), current_date, true, "SELL", diff.toString());
                            long transaction_id = orderTransactionService.createOrderTransactionReturnID(new_record);
                            //change involved user balance
                            changeBalance(order, transaction_price, transaction_id);
                            changeBalance(another_sell, transaction_price, transaction_id);
                            changeBalance(new_order, transaction_price, transaction_id);
                            return "buy > sell1+sell2";
                        }else if(quantity_match_res_twoSell == -1){
                            //match quantity buy < sell1+sell2
                            BigDecimal transaction_price = ConvertPriceToUSD(order).max(ConvertPriceToUSD(another_sell));
                            order.setStatus("Fulfilled");
                            another_sell.setStatus("Fulfilled");
                            new_order.setStatus("Fulfilled");
                            orderRepository.save(order);
                            orderRepository.save(another_sell);
                            orderRepository.save(new_order);

                            //create transaction record return transaction_id
                            Date current_date = new Date();
                            BigDecimal diff = new BigDecimal(order.getBtc_quantity()).add(new BigDecimal(another_sell.getBtc_quantity())).subtract(new BigDecimal(new_order.getBtc_quantity()));
                            OrderTransaction new_record = new OrderTransaction(new_order.getId(), null, order.getId(), another_sell.getId(), transaction_price.toString(), current_date, true, "BUY", diff.toString());
                            long transaction_id = orderTransactionService.createOrderTransactionReturnID(new_record);
                            //change involved user balance
                            changeBalance(order, transaction_price, transaction_id);
                            changeBalance(another_sell, transaction_price, transaction_id);
                            changeBalance(new_order, transaction_price, transaction_id);
                            return "buy < sell1+sell2";
                        }else return "not match";
                    }
                } else if(quantity_match_res == -2){
                    //not match, quantity buy << sell
                    for (Orders another_buy : buy_order) {
                        if(ConvertPriceToUSD(another_buy).compareTo(ConvertPriceToUSD(order)) < 0) continue;
                        Integer quantity_match_res_twoBuy = quantity_match(new BigDecimal(new_order.getBtc_quantity()).add(new BigDecimal(another_buy.getBtc_quantity())),
                                new BigDecimal(order.getBtc_quantity()));
                        if (quantity_match_res_twoBuy == 0) {
                            //match quantity buy1+buy2 = sell
                            BigDecimal transaction_price = ConvertPriceToUSD(order);
                            order.setStatus("Fulfilled");
                            another_buy.setStatus("Fulfilled");
                            new_order.setStatus("Fulfilled");
                            orderRepository.save(order);
                            orderRepository.save(another_buy);
                            orderRepository.save(new_order);
                            //create transaction record return transaction_id
                            Date current_date = new Date();
                            OrderTransaction new_record = new OrderTransaction(new_order.getId(), another_buy.getId(), order.getId(), null, transaction_price.toString(), current_date, false, null, null);
                            long transaction_id = orderTransactionService.createOrderTransactionReturnID(new_record);
                            //change involved user balance
                            changeBalance(order, transaction_price, transaction_id);
                            changeBalance(another_buy, transaction_price, transaction_id);
                            changeBalance(new_order, transaction_price, transaction_id);
                            return "buy1+buy2 = sell";
                        } else if(quantity_match_res_twoBuy == 1) {
                            //match quantity buy1+buy2 > sell
                            BigDecimal transaction_price = ConvertPriceToUSD(order);
                            order.setStatus("Fulfilled");
                            another_buy.setStatus("Fulfilled");
                            new_order.setStatus("Fulfilled");
                            orderRepository.save(order);
                            orderRepository.save(another_buy);
                            orderRepository.save(new_order);
                            //create transaction record return transaction_id
                            Date current_date = new Date();
                            BigDecimal diff = new BigDecimal(new_order.getBtc_quantity()).add(new BigDecimal(another_buy.getBtc_quantity())).subtract(new BigDecimal(order.getBtc_quantity()));
                            OrderTransaction new_record = new OrderTransaction(new_order.getId(), another_buy.getId(), order.getId(), null, transaction_price.toString(), current_date, true, "SELL", diff.toString());
                            long transaction_id = orderTransactionService.createOrderTransactionReturnID(new_record);
                            //change involved user balance
                            changeBalance(order, transaction_price, transaction_id);
                            changeBalance(another_buy, transaction_price, transaction_id);
                            changeBalance(new_order, transaction_price, transaction_id);
                            return "buy1+buy2 > sell";
                        } else if(quantity_match_res_twoBuy == -1) {
                            //match quantity buy1+buy2 < sell
                            BigDecimal transaction_price = ConvertPriceToUSD(order);
                            order.setStatus("Fulfilled");
                            another_buy.setStatus("Fulfilled");
                            new_order.setStatus("Fulfilled");
                            orderRepository.save(order);
                            orderRepository.save(another_buy);
                            orderRepository.save(new_order);

                            //create transaction record return transaction_id
                            Date current_date = new Date();
                            BigDecimal diff = new BigDecimal(order.getBtc_quantity()).subtract(new BigDecimal(new_order.getBtc_quantity()).add(new BigDecimal(another_buy.getBtc_quantity())));
                            OrderTransaction new_record = new OrderTransaction(new_order.getId(), another_buy.getId(), order.getId(), null, transaction_price.toString(), current_date, true, "BUY", diff.toString());
                            long transaction_id = orderTransactionService.createOrderTransactionReturnID(new_record);
                            //change involved user balance
                            changeBalance(order, transaction_price, transaction_id);
                            changeBalance(another_buy, transaction_price, transaction_id);
                            changeBalance(new_order, transaction_price, transaction_id);
                            return "buy1+buy2 < sell";
                        } else return "not match";
                    }
                } else {
                    //match buy == sell

                    //match price
                    BigDecimal transaction_price = ConvertPriceToUSD(order);
                    //change involved orders status
                    order.setStatus("Fulfilled");
                    new_order.setStatus("Fulfilled");
                    orderRepository.save(order);
                    orderRepository.save(new_order);
                    //create transaction record return transaction_id
                    Date current_date = new Date();
                    OrderTransaction new_record = new OrderTransaction(new_order.getId(), null, order.getId(), null, transaction_price.toString(), current_date, false, null, null);
                    long transaction_id = orderTransactionService.createOrderTransactionReturnID(new_record);

                    //change involved user balance
                    changeBalance(order, transaction_price, transaction_id);
                    changeBalance(new_order, transaction_price, transaction_id);

                    return "One buy One sell, buy = sell";
                }
            }
            return "not match";
        }
        return "bad new_order type";
    }

    @Override
    public List<Orders> getOpeningOrdersByUsername(String username) {
        return orderRepository.findOrderByUsernameAndStatus(username, "Open");
    }
}
