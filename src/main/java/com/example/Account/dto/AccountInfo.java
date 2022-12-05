package com.example.Account.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountInfo {  //client <=> application    cf)AccountDto: controller <=> service
    private String accountNumber;
    private Long balance;
}
