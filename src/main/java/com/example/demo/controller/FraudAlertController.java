package com.example.demo.controller;

import com.example.demo.model.FraudAlertRecord;
import com.example.demo.service.FraudAlertService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/fraud-alerts")
public class FraudAlertController {
    private final FraudAlertService fraudAlertService;

    public FraudAlertController(FraudAlertService fraudAlertService) {
        this.fraudAlertService = fraudAlertService;
    }

    @PostMapping
    public ResponseEntity<FraudAlertRecord> createAlert(@Valid @RequestBody FraudAlertRecord alert) {
        FraudAlertRecord saved = fraudAlertService.createAlert(alert);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<FraudAlertRecord>> getAllAlerts() {
        return ResponseEntity.ok(fraudAlertService.getAllAlerts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FraudAlertRecord> getAlert(@PathVariable Long id) {
        Optional<FraudAlertRecord> alertOpt = fraudAlertService.getAlertById(id);
        return alertOpt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/serial/{serialNumber}")
    public ResponseEntity<List<FraudAlertRecord>> getAlertsBySerial(@PathVariable String serialNumber) {
        return ResponseEntity.ok(fraudAlertService.getAlertsBySerial(serialNumber));
    }

    @GetMapping("/claim/{claimId}")
    public ResponseEntity<List<FraudAlertRecord>> getAlertsByClaim(@PathVariable Long claimId) {
        return ResponseEntity.ok(fraudAlertService.getAlertsByClaim(claimId));
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<FraudAlertRecord> resolveAlert(@PathVariable Long id) {
        FraudAlertRecord resolved = fraudAlertService.resolveAlert(id);
        return ResponseEntity.ok(resolved);
    }
}