package com.example.demo.controller;

import com.example.demo.model.StolenDeviceReport;
import com.example.demo.service.StolenDeviceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stolen-devices")
@Tag(name = "StolenDevice", description = "Stolen device endpoints")
public class StolenDeviceController {
    
    private final StolenDeviceService stolenDeviceService;
    
    public StolenDeviceController(StolenDeviceService stolenDeviceService) {
        this.stolenDeviceService = stolenDeviceService;
    }
    
    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StolenDeviceReport> reportStolen(@RequestBody StolenDeviceReport report) {
        StolenDeviceReport savedReport = stolenDeviceService.reportStolen(report);
        return ResponseEntity.ok(savedReport);
    }
    
    @GetMapping("/")
    public ResponseEntity<List<StolenDeviceReport>> getAllReports() {
        List<StolenDeviceReport> reports = stolenDeviceService.getAllReports();
        return ResponseEntity.ok(reports);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<StolenDeviceReport> getReportById(@PathVariable Long id) {
        Optional<StolenDeviceReport> report = stolenDeviceService.getReportById(id);
        return report.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/serial/{serialNumber}")
    public ResponseEntity<List<StolenDeviceReport>> getReportsBySerial(@PathVariable String serialNumber) {
        List<StolenDeviceReport> reports = stolenDeviceService.getReportsBySerial(serialNumber);
        return ResponseEntity.ok(reports);
    }
}