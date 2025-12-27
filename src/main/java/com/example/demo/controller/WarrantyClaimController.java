package com.example.demo.controller;

import com.example.demo.model.WarrantyClaimRecord;
import com.example.demo.service.WarrantyClaimService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/claims")
@Tag(name = "Claim", description = "Warranty claim endpoints")
public class WarrantyClaimController {
    
    private final WarrantyClaimService warrantyClaimService;
    
    public WarrantyClaimController(WarrantyClaimService warrantyClaimService) {
        this.warrantyClaimService = warrantyClaimService;
    }
    
    @PostMapping("/")
    public ResponseEntity<WarrantyClaimRecord> submitClaim(@RequestBody WarrantyClaimRecord claim) {
        WarrantyClaimRecord savedClaim = warrantyClaimService.submitClaim(claim);
        return ResponseEntity.ok(savedClaim);
    }
    
    @GetMapping("/")
    public ResponseEntity<List<WarrantyClaimRecord>> getAllClaims() {
        List<WarrantyClaimRecord> claims = warrantyClaimService.getAllClaims();
        return ResponseEntity.ok(claims);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<WarrantyClaimRecord> getClaimById(@PathVariable Long id) {
        Optional<WarrantyClaimRecord> claim = warrantyClaimService.getClaimById(id);
        return claim.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/serial/{serialNumber}")
    public ResponseEntity<List<WarrantyClaimRecord>> getClaimsBySerial(@PathVariable String serialNumber) {
        List<WarrantyClaimRecord> claims = warrantyClaimService.getClaimsBySerial(serialNumber);
        return ResponseEntity.ok(claims);
    }
    
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WarrantyClaimRecord> updateClaimStatus(@PathVariable Long id, @RequestParam String status) {
        WarrantyClaimRecord updatedClaim = warrantyClaimService.updateClaimStatus(id, status);
        return ResponseEntity.ok(updatedClaim);
    }
}