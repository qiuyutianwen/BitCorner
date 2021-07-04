package com.bitcorner.bitCorner.services;

import com.bitcorner.bitCorner.dto.BalancePatchDTO;
import com.bitcorner.bitCorner.exception.UnauthorizedOperationException;
import com.bitcorner.bitCorner.models.Bill;
import com.bitcorner.bitCorner.models.BillTransaction;
import com.bitcorner.bitCorner.models.User;
import com.bitcorner.bitCorner.repositories.BillRepository;
import com.bitcorner.bitCorner.repositories.UserRepository;
import com.bitcorner.bitCorner.repositories.BillTransactionRepository;
import org.springframework.stereotype.Service;
import com.bitcorner.bitCorner.utility.EmailSender;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BillServiceImp implements BillService{
    private final BillRepository billRepository;
    private final BillTransactionRepository billTransactionRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final EmailSender emailSender;

    private final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public BillServiceImp(BillRepository billRepository, BillTransactionRepository billTransactionRepository, UserRepository userRepository, UserService userService, EmailSender emailSender) {
        this.billRepository = billRepository;
        this.billTransactionRepository = billTransactionRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.emailSender = emailSender;
    }

    @Override
    public void sendEmail(String email, String title, String msg) {
        emailSender.sendEmail(email, title, msg);
    }

    @Override
    public Bill createBill(Bill bill) {
        User receiver = userService.getUserById(bill.getReceiver_id());
        String nickname = receiver.getNickname();
        String msg = String.format("Hello %s, \n\nYou receive a new bill, please pay the bill before due date!\n\nThank you,\nTeam BitCorner", nickname);
        sendEmail(receiver.getUsername(), "New Bill Received", msg);

        User sender = userService.getUserById(bill.getSender_id());
        String nicknameS = sender.getNickname();
        String msgS = String.format("Hello %s, \n\nYou created a new bill and sent it to the receiver!\n\nThank you,\nTeam BitCorner", nicknameS);
        sendEmail(sender.getUsername(), "New Bill Created", msgS);

        return billRepository.save(bill);
    }

    @Override
    public List<Bill> getBills() {
        return billRepository.findAll();
    }

    @Override
    public List<Bill> getBillsBySenderId(long id) {
        System.out.println("getBillsBySenderId" + id);
        System.out.println("list bill" + billRepository.findBillBySenders(id));
        return billRepository.findBillBySenders(id);
    }

    @Override
    public Bill getBillById(long id) {
        return billRepository.findById(id);
    }

    @Override
    public Map<String, Object> converBillToMap(Bill bill) {
//        System.out.print("convert " + bill.getId() + " " + bill.getSender_id() + " " + bill.getAmount()
//                + " " + bill.getCurrency() + " " + bill.getDescription() + " " + bill.getStart_date() + " "
//                + bill.getDue_date()+ " " + bill.getReceiver_id() + " " + bill.getStatus() + "\n");

        // Overdue
        if(bill.getStatus().equals("Waiting")){
            String ddl = DATE_TIME_FORMAT.format(bill.getDue_date());
            Date now = new Date(System.currentTimeMillis());
            String now_day = DATE_TIME_FORMAT.format(now);
            //System.out.println(ddl);
            //System.out.println(now_day);
            try {
                if(DATE_TIME_FORMAT.parse(ddl).before(DATE_TIME_FORMAT.parse(now_day))){
                    bill.setStatus("Overdue");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", bill.getId());
        map.put("sender_id", bill.getSender_id());
        map.put("amount", bill.getAmount());
        map.put("currency", bill.getCurrency());
        map.put("description", bill.getDescription());
        Date start_date = bill.getStart_date();
        map.put("start_date", start_date);
        Date due_date = bill.getDue_date();
        map.put("due_date", due_date);
        map.put("receiver_id", bill.getReceiver_id());
        map.put("status", bill.getStatus());

        return map;
    }

    @Override
    public List<Map<String, Object>> convertBillListToMap(List<Bill> bills) {
        List<Map<String, Object>> res = new ArrayList<>();
        for(Bill bill : bills){
            res.add(converBillToMap(bill));
        }
        return res;
    }

    @Override
    public void updateBill(Bill bill) {
        User receiver = userService.getUserById(bill.getReceiver_id());
        String nickname = receiver.getNickname();
        String msg = String.format("Hello %s, \n\na bill has been updated, please check for the new update.\n\nThank you,\nTeam BitCorner", nickname);
        sendEmail(receiver.getUsername(), "Bill Updated", msg);

        User sender = userService.getUserById(bill.getSender_id());
        String nicknameS = sender.getNickname();
        String msgS = String.format("Hello %s, \n\nyou have successfully update a bill.\n\nThank you,\nTeam BitCorner", nicknameS);
        sendEmail(sender.getUsername(), "Bill Updated", msgS);
        billRepository.save(bill);
    }

    @Override
    public Bill cancelBill(Bill bill) {
        bill.setStatus("Cancelled");
        billRepository.save(bill);

        User receiver = userService.getUserById(bill.getReceiver_id());
        String nickname = receiver.getNickname();
        String msg = String.format("Hello %s, \n\na bill has been cancelled.\n\nThank you,\nTeam BitCorner", nickname);
        sendEmail(receiver.getUsername(), "Bill Cancelled", msg);

        User sender = userService.getUserById(bill.getSender_id());
        String nicknameS = sender.getNickname();
        String msgS = String.format("Hello %s, \n\nyou have successfully cancelled a bill.\n\nThank you,\nTeam BitCorner", nicknameS);
        sendEmail(sender.getUsername(), "Bill Cancelled", msgS);

        return bill;
    }

    @Override
    public Bill rejectBill(Bill bill) {
       System.out.println("imhere");
        bill.setStatus("Rejected");
        billRepository.save(bill);

        User receiver = userService.getUserById(bill.getReceiver_id());
        String nickname = receiver.getNickname();
        String msg = String.format("Hello %s, \n\nyou have successfully rejected a bill.\n\nThank you,\nTeam BitCorner", nickname);
        sendEmail(receiver.getUsername(), "Bill Rejected", msg);

        User sender = userService.getUserById(bill.getSender_id());
        String nicknameS = sender.getNickname();
        String msgS = String.format("Hello %s, \n\na bill has been rejected, please check for update.\n\nThank you,\nTeam BitCorner", nicknameS);
        sendEmail(sender.getUsername(), "Bill Rejected", msgS);

        return bill;
    }

    @Override
    public Bill payBill(long id, String paidType, Double paidAmount, double transaction_fee) {
        Bill bill = getBillById(id);

        if(bill.getStatus().equals("Paid") || bill.getStatus().equals("Rejected") || bill.getStatus().equals("Cancelled")){
            throw new UnauthorizedOperationException("This bill is not available to pay");
        }

        User sender = userRepository.getOne(bill.getSender_id());
        User receiver = userRepository.getOne(bill.getReceiver_id());
        System.out.println("sender's eamil:" + sender.getUsername());
        System.out.println("sender's eamil:" + receiver.getUsername());

        if(bill.getCurrency().equals("BITCOIN")){
            BalancePatchDTO updateSender = BalancePatchDTO.builder()
                    .username(sender.getUsername())
                    .amount(Double.parseDouble(bill.getAmount()))
                    .currency("btc")
                    .operation("deposit").build();
            userService.updateBalance(updateSender);
        }else{
            BalancePatchDTO updateSender = BalancePatchDTO.builder()
                    .username(sender.getUsername())
                    .amount(Double.parseDouble(bill.getAmount()))
                    .currency(bill.getCurrency())
                    .operation("deposit").build();
            userService.updateBalance(updateSender);
        }

        BalancePatchDTO updateReceiver = BalancePatchDTO.builder()
                .username(receiver.getUsername())
                .amount(paidAmount + transaction_fee)
                .currency(paidType)
                .operation("withdraw").build();

        userService.updateBalance(updateReceiver);
        //System.out.println("updateReceiver: " + updateReceiver);

        System.out.println("Old Status:" +  bill.getStatus());
        bill.setStatus("Paid");
        System.out.println("New Status:" +  bill.getStatus());

        BillTransaction newRecord = new BillTransaction(
                bill.getSender_id(),
                sender.getUsername(),
                sender.getNickname(),
                bill.getReceiver_id(),
                receiver.getUsername(),
                receiver.getNickname(),
                bill.getStatus(),
                bill.getCurrency(),
                bill.getAmount(),
                bill.getDescription(),
                bill.getStart_date(),
                transaction_fee,
                paidType);

        billTransactionRepository.save(newRecord);

        // send eamil
        String msg1 = String.format("Hello %s, \n\na bill has been paid! Please check your balance.\n\nThank you,\nTeam BitCorner", sender.getNickname());
        sendEmail(sender.getUsername(), "Bill Payment Received", msg1);

        String msg2 = String.format("Hello %s, \n\nBill paid success!\n\nThank you for you payment,\nTeam BitCorner", receiver.getNickname());
        sendEmail(receiver.getUsername(), "Bill Paid", msg2);

        return bill;
    }

}