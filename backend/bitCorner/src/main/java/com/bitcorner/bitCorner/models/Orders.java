package com.bitcorner.bitCorner.models;

import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "TYPE", nullable = false)
    private String type;

    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name = "TRANSACTION_TYPE", nullable = false)
    private String transaction_type;

    @Column(name = "CURRENCY", nullable = false)
    private String currency;

    @Column(name = "PRICE", nullable = false)
    @NonNull
    private String price;

    @Column(name = "BTC_QUANTITY", nullable = false)
    private String btc_quantity;

    @Column(name = "START_DATE", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date start_date;

    @Column(name = "STATUS", nullable = false)
    private String status;

    public Orders(String type, String username, String transaction_type, String currency, String price, String btc_quantity, java.util.Date start_date, String status){
        this.type = type;
        this.username = username;
        this.transaction_type = transaction_type;
        this.currency = currency;
        this.price = price;
        this.btc_quantity = btc_quantity;
        this.start_date = start_date;
        this.status = status;
    }

    public Orders(long id, String type, String username, String transaction_type, String currency, String price, String btc_quantity, String status) {
        this.id = id;
        this.type = type;
        this.username = username;
        this.transaction_type = transaction_type;
        this.currency = currency;
        this.price = price;
        this.status = status;
        this.btc_quantity = btc_quantity;
    }

    public Orders() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBtc_quantity() {
        return btc_quantity;
    }

    public void setBtc_quantity(String btc_quantity) {
        this.btc_quantity = btc_quantity;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
}
