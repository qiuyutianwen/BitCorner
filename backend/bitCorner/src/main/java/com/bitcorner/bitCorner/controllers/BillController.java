package com.bitcorner.bitCorner.controllers;

import com.bitcorner.bitCorner.models.Bill;
import com.bitcorner.bitCorner.models.BillTransaction;
import com.bitcorner.bitCorner.services.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin
public class BillController {
    private BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping(value = "/bill/send")
    public ResponseEntity<?> sendBill(@RequestBody Bill bill, @RequestParam(required = false) String format){
        MediaType mediaType = MediaType.APPLICATION_JSON;
        if (Objects.nonNull(format) && format.equalsIgnoreCase("xml")) {
            mediaType = MediaType.APPLICATION_XML;
        }

        Map<String, Object> response = billService.converBillToMap(billService.createBill(bill));
        return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(response);
    }

    @GetMapping(value = "/bill")
    public ResponseEntity<?> fetchBill(@RequestParam long userid, @RequestParam(required = false) String format) {
        MediaType mediaType = MediaType.APPLICATION_JSON;
        if (Objects.nonNull(format) && format.equalsIgnoreCase("xml")) {
            mediaType = MediaType.APPLICATION_XML;
        }

        List<Bill> bill_ids = billService.getBillsBySenderId(userid);
        System.out.println("here" + bill_ids);
        List<Map<String, Object>> response = billService.convertBillListToMap(bill_ids);
        return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(response);
    }

    @PostMapping(value = "/bill/update")
    public ResponseEntity<?> updateBill(@RequestBody Bill bill, @RequestParam(required = false) String format){
        MediaType mediaType = MediaType.APPLICATION_JSON;
        if (Objects.nonNull(format) && format.equalsIgnoreCase("xml")) {
            mediaType = MediaType.APPLICATION_XML;
        }
        billService.updateBill(bill);
        Map<String, Object> response = billService.converBillToMap(bill);
        return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(response);
    }

    @GetMapping(value = "/bill/id")
    public ResponseEntity<?> fetchBillById(@RequestParam long billId, @RequestParam(required = false) String format) {
        MediaType mediaType = MediaType.APPLICATION_JSON;
        if (Objects.nonNull(format) && format.equalsIgnoreCase("xml")) {
            mediaType = MediaType.APPLICATION_XML;
        }

        Bill bill_id = billService.getBillById(billId);
        Map<String, Object> response = billService.converBillToMap(bill_id);
        return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(response);
    }

    @PostMapping(value = "/bill/pay")
    public ResponseEntity<?> payBill(@RequestParam long billId, @RequestParam String paidType, @RequestParam double paidAmount,@RequestParam double transaction_fee, @RequestParam(required = false) String format){
        MediaType mediaType = MediaType.APPLICATION_JSON;
        if (Objects.nonNull(format) && format.equalsIgnoreCase("xml")) {
            mediaType = MediaType.APPLICATION_XML;
        }

        Bill paidBill = billService.payBill(billId, paidType, paidAmount, transaction_fee);
        Map<String, Object> response = billService.converBillToMap(paidBill);
        return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(response);
    }

    @PostMapping(value = "/bill/cancel")
    public ResponseEntity<?> cancelBill(@RequestBody Bill bill, @RequestParam(required = false) String format){
        MediaType mediaType = MediaType.APPLICATION_JSON;
        if (Objects.nonNull(format) && format.equalsIgnoreCase("xml")) {
            mediaType = MediaType.APPLICATION_XML;
        }
        
        Bill cancelledBill = billService.cancelBill(bill);
        Map<String, Object> response = billService.converBillToMap(cancelledBill);
        return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(response);
    }

    @PostMapping(value = "/bill/reject")
    public ResponseEntity<?> rejectBill(@RequestBody Bill bill, @RequestParam(required = false) String format){
        MediaType mediaType = MediaType.APPLICATION_JSON;
        if (Objects.nonNull(format) && format.equalsIgnoreCase("xml")) {
            mediaType = MediaType.APPLICATION_XML;
        }

        Bill rejectedBill = billService.rejectBill(bill);
        Map<String, Object> response = billService.converBillToMap(rejectedBill);
        return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(response);
    }
}
