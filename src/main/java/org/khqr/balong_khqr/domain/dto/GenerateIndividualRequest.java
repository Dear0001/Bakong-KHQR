package org.khqr.balong_khqr.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateIndividualRequest {

//    private String bakongAccountId;
    private String merchantName;
    private String merchantCity;
    private String accountInformation;
//    private String acquiringBank;
    private Double amount;
    private String currency;
    private String billNumber;
    private String mobileNumber;
    private String storeLabel;
    private String terminalLabel;
    private String purposeOfTransaction;


}

