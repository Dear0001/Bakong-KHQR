package org.khqr.balong_khqr.service;

import kh.gov.nbc.bakong_khqr.BakongKHQR;
import kh.gov.nbc.bakong_khqr.model.*;
import org.khqr.balong_khqr.domain.dto.GenerateIndividualRequest;
import org.khqr.balong_khqr.domain.dto.GenerateMerchantRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class KhqrService {
    private static final Logger logger = LoggerFactory.getLogger(KhqrService.class);
    private final RestTemplate restTemplate;
    @Value("${bakong.api.base-url}")
    private String baseUrl;

    @Value("${bakong.api.token}")
    private String token;

    @Value("${bakong.api.email}")
    private String bakongEmail;

    @Value("${bakong-account.id}")
    private String FTB_BAKONG_ACCOUNT_ID;

    @Value("${bakong.acquiring-bank}")
    private String ACQUIRING_BANK;

    public KhqrService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public KHQRResponse<KHQRData> generateIndividual(GenerateIndividualRequest request) {
        String url = baseUrl + "/v1/generate_deeplink_by_qr";

        // Renew token before generating deeplink if email configured
        if (bakongEmail != null && !bakongEmail.isEmpty()) {
            try {
                Object renewResp = renewToken(bakongEmail);
                logger.error("Response from renew token: " + renewResp);
                String newToken = extractTokenFromRenewResponse(renewResp);
                if (newToken != null && !newToken.isEmpty()) {
                    this.token = newToken; // update token used by postWithAuth
                }
            } catch (Exception ex) {
                logger.error("Exception occurred:"+ ex);
            }
        }

        // Build request body as a map and include bakongAccountId explicitly
        Map<String, Object> body = new HashMap<>();
        body.put("bakongAccountId", FTB_BAKONG_ACCOUNT_ID);
        body.put("merchantName", request.getMerchantName());
        body.put("merchantCity", request.getMerchantCity());
        body.put("accountInformation", request.getAccountInformation());
        body.put("acquiringBank", ACQUIRING_BANK);
        body.put("amount", request.getAmount());
        body.put("currency", request.getCurrency());
        body.put("billNumber", request.getBillNumber());
        body.put("mobileNumber", request.getMobileNumber());
        body.put("storeLabel", request.getStoreLabel());
        body.put("terminalLabel", request.getTerminalLabel());
        body.put("purposeOfTransaction", request.getPurposeOfTransaction());

        // Use postWithAuth so Authorization header includes refreshed token
        Object resp = postWithAuth(url, body, KHQRResponse.class);
        return (KHQRResponse<KHQRData>) resp;
    }

    public KHQRResponse<KHQRData> generateMerchant(GenerateMerchantRequest request) {
        MerchantInfo info = new MerchantInfo();
        info.setBakongAccountId(FTB_BAKONG_ACCOUNT_ID);
        info.setMerchantId(request.getMerchantId());
        info.setMerchantName(request.getMerchantName());
        info.setMerchantCity(request.getMerchantCity());
        info.setAcquiringBank(request.getAcquiringBank());
        info.setAmount(request.getAmount());
        return BakongKHQR.generateMerchant(info);
    }

    public KHQRResponse<CRCValidation> verify(String qrCode) {
        return BakongKHQR.verify(qrCode);
    }

    public KHQRResponse<KHQRDecodeData> decode(String qrCode) {
        return BakongKHQR.decode(qrCode);
    }

    // Helper to post with Authorization header and JSON content-type
    private Object postWithAuth(String url, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null && !token.isEmpty()) {
            headers.set("Authorization", "Bearer " + token);
        }
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(url, entity, Object.class);
    }

    // Generic version returning typed response
    private <T> T postWithAuth(String url, Object body, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null && !token.isEmpty()) {
            headers.set("Authorization", "Bearer " + token);
        }
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(url, entity, responseType);
    }

    // 1. Renew Token
    public Object renewToken(String bakongEmail) {
        String url = baseUrl + "/v1/renew_token";
        Map<String, String> body = new HashMap<>();
        body.put("email", bakongEmail);
        // renew token endpoint does not require Authorization per docs
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        logger.error("Renewing token for email: " + bakongEmail);
        return restTemplate.postForObject(url, entity, Object.class);
    }

    private String extractTokenFromRenewResponse(Object renewResp) {
        if (renewResp instanceof Map) {
            Map respMap = (Map) renewResp;
            Object data = respMap.get("data");
            if (data instanceof Map) {
                Object t = ((Map) data).get("token");
                if (t instanceof String) return (String) t;
            }
        }
        return null;
    }

    // 3. Check Transaction Status by MD5
    public Object checkTransactionByMd5(String md5) {
        String url = baseUrl + "/v1/check_transaction_by_md5";
        Map<String, String> body = new HashMap<>();
        body.put("md5", md5);
        return postWithAuth(url, body);
    }

    // 4. Check Transaction Status by Full Hash
    public Object checkTransactionByHash(String hash) {
        String url = baseUrl + "/v1/check_transaction_by_hash";
        Map<String, String> body = new HashMap<>();
        body.put("hash", hash);
        return postWithAuth(url, body);
    }

    // 5. Check Transaction Status by Short Hash
    public Object checkTransactionByShortHash(String hash, Double amount, String currency) {
        String url = baseUrl + "/v1/check_transaction_by_short_hash";
        Map<String, Object> body = new HashMap<>();
        body.put("hash", hash);
        body.put("amount", amount);
        body.put("currency", currency);
        return postWithAuth(url, body);
    }

    // 6. Check Bakong Account
    public Object checkBakongAccount(String accountId) {
        String url = baseUrl + "/v1/check_bakong_account";
        Map<String, String> body = new HashMap<>();
        body.put("accountId", accountId);
        return postWithAuth(url, body);
    }

    // 7. Check Transaction Status by Instruction Reference
    public Object checkTransactionByInstructionRef(String instructionRef) {
        String url = baseUrl + "/v1/check_transaction_by_instruction_ref";
        Map<String, String> body = new HashMap<>();
        body.put("instructionRef", instructionRef);
        return postWithAuth(url, body);
    }

    // 8. Check Transaction Status by External Reference
    public Object checkTransactionByExternalRef(String externalRef) {
        String url = baseUrl + "/v1/check_transaction_by_external_ref";
        Map<String, String> body = new HashMap<>();
        body.put("externalRef", externalRef);
        return postWithAuth(url, body);
    }

    // 9. Check Transaction Status by MD5 List
    public Object checkTransactionByMd5List(List<String> md5List) {
        String url = baseUrl + "/v1/check_transaction_by_md5_list";
        return postWithAuth(url, md5List);
    }

    // 10. Check Transaction Status by Full Hash List
    public Object checkTransactionByHashList(List<String> hashList) {
        String url = baseUrl + "/v1/check_transaction_by_hash_list";
        return postWithAuth(url, hashList);
    }
}
