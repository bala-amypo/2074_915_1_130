package com.example.demo.controller;

import com.example.demo.model.WarrantyClaimRecord;
import com.example.demo.service.WarrantyClaimService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/claims")
public class WarrantyClaimController {
    private final WarrantyClaimService warrantyClaimService;

    public WarrantyClaimController(WarrantyClaimService warrantyClaimService) {
        this.warrantyClaimService = warrantyClaimService;
    }

    @PostMapping
    public ResponseEntity<WarrantyClaimRecord> submitClaim(@Valid @RequestBody WarrantyClaimRecord claim) {
        WarrantyClaimRecord saved = warrantyClaimService.submitClaim(claim);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<WarrantyClaimRecord>> getAllClaims() {
        return ResponseEntity.ok(warrantyClaimService.getAllClaims());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarrantyClaimRecord> getClaim(@PathVariable Long id) {
        Optional<WarrantyClaimRecord> claim = warrantyClaimService.getClaimById(id);
        return claim.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/serial/{serialNumber}")
    public ResponseEntity<List<WarrantyClaimRecord>> getClaimsBySerial(@PathVariable String serialNumber) {
        return ResponseEntity.ok(warrantyClaimService.getClaimsBySerial(serialNumber));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<WarrantyClaimRecord> updateStatus(@PathVariable Long id, @RequestParam String status) {
        WarrantyClaimRecord updated = warrantyClaimService.updateClaimStatus(id, status);
        return ResponseEntity.ok(updated);
    }
}