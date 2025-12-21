package com.example.demo.controller;

import com.example.demo.model.DeviceOwnershipRecord;
import com.example.demo.service.DeviceOwnershipService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/devices")
public class DeviceOwnershipController {
    private final DeviceOwnershipService deviceOwnershipService;

    public DeviceOwnershipController(DeviceOwnershipService deviceOwnershipService) {
        this.deviceOwnershipService = deviceOwnershipService;
    }

    @PostMapping
    public ResponseEntity<DeviceOwnershipRecord> registerDevice(@Valid @RequestBody DeviceOwnershipRecord device) {
        DeviceOwnershipRecord saved = deviceOwnershipService.registerDevice(device);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<DeviceOwnershipRecord>> getAllDevices() {
        return ResponseEntity.ok(deviceOwnershipService.getAllDevices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceOwnershipRecord> getDeviceById(@PathVariable Long id) {
        DeviceOwnershipRecord device = deviceOwnershipService.updateDeviceStatus(id, true); // triggers existence check
        return ResponseEntity.ok(device);
    }

    @GetMapping("/serial/{serialNumber}")
    public ResponseEntity<DeviceOwnershipRecord> getBySerial(@PathVariable String serialNumber) {
        Optional<DeviceOwnershipRecord> device = deviceOwnershipService.getBySerial(serialNumber);
        return device.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<DeviceOwnershipRecord> updateStatus(@PathVariable Long id, @RequestParam boolean active) {
        DeviceOwnershipRecord updated = deviceOwnershipService.updateDeviceStatus(id, active);
        return ResponseEntity.ok(updated);
    }
}