package org.khqr.balong_khqr.repository;

import org.khqr.balong_khqr.domain.KhqrTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KhqrTransactionRepository
        extends JpaRepository<KhqrTransaction, Long> {
}