package com.bitcorner.bitCorner.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@Entity
public class OrderTransactionFee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "TRANSACTION_ID", nullable = false)
    private long transaction_id;

    @Column(name = "CURRENCY", nullable = false)
    private String currency;

    @Column(name = "QUANTITY", nullable = false)
    private String quantity;

    public OrderTransactionFee(){}

    public OrderTransactionFee(long transaction_id, String currency, String quantity){
        this.transaction_id = transaction_id;
        this.currency = currency;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(long transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
