package com.bitcorner.bitCorner.dto;

import com.bitcorner.bitCorner.models.Address;
import com.bitcorner.bitCorner.models.Balance;
import com.bitcorner.bitCorner.models.Bank;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPatchDTO {
    private Long id;
    private String email;
    private Bank bank;
    private Address address;
    private Double bitcoin;

}
