 package com.example.demo.repository;

import com.example.demo.model.FraudAlertRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FraudAlertRecordRepository extends JpaRepository<FraudAlertRecord, Long> {
    List<FraudAlertRecord> findByClaimId(Long claimId);
    List<FraudAlertRecord> findBySerialNumber(String serialNumber);
    List<FraudAlertRecord> findByResolved(Boolean resolved);
}