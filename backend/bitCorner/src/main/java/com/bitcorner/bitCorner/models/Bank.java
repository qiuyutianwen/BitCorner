package com.bitcorner.bitCorner.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;


@Data
@Embeddable
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Bank {

    private String bank; // e.g., 100 Main ST
    private String number;
    private String name;
    private String currency;
    @Embedded
    private Balance balance;
//    private Double bitcoin;

    public Bank(String bank, String number, String name, String currency, Balance balance) {
        this.bank = bank;
        this.number = number;
        this.name = name;
        this.currency = currency;
        this.balance = balance;
//        this.bitcoin = bitcoin;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }
}