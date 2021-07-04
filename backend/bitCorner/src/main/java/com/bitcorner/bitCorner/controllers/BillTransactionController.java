package com.bitcorner.bitCorner.controllers;

import com.bitcorner.bitCorner.models.BillTransaction;
import com.bitcorner.bitCorner.services.BillTransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin
public class BillTransactionController {
    private BillTransactionService billTransactionService;

    public BillTransactionController(BillTransactionService billTransactionService) {
        this.billTransactionService = billTransactionService;
    }

    @GetMapping(value = "/bill_transaction/all")
    public ResponseEntity<?> fetchAllBillTransaction(@RequestParam(required = false) String format) {
        MediaType mediaType = MediaType.APPLICATION_JSON;
        if (Objects.nonNull(format) && format.equalsIgnoreCase("xml")) {
            mediaType = MediaType.APPLICATION_XML;
        }

        List<BillTransaction> billTransactions_ids = billTransactionService.getBillTransactions();
        List<Map<String, Object>> response = billTransactionService.convertBillTransactionListToMap(billTransactions_ids);
        return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(response);
    }
}
