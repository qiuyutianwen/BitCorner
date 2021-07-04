package com.bitcorner.bitCorner.controllers;

import com.bitcorner.bitCorner.exception.DataNotFoundException;
import com.bitcorner.bitCorner.models.OrderTransaction;
import com.bitcorner.bitCorner.models.Orders;
import com.bitcorner.bitCorner.services.OrderService;

import com.bitcorner.bitCorner.services.OrderTransactionService;
import com.bitcorner.bitCorner.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController
@CrossOrigin
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final OrderTransactionService orderTransactionService;

    public OrderController(OrderService orderService, UserService userService,
            OrderTransactionService orderTransactionService) {
        this.orderService = orderService;
        this.userService = userService;
        this.orderTransactionService = orderTransactionService;
    }

    /**
     * (1) Create an Order
     *
     * Path:
     * /order?username=XX&type=ZZ&currency=UU&price=VV&btc_quantity=WW&start_date=WW&status=BB&format={json
     * | xml }
     *
     * Method: POST
     *
     * Description: This API creates an order object.
     */
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public ResponseEntity<?> createOrder(@RequestParam String username, @RequestParam String type,
            @RequestParam String transaction_type, @RequestParam String currency,
            @RequestParam(required = false) String price, @RequestParam String btc_quantity,
            @RequestParam(required = false) String format) {
        // check input
        if (username == null || username.isEmpty() || type == null || type.isEmpty() || transaction_type == null
                || transaction_type.isEmpty() || currency == null || currency.isEmpty() || btc_quantity == null
                || btc_quantity.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        /*
         * 1. check if user is in the database!!!!!!!! 2. if order type is sell, check
         * btc balance else if order type is buy, check user currency balance
         */

        // set mediatype
        MediaType mediaType = MediaType.APPLICATION_JSON;
        if (Objects.nonNull(format) && format.equalsIgnoreCase("xml")) {
            mediaType = MediaType.APPLICATION_XML;
        }

        if (new BigDecimal(btc_quantity).compareTo(new BigDecimal("0")) <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("btc quantity must be greater than zero");
        }

        try {
            userService.findByUsername(username);
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getDetails());
        }

        if (!"Market Order".equalsIgnoreCase(transaction_type) && !"Limit Order".equalsIgnoreCase(transaction_type)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("transaction_type invalid");
        }
        if ("Limit Order".equalsIgnoreCase(transaction_type)) {
            if (price == null || price.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("limit order must provide price");
            }
            if (new BigDecimal(price).compareTo(new BigDecimal("0")) <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("limit order's price must be greater than zero");
            }
        }
        if (!"EUR".equalsIgnoreCase(currency) && !"GBP".equalsIgnoreCase(currency) && !"USD".equalsIgnoreCase(currency)
                && !"CNY".equalsIgnoreCase(currency) && !"INR".equalsIgnoreCase(currency)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("currency type is not supported");
        }

        if (type.equalsIgnoreCase("BUY")) {
            List<Orders> opening_orders = orderService.getOpeningOrdersByUsername(username);
            List<Orders> opening_orders_in_this_currency = opening_orders.stream()
                    .filter(o -> o.getType().equalsIgnoreCase("BUY"))
                    .filter(o -> o.getCurrency().equalsIgnoreCase(currency)).collect(toList());
            BigDecimal total_used_money_in_this_currency = new BigDecimal("0");
            for (Orders order : opening_orders_in_this_currency) {
                total_used_money_in_this_currency = total_used_money_in_this_currency
                        .add(new BigDecimal(order.getPrice()).multiply(new BigDecimal(order.getBtc_quantity())));
            }
            total_used_money_in_this_currency = total_used_money_in_this_currency
                    .add(new BigDecimal(price).multiply(new BigDecimal(btc_quantity)));
            if (total_used_money_in_this_currency
                    .compareTo(BigDecimal.valueOf(userService.getBalance(currency, username))) > 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("you don't have enough money of " + currency);
            }
        } else if (type.equalsIgnoreCase("SELL")) {
            List<Orders> opening_orders = orderService.getOpeningOrdersByUsername(username);
            List<Orders> opening_sell_orders = opening_orders.stream().filter(o -> o.getType().equalsIgnoreCase("SELL"))
                    .collect(toList());
            BigDecimal total_selling_btc_quantity = new BigDecimal("0");
            for (Orders order : opening_sell_orders) {
                total_selling_btc_quantity = total_selling_btc_quantity.add(new BigDecimal(order.getBtc_quantity()));
            }
            total_selling_btc_quantity = total_selling_btc_quantity.add(new BigDecimal(btc_quantity));
            if (total_selling_btc_quantity.compareTo(BigDecimal.valueOf(userService.getBalance("btc", username))) > 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("you don't have enough btc");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad new_order type");
        }
        Date current_date = new Date();
        Orders new_order = new Orders(type, username, transaction_type, currency, price, btc_quantity, current_date,
                "Open");
        //= String.format(
        //                "Hello %s,\n\nPlease click the link below to verify your registration:\n%s\n\nThank you,\nTeam BitCorner",
        //                user.getNickname(),
        //                siteURL + "/verify?token=" + user.getToken()
        //        );
        String nickname = userService.findByUsername(username).getNickname();
        String msg = String.format("Hello %s, \n\nYour Order has been created!\n\nThank you,\nTeam BitCorner", nickname);
        orderService.sendEmail(username, "Order created", msg);
        // match orders
        String match_res = orderService.matchOrders(new_order);
        System.out.println("================");
        System.out.println(match_res);
        if (match_res.equals("not match")) {
            Map<String, Object> response = orderService.convertOrderToMap(orderService.createOrder(new_order));
            return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(response);
        }
        Map<String, Object> response = orderService.convertOrderToMap(new_order);
        return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(response);
    }

    /**
     * (2) Get Orders
     *
     * Path: /order?username=XX&format={json | xml }
     *
     * Method: GET
     *
     * Description: This API gets orders by username
     */
    @GetMapping(value = "/order")
    public ResponseEntity<?> fetchOrder(@RequestParam String username, @RequestParam(required = false) String format) {
        MediaType mediaType = MediaType.APPLICATION_JSON;
        if (Objects.nonNull(format) && format.equalsIgnoreCase("xml")) {
            mediaType = MediaType.APPLICATION_XML;
        }

        List<Orders> orders_username = orderService.getOrdersByUsername(username);
        List<Map<String, Object>> response = orderService.convertOrderListToMap(orders_username);
        return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(response);
    }

    /**
     * (3) Get ALL Orders
     *
     * Path: order?format={json | xml }
     *
     * Method: GET
     *
     * Description: This API gets orders by username
     */
    @GetMapping(value = "/order/all")
    public ResponseEntity<?> fetchAllOrder(@RequestParam(required = false) String format) {
        MediaType mediaType = MediaType.APPLICATION_JSON;
        if (Objects.nonNull(format) && format.equalsIgnoreCase("xml")) {
            mediaType = MediaType.APPLICATION_XML;
        }

        List<Orders> orders_username = orderService.getAllOrders();
        List<Map<String, Object>> response = orderService.convertOrderListToMap(orders_username);
        return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(response);
    }

    /**
     * (4) Put multipart file, form data
     *
     * Path: /accounts/updateOrder?format={json | xml }
     *
     * Method: PUT, consumes = {MediaType.APPLICATION_JSON}
     *
     * Description: This API tests multiple type form data to update orders
     */
    @PutMapping(value = "/accounts/updateOrder")
    public ResponseEntity<?> testUpload(@RequestBody Orders order, @RequestParam(required = false) String format) {
        MediaType mediaType = MediaType.APPLICATION_JSON;
        if (Objects.nonNull(format) && format.equalsIgnoreCase("xml")) {
            mediaType = MediaType.APPLICATION_XML;
        }
        Date current_date = new Date();
        order.setStart_date(current_date);

        String username = order.getUsername();
        String type = order.getType();
        String currency = order.getCurrency();
        String price = order.getPrice();
        String btc_quantity = order.getBtc_quantity();

        if (order.getStatus().equalsIgnoreCase("Open")) {
            if (type.equalsIgnoreCase("BUY")) {
                List<Orders> opening_orders = orderService.getOpeningOrdersByUsername(username);
                List<Orders> opening_orders_in_this_currency = opening_orders.stream()
                        .filter(o -> o.getType().equalsIgnoreCase("BUY"))
                        .filter(o -> o.getCurrency().equalsIgnoreCase(currency)).collect(toList());
                BigDecimal total_used_money_in_this_currency = new BigDecimal("0");
                for (Orders o : opening_orders_in_this_currency) {
                    total_used_money_in_this_currency = total_used_money_in_this_currency
                            .add(new BigDecimal(o.getPrice()).multiply(new BigDecimal(o.getBtc_quantity())));
                }
                total_used_money_in_this_currency = total_used_money_in_this_currency
                        .add(new BigDecimal(price).multiply(new BigDecimal(btc_quantity)));
                if (total_used_money_in_this_currency
                        .compareTo(BigDecimal.valueOf(userService.getBalance(currency, username))) > 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("you don't have enough money of " + currency);
                }
            } else if (type.equalsIgnoreCase("SELL")) {
                List<Orders> opening_orders = orderService.getOpeningOrdersByUsername(username);
                List<Orders> opening_sell_orders = opening_orders.stream()
                        .filter(o -> o.getType().equalsIgnoreCase("SELL")).collect(toList());
                BigDecimal total_selling_btc_quantity = new BigDecimal("0");
                for (Orders o : opening_sell_orders) {
                    total_selling_btc_quantity = total_selling_btc_quantity.add(new BigDecimal(o.getBtc_quantity()));
                }
                total_selling_btc_quantity = total_selling_btc_quantity.add(new BigDecimal(btc_quantity));
                if (total_selling_btc_quantity
                        .compareTo(BigDecimal.valueOf(userService.getBalance("btc", username))) > 0) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("you don't have enough btc");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad new_order type");
            }
            String nickname = userService.findByUsername(username).getNickname();
            String msg = String.format("Hello %s, \n\nYour Order has been created!\n\nThank you,\nTeam BitCorner", nickname);
            orderService.sendEmail(username, "Order created", msg);
            String match_res = orderService.matchOrders(order);
            System.out.println("================");
            System.out.println(match_res);
            if (match_res.equals("not match")) {
                Map<String, Object> response = orderService.convertOrderToMap(orderService.createOrder(order));
                return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(response);
            }
            Map<String, Object> response = orderService.convertOrderToMap(order);
            return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(response);
        }
        Map<String, Object> response = orderService.convertOrderToMap(orderService.createOrder(order));
        return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(response);
    }

    /**
     * (5) Get transaction record
     *
     * Path: /order/transaction?format={json | xml }
     *
     * Method: GET
     *
     * Description: This API gets all transaction records
     */
    @GetMapping(value = "/order/transaction")
    public ResponseEntity<?> fetchAllOrderTransaction(@RequestParam(required = false) String format) {
        MediaType mediaType = MediaType.APPLICATION_JSON;
        if (Objects.nonNull(format) && format.equalsIgnoreCase("xml")) {
            mediaType = MediaType.APPLICATION_XML;
        }

        List<OrderTransaction> orderTransactions = orderTransactionService.getAllRecords();
        List<Map<String, Object>> response = orderService.convertOrderTransactionListToMap(orderTransactions);
        return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(response);
    }

    /**
     * (5) Get three prices LAP, LBP, LTP
     *
     * Path: /allprice?format={json | xml }
     *
     * Method: GET
     *
     * Description: This API gets all transaction records
     */
    @GetMapping(value = "/allprice")
    public ResponseEntity<?> fetchAllPrice(@RequestParam(required = false) String format) {
        MediaType mediaType = MediaType.APPLICATION_JSON;
        if (Objects.nonNull(format) && format.equalsIgnoreCase("xml")) {
            mediaType = MediaType.APPLICATION_XML;
        }
        Map<String, Object> response = new LinkedHashMap<>();
        OrderTransaction latestOrderTransaction = orderTransactionService.getLatestOrderTransaction();
        if(latestOrderTransaction == null){
            response.put("LTP", "50000.00");
        }else{
            response.put("LTP", latestOrderTransaction.getTransaction_price());
        }
        Orders latestNotCancelledBuyOrder = orderService.getLatestNotCancelledBuyOrder();
        if(latestNotCancelledBuyOrder == null){
            Map<String, Object> LBP = new LinkedHashMap<>();
            LBP.put("currency", "USD");
            LBP.put("price", "50000.00");
            response.put("LBP", LBP);
        }else{
            Map<String, Object> LBP = new LinkedHashMap<>();
            LBP.put("currency", latestNotCancelledBuyOrder.getCurrency());
            LBP.put("price", latestNotCancelledBuyOrder.getPrice());
            response.put("LBP", LBP);
        }
        Orders latestNotCancelledSellOrder = orderService.getLatestNotCancelledSellOrder();
        if(latestNotCancelledSellOrder == null){
            Map<String, Object> LAP = new LinkedHashMap<>();
            LAP.put("currency", "USD");
            LAP.put("price", "50000.00");
            response.put("LAP", LAP);
        }else{
            Map<String, Object> LAP = new LinkedHashMap<>();
            LAP.put("currency", latestNotCancelledSellOrder.getCurrency());
            LAP.put("price", latestNotCancelledSellOrder.getPrice());
            response.put("LAP", LAP);
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(response);
    }
}
