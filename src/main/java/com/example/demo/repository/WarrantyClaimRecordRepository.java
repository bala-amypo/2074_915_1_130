 package com.example.demo.repository;

import com.example.demo.model.WarrantyClaimRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WarrantyClaimRecordRepository extends JpaRepository<WarrantyClaimRecord, Long> {
    List<WarrantyClaimRecord> findBySerialNumber(String serialNumber);
    boolean existsBySerialNumberAndClaimReason(String serialNumber, String claimReason);
    List<WarrantyClaimRecord> findByStatus(String status);
}