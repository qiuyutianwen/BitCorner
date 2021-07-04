package com.bitcorner.bitCorner.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@Entity
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "SENDER_ID", nullable = false)
    private long sender_id;

    @Column(name = "RECEIVER_ID", nullable = false)
    private long receiver_id;

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

    @Column(name = "DUE_DATE", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date due_date;

    public Bill(long id, long sender_id, long receiver_id, String status, String currency, String amount, String description, java.util.Date start_date, java.util.Date due_date) {
        this.id = id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.status = status;
        this.currency = currency;
        this.amount = amount;
        this.description = description;
        this.start_date = start_date;
        this.due_date = due_date;
    }

    public Bill(long sender_id, long receiver_id, String status, String currency, String amount, String description, java.util.Date start_date, java.util.Date due_date) {
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.status = status;
        this.currency = currency;
        this.amount = amount;
        this.description = description;
        this.start_date = start_date;
        this.due_date = due_date;
    }

    public Bill(){
        System.out.println("empty");
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

    public Date getDue_date() {
        return due_date;
    }

    public void setDue_date(Date due_date) {
        this.due_date = due_date;
    }
}
