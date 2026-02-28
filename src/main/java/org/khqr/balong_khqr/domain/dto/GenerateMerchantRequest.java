package org.khqr.balong_khqr.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateMerchantRequest {

    private String bakongAccountId;
    private String merchantId;
    private String merchantName;
    private String merchantCity;
    private String acquiringBank;
    private Double amount;
    private String currency;
    private String billNumber;


}
