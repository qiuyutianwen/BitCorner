package com.bitcorner.bitCorner.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@Entity
public class BillTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "SENDER_ID", nullable = false)
    private long sender_id;

    @Column(name = "SENDER_EMAIL", nullable = false)
    private String sender_email;

    @Column(name = "SENDER_NICKNAME", nullable = false)
    private String sender_nickname;

    @Column(name = "RECEIVER_ID", nullable = false)
    private long receiver_id;

    @Column(name = "RECEIVER_EMAIL", nullable = false)
    private String receiver_email;

    @Column(name = "RECEIVER_NICKNAME", nullable = false)
    private String receiver_nickname;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "CURRENCY", nullable = false)
    private String currency;

    @Column(name = "AMOUNT", nullable = false)
    private String amount;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "START_DATE", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date start_date;

    @Column(name = "TRANSACTION_FEE", nullable = false)
    private double transaction_fee;

    @Column(name = "TRANSACTION_FEE_CURRENCY_TYPE", nullable = false)
    private String transaction_fee_currency_type;

    public BillTransaction(long sender_id,String sender_email, String sender_nickname, long receiver_id, String receiver_email, String receiver_nickname, String status, String currency, String amount, String description, java.util.Date start_date, double transaction_fee, String transaction_fee_currency_type) {
        this.sender_id = sender_id;
        this.sender_email = sender_email;
        this.sender_nickname = sender_nickname;
        this.receiver_id = receiver_id;
        this.receiver_email = receiver_email;
        this.receiver_nickname = receiver_nickname;
        this.status = status;
        this.currency = currency;
        this.amount = amount;
        this.description = description;
        this.start_date = start_date;
        this.transaction_fee = transaction_fee;
        this.transaction_fee_currency_type = transaction_fee_currency_type;
    }

    public BillTransaction() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSender_id() {
        return sender_id;
    }

    public void setSender_id(long sender_id) {
        this.sender_id = sender_id;
    }

    public long getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(long receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public double getTransaction_fee() {
        return transaction_fee;
    }

    public void setTransaction_fee(double transaction_fee) {
        this.transaction_fee = transaction_fee;
    }

    public String getTransaction_fee_currency_type() {
        return transaction_fee_currency_type;
    }

    public void setTransaction_fee_currency_type(String transaction_fee_currency_type) { this.transaction_fee_currency_type = transaction_fee_currency_type; }

    public String getSender_email() { return sender_email; }

    public void setSender_email(String sender_email) { this.sender_email = sender_email; }

    public String getReceiver_email() { return receiver_email; }

    public void setReceiver_email(String receiver_email) { this.receiver_email = receiver_email; }

    public String getSender_nickname() { return sender_nickname; }

    public void setSender_nickname(String sender_nickname) { this.sender_nickname = sender_nickname; }

    public String getReceiver_nickname() { return receiver_nickname; }

    public void setReceiver_nickname(String receiver_nickname) {this.receiver_nickname = receiver_nickname; }
}
