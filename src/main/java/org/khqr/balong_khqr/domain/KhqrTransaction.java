package org.khqr.balong_khqr.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "khqr_transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhqrTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String qr;

    @Column(length = 32)
    private String md5;

    @Column(name = "transaction_type", length = 20)
    private String transactionType;

    @Column(name = "bakong_account_id", nullable = false, length = 32)
    private String bakongAccountId;

    @Column(name = "merchant_id", length = 32)
    private String merchantId;

    @Column(name = "merchant_name", length = 50)
    private String merchantName;

    @Column(name = "merchant_city", length = 50)
    private String merchantCity;

    @Column(precision = 13, scale = 2)
    private BigDecimal amount;

    @Column(length = 3)
    private String currency = "KHR";

    @Column(name = "bill_number", length = 25)
    private String billNumber;

    @Column(name = "mobile_number", length = 25)
    private String mobileNumber;

    @Column(name = "store_label", length = 25)
    private String storeLabel;

    @Column(name = "terminal_label", length = 25)
    private String terminalLabel;

    @Column(name = "purpose_of_transaction", length = 25)
    private String purposeOfTransaction;

    @Column(name = "verify_status")
    private Boolean verifyStatus;

    @Column(length = 8)
    private String crc;

    @Column(name = "raw_decode_response", columnDefinition = "jsonb")
    private String rawDecodeResponse;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
