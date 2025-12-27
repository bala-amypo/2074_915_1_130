package com.example.demo.service;

import com.example.demo.model.WarrantyClaimRecord;

import java.util.List;
import java.util.Optional;

public interface WarrantyClaimService {
    WarrantyClaimRecord submitClaim(WarrantyClaimRecord claim);
    WarrantyClaimRecord updateClaimStatus(Long claimId, String status);
    Optional<WarrantyClaimRecord> getClaimById(Long id);
    List<WarrantyClaimRecord> getClaimsBySerial(String serialNumber);
    List<WarrantyClaimRecord> getAllClaims();
}