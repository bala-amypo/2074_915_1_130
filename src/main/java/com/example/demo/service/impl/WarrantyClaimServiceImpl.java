 package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class WarrantyClaimServiceImpl implements com.example.demo.service.WarrantyClaimService {
    private final WarrantyClaimRecordRepository claimRepo;
    private final DeviceOwnershipRecordRepository deviceRepo;
    private final StolenDeviceReportRepository stolenRepo;
    private final FraudAlertRecordRepository alertRepo;
    private final FraudRuleRepository ruleRepo;

    public WarrantyClaimServiceImpl(WarrantyClaimRecordRepository claimRepo,
                                    DeviceOwnershipRecordRepository deviceRepo,
                                    StolenDeviceReportRepository stolenRepo,
                                    FraudAlertRecordRepository alertRepo,
                                    FraudRuleRepository ruleRepo) {
        this.claimRepo = claimRepo;
        this.deviceRepo = deviceRepo;
        this.stolenRepo = stolenRepo;
        this.alertRepo = alertRepo;
        this.ruleRepo = ruleRepo;
    }

    @Override
    public WarrantyClaimRecord submitClaim(WarrantyClaimRecord claim) {
        // Device exists?
        DeviceOwnershipRecord device = deviceRepo.findBySerialNumber(claim.getSerialNumber())
                .orElseThrow(() -> new NoSuchElementException("Offer not found")); // per spec

        // Stolen?
        if (stolenRepo.existsBySerialNumber(claim.getSerialNumber())) {
            claim.setStatus("FLAGGED");
            createAlert(claim.getId(), claim.getSerialNumber(), "STOLEN_DEVICE", "CRITICAL");
        }
        // Warranty expired?
        else if (device.getWarrantyExpiration().isBefore(LocalDate.now())) {
            claim.setStatus("FLAGGED");
            createAlert(claim.getId(), claim.getSerialNumber(), "EXPIRED_WARRANTY", "HIGH");
        }
        // Duplicate?
        else if (claimRepo.existsBySerialNumberAndClaimReason(claim.getSerialNumber(), claim.getClaimReason())) {
            claim.setStatus("FLAGGED");
            createAlert(claim.getId(), claim.getSerialNumber(), "DUPLICATE_CLAIM", "MEDIUM");
        }

        return claimRepo.save(claim);
    }

    private void createAlert(Long claimId, String serial, String type, String severity) {
        FraudAlertRecord alert = new FraudAlertRecord(claimId, serial, type, severity);
        alertRepo.save(alert);
    }

    @Override
    public WarrantyClaimRecord updateClaimStatus(Long claimId, String status) {
        WarrantyClaimRecord claim = claimRepo.findById(claimId)
                .orElseThrow(() -> new NoSuchElementException("Request not found"));
        claim.setStatus(status);
        return claimRepo.save(claim);
    }

    @Override
    public Optional<WarrantyClaimRecord> getClaimById(Long id) {
        return claimRepo.findById(id);
    }

    @Override
    public List<WarrantyClaimRecord> getClaimsBySerial(String serialNumber) {
        return claimRepo.findBySerialNumber(serialNumber);
    }

    @Override
    public List<WarrantyClaimRecord> getAllClaims() {
        return claimRepo.findAll();
    }
}