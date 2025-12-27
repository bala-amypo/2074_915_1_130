package com.example.demo.service.impl;

import com.example.demo.model.DeviceOwnershipRecord;
import com.example.demo.model.WarrantyClaimRecord;
import com.example.demo.repository.DeviceOwnershipRecordRepository;
import com.example.demo.repository.FraudAlertRecordRepository;
import com.example.demo.repository.FraudRuleRepository;
import com.example.demo.repository.StolenDeviceReportRepository;
import com.example.demo.repository.WarrantyClaimRecordRepository;
import com.example.demo.service.WarrantyClaimService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class WarrantyClaimServiceImpl implements WarrantyClaimService {
    
    private final WarrantyClaimRecordRepository warrantyClaimRecordRepository;
    private final DeviceOwnershipRecordRepository deviceOwnershipRecordRepository;
    private final StolenDeviceReportRepository stolenDeviceReportRepository;
    private final FraudAlertRecordRepository fraudAlertRecordRepository;
    private final FraudRuleRepository fraudRuleRepository;
    
    public WarrantyClaimServiceImpl(WarrantyClaimRecordRepository warrantyClaimRecordRepository,
                                  DeviceOwnershipRecordRepository deviceOwnershipRecordRepository,
                                  StolenDeviceReportRepository stolenDeviceReportRepository,
                                  FraudAlertRecordRepository fraudAlertRecordRepository,
                                  FraudRuleRepository fraudRuleRepository) {
        this.warrantyClaimRecordRepository = warrantyClaimRecordRepository;
        this.deviceOwnershipRecordRepository = deviceOwnershipRecordRepository;
        this.stolenDeviceReportRepository = stolenDeviceReportRepository;
        this.fraudAlertRecordRepository = fraudAlertRecordRepository;
        this.fraudRuleRepository = fraudRuleRepository;
    }
    
    @Override
    public WarrantyClaimRecord submitClaim(WarrantyClaimRecord claim) {
        // Check if device exists
        DeviceOwnershipRecord device = deviceOwnershipRecordRepository.findBySerialNumber(claim.getSerialNumber())
                .orElseThrow(() -> new NoSuchElementException("Offer not found"));
        
        // Check if device is stolen
        boolean isStolen = stolenDeviceReportRepository.existsBySerialNumber(claim.getSerialNumber());
        
        // Check if warranty is expired
        boolean isExpired = device.getWarrantyExpiration().isBefore(LocalDate.now());
        
        // Check for duplicate claim
        boolean isDuplicate = warrantyClaimRecordRepository.existsBySerialNumberAndClaimReason(
                claim.getSerialNumber(), claim.getClaimReason());
        
        // Set status based on fraud checks
        if (isStolen || isExpired || isDuplicate) {
            claim.setStatus("FLAGGED");
        } else {
            claim.setStatus("PENDING");
        }
        
        claim.setDevice(device);
        return warrantyClaimRecordRepository.save(claim);
    }
    
    @Override
    public WarrantyClaimRecord updateClaimStatus(Long claimId, String status) {
        WarrantyClaimRecord claim = warrantyClaimRecordRepository.findById(claimId)
                .orElseThrow(() -> new NoSuchElementException("Request not found"));
        claim.setStatus(status);
        return warrantyClaimRecordRepository.save(claim);
    }
    
    @Override
    public Optional<WarrantyClaimRecord> getClaimById(Long id) {
        return warrantyClaimRecordRepository.findById(id);
    }
    
    @Override
    public List<WarrantyClaimRecord> getClaimsBySerial(String serialNumber) {
        return warrantyClaimRecordRepository.findBySerialNumber(serialNumber);
    }
    
    @Override
    public List<WarrantyClaimRecord> getAllClaims() {
        return warrantyClaimRecordRepository.findAll();
    }
}