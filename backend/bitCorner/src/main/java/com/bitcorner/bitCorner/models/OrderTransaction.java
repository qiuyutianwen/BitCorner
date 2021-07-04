package com.bitcorner.bitCorner.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@Entity
public class OrderTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "BUY_ID1", nullable = false)
    private long buy_id1;

    @Column(name = "BUY_ID2")
    private Long buy_id2;

    @Column(name = "SELL_ID1", nullable = false)
    private long sell_id1;

    @Column(name = "SELL_ID2")
    private Long sell_id2;

    @Column(name = "TRANSACTION_PRICE", nullable = false)
    private String transaction_price;

    @Column(name = "TRANSACTION_DATE", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date transaction_date;

    @Column(name = "MM_INVOLVED", nullable = false)
    private boolean mm_involved;

    @Column(name = "INVOLVED_TYPE")
    private String involved_type;

    @Column(name = "INVOLVED_QUANTITY")
    private String involved_quantity;

    public OrderTransaction() {}

    public OrderTransaction(long buy_id1, Long buy_id2, long sell_id1, Long sell_id2, String transaction_price, Date transaction_date, boolean mm_involved, String involved_type, String involved_quantity){
        this.buy_id1 = buy_id1;
        this.buy_id2 = buy_id2;
        this.sell_id1 = sell_id1;
        this.sell_id2 = sell_id2;
        this.transaction_price = transaction_price;
        this.transaction_date = transaction_date;
        this.mm_involved = mm_involved;
        this.involved_type = involved_type;
        this.involved_quantity = involved_quantity;
    }

    public long getBuy_id1() {
        return buy_id1;
    }

    public void setBuy_id1(long buy_id1) {
        this.buy_id1 = buy_id1;
    }

    public Long getBuy_id2() {
        return buy_id2;
    }

    public void setBuy_id2(Long buy_id2) {
        this.buy_id2 = buy_id2;
    }

    public long getSell_id1() {
        return sell_id1;
    }

    public void setSell_id1(long sell_id1) {
        this.sell_id1 = sell_id1;
    }

    public Long getSell_id2() {
        return sell_id2;
    }

    public void setSell_id2(Long sell_id2) {
        this.sell_id2 = sell_id2;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTransaction_price() {
        return transaction_price;
    }

    public void setTransaction_price(String transaction_price) {
        this.transaction_price = transaction_price;
    }

    public Date getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(Date transaction_date) {
        this.transaction_date = transaction_date;
    }

    public boolean isMm_involved() {
        return mm_involved;
    }

    public void setMm_involved(boolean mm_involved) {
        this.mm_involved = mm_involved;
    }

    public String getInvolved_type() {
        return involved_type;
    }

    public void setInvolved_type(String involved_type) {
        this.involved_type = involved_type;
    }

    public String getInvolved_quantity() {
        return involved_quantity;
    }

    public void setInvolved_quantity(String involved_quantity) {
        this.involved_quantity = involved_quantity;
    }
}
