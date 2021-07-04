package com.bitcorner.bitCorner.dto;

import com.bitcorner.bitCorner.models.Address;
import com.bitcorner.bitCorner.models.Bank;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BalancePatchDTO {
    private String username;
    private Double amount;
    private String currency;   // value: usd, cny, inr, eur, gbp, btc
    private String operation;  // value: withdraw, deposit
}