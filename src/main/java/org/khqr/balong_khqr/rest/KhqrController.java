package org.khqr.balong_khqr.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kh.gov.nbc.bakong_khqr.model.*;
import org.khqr.balong_khqr.domain.dto.DecodeRequest;
import org.khqr.balong_khqr.domain.dto.GenerateIndividualRequest;
import org.khqr.balong_khqr.domain.dto.GenerateMerchantRequest;
import org.khqr.balong_khqr.domain.dto.VerifyRequest;
import org.khqr.balong_khqr.service.KhqrService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/khqr")
@Tag(name = "KHQR", description = "Bakong KHQR APIs")
public class KhqrController {

    private final KhqrService khqrService;

    public KhqrController(KhqrService khqrService) {
        this.khqrService = khqrService;
    }

    @PostMapping("/individual")
    @Operation(summary = "Generate Individual KHQR")
    public KHQRResponse generateIndividual(@RequestBody GenerateIndividualRequest request) {
        return khqrService.generateIndividual(request);
    }

    @PostMapping("/merchant")
    @Operation(summary = "Generate Merchant KHQR")
    public KHQRResponse<KHQRData> generateMerchant(@RequestBody GenerateMerchantRequest request) {
        return khqrService.generateMerchant(request);
    }

    @PostMapping("/verify")
    @Operation(summary = "Verify KHQR")
    public KHQRResponse<CRCValidation> verify(@RequestBody VerifyRequest request) {
        return khqrService.verify(request.getQrCode());
    }

    @PostMapping("/decode")
    @Operation(summary = "Decode KHQR")
    public KHQRResponse<KHQRDecodeData> decode(@RequestBody DecodeRequest request) {
        return khqrService.decode(request.getQrCode());
    }

    // New endpoints that forward to Bakong open API via KhqrService

    @PostMapping("/renew-token")
    @Operation(summary = "Renew token from Bakong")
    public Object renewToken(@RequestParam String email) {
        return khqrService.renewToken(email);
    }

    @PostMapping("/check-by-md5")
    @Operation(summary = "Check transaction by MD5")
    public Object checkByMd5(@RequestParam String md5) {
        return khqrService.checkTransactionByMd5(md5);
    }

    @PostMapping("/check-by-hash")
    @Operation(summary = "Check transaction by full hash")
    public Object checkByHash(@RequestParam String hash) {
        return khqrService.checkTransactionByHash(hash);
    }

    @PostMapping("/check-by-short-hash")
    @Operation(summary = "Check transaction by short hash")
    public Object checkByShortHash(@RequestParam String hash,
                                   @RequestParam Double amount,
                                   @RequestParam String currency) {
        return khqrService.checkTransactionByShortHash(hash, amount, currency);
    }

    @PostMapping("/check-account")
    @Operation(summary = "Check bakong account")
    public Object checkAccount(@RequestParam String accountId) {
        return khqrService.checkBakongAccount(accountId);
    }

    @PostMapping("/check-by-instruction-ref")
    @Operation(summary = "Check transaction by instruction ref")
    public Object checkByInstructionRef(@RequestParam String instructionRef) {
        return khqrService.checkTransactionByInstructionRef(instructionRef);
    }

    @PostMapping("/check-by-external-ref")
    @Operation(summary = "Check transaction by external ref")
    public Object checkByExternalRef(@RequestParam String externalRef) {
        return khqrService.checkTransactionByExternalRef(externalRef);
    }

    @PostMapping("/check-by-md5-list")
    @Operation(summary = "Check transaction by md5 list")
    public Object checkByMd5List(@RequestBody List<String> md5List) {
        return khqrService.checkTransactionByMd5List(md5List);
    }

    @PostMapping("/check-by-hash-list")
    @Operation(summary = "Check transaction by hash list")
    public Object checkByHashList(@RequestBody List<String> hashList) {
        return khqrService.checkTransactionByHashList(hashList);
    }
}
