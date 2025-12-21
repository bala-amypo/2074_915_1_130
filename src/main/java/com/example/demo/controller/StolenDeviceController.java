package com.example.demo.controller;

import com.example.demo.model.StolenDeviceReport;
import com.example.demo.service.StolenDeviceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stolen-devices")
public class StolenDeviceController {
    private final StolenDeviceService stolenDeviceService;

    public StolenDeviceController(StolenDeviceService stolenDeviceService) {
        this.stolenDeviceService = stolenDeviceService;
    }

    @PostMapping
    public ResponseEntity<StolenDeviceReport> reportStolen(@Valid @RequestBody StolenDeviceReport report) {
        StolenDeviceReport saved = stolenDeviceService.reportStolen(report);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<StolenDeviceReport>> getAllReports() {
        return ResponseEntity.ok(stolenDeviceService.getAllReports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StolenDeviceReport> getReport(@PathVariable Long id) {
        Optional<StolenDeviceReport> report = stolenDeviceService.getReportById(id);
        return report.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/serial/{serialNumber}")
    public ResponseEntity<List<StolenDeviceReport>> getReportsBySerial(@PathVariable String serialNumber) {
        return ResponseEntity.ok(stolenDeviceService.getReportsBySerial(serialNumber));
    }
}