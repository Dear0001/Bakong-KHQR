package org.khqr.balong_khqr.utilities;


import kh.gov.nbc.bakong_khqr.model.IndividualInfo;
import kh.gov.nbc.bakong_khqr.model.KHQRCurrency;
import org.khqr.balong_khqr.domain.dto.GenerateIndividualRequest;
import org.springframework.beans.factory.annotation.Value;

public class KhqrMapper {
    @Value("${khqr.bakong.account.id}")
    private static String FTB_BAKONG_ACCOUNT_ID;

    @Value("${khqr.acquiring.bank}")
    private static String ACQUIRING_BANK;

    public static IndividualInfo toIndividualInfo(GenerateIndividualRequest req) {
        IndividualInfo info = new IndividualInfo();
        info.setBakongAccountId(FTB_BAKONG_ACCOUNT_ID);
        info.setMerchantName(req.getMerchantName());
        info.setMerchantCity(req.getMerchantCity());
        info.setAccountInformation(req.getAccountInformation());
        info.setAcquiringBank(ACQUIRING_BANK);
        info.setAmount(req.getAmount());

        if ("USD".equalsIgnoreCase(req.getCurrency())) {
            info.setCurrency(KHQRCurrency.USD);
        }

        info.setBillNumber(req.getBillNumber());
        info.setMobileNumber(req.getMobileNumber());
        info.setStoreLabel(req.getStoreLabel());
        info.setTerminalLabel(req.getTerminalLabel());
        info.setPurposeOfTransaction(req.getPurposeOfTransaction());

        return info;
    }
}

