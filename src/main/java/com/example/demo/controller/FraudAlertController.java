package com.example.demo.controller;

import com.example.demo.model.FraudAlertRecord;
import com.example.demo.service.FraudAlertService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/fraud-alerts")
@Tag(name = "Alert", description = "Fraud alert endpoints")
public class FraudAlertController {
    
    private final FraudAlertService fraudAlertService;
    
    public FraudAlertController(FraudAlertService fraudAlertService) {
        this.fraudAlertService = fraudAlertService;
    }
    
    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FraudAlertRecord> createAlert(@RequestBody FraudAlertRecord alert) {
        FraudAlertRecord savedAlert = fraudAlertService.createAlert(alert);
        return ResponseEntity.ok(savedAlert);
    }
    
    @GetMapping("/")
    public ResponseEntity<List<FraudAlertRecord>> getAllAlerts() {
        List<FraudAlertRecord> alerts = fraudAlertService.getAllAlerts();
        return ResponseEntity.ok(alerts);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FraudAlertRecord> getAlertById(@PathVariable Long id) {
        Optional<FraudAlertRecord> alert = fraudAlertService.getAllAlerts().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
        return alert.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/serial/{serialNumber}")
    public ResponseEntity<List<FraudAlertRecord>> getAlertsBySerial(@PathVariable String serialNumber) {
        List<FraudAlertRecord> alerts = fraudAlertService.getAlertsBySerial(serialNumber);
        return ResponseEntity.ok(alerts);
    }
    
    @GetMapping("/claim/{claimId}")
    public ResponseEntity<List<FraudAlertRecord>> getAlertsByClaim(@PathVariable Long claimId) {
        List<FraudAlertRecord> alerts = fraudAlertService.getAlertsByClaim(claimId);
        return ResponseEntity.ok(alerts);
    }
    
    @PutMapping("/{id}/resolve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FraudAlertRecord> resolveAlert(@PathVariable Long id) {
        FraudAlertRecord resolvedAlert = fraudAlertService.resolveAlert(id);
        return ResponseEntity.ok(resolvedAlert);
    }
}