package com.bitcorner.bitCorner.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Balance {
    private Double usd = 0.0;
    private Double cny= 0.0;
    private Double inr= 0.0;
    private Double eur= 0.0;
    private Double gbp= 0.0;
    private Double btc= 0.0;

    public Double getUsd() {
        return usd;
    }

    public void setUsd(Double usd) {
        this.usd = usd;
    }

    public Double getCny() {
        return cny;
    }

    public void setCny(Double cny) {
        this.cny = cny;
    }

    public Double getInr() {
        return inr;
    }

    public void setInr(Double inr) {
        this.inr = inr;
    }

    public Double getEur() {
        return eur;
    }

    public void setEur(Double eur) {
        this.eur = eur;
    }

    public Double getGbp() {
        return gbp;
    }

    public void setGbp(Double gbp) {
        this.gbp = gbp;
    }

    public Double getBtc() {
        return btc;
    }

    public void setBtc(Double btc) {
        this.btc = btc;
    }
}
