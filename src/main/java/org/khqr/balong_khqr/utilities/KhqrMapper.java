package org.khqr.balong_khqr.utilities;


import kh.gov.nbc.bakong_khqr.model.IndividualInfo;
import kh.gov.nbc.bakong_khqr.model.KHQRCurrency;
import org.khqr.balong_khqr.domain.dto.GenerateIndividualRequest;

public class KhqrMapper {

    public static IndividualInfo toIndividualInfo(GenerateIndividualRequest req) {
        IndividualInfo info = new IndividualInfo();
        String FTB_BAKONG_ACCOUNT_ID = "aclbkhppxxx@aclb";
        info.setBakongAccountId(FTB_BAKONG_ACCOUNT_ID);
        info.setMerchantName(req.getMerchantName());
        info.setMerchantCity(req.getMerchantCity());
        info.setAccountInformation(req.getAccountInformation());
        String ACQUIRING_BANK = "Acleda Bank Plc.";
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

