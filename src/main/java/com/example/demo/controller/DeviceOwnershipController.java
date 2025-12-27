package com.example.demo.controller;

import com.example.demo.model.DeviceOwnershipRecord;
import com.example.demo.service.DeviceOwnershipService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/devices")
@Tag(name = "Device", description = "Device ownership endpoints")
public class DeviceOwnershipController {
    
    private final DeviceOwnershipService deviceOwnershipService;
    
    public DeviceOwnershipController(DeviceOwnershipService deviceOwnershipService) {
        this.deviceOwnershipService = deviceOwnershipService;
    }
    
    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeviceOwnershipRecord> registerDevice(@RequestBody DeviceOwnershipRecord device) {
        DeviceOwnershipRecord savedDevice = deviceOwnershipService.registerDevice(device);
        return ResponseEntity.ok(savedDevice);
    }
    
    @GetMapping("/")
    public ResponseEntity<List<DeviceOwnershipRecord>> getAllDevices() {
        List<DeviceOwnershipRecord> devices = deviceOwnershipService.getAllDevices();
        return ResponseEntity.ok(devices);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DeviceOwnershipRecord> getDeviceById(@PathVariable Long id) {
        Optional<DeviceOwnershipRecord> device = deviceOwnershipService.getAllDevices().stream()
                .filter(d -> d.getId().equals(id))
                .findFirst();
        return device.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/serial/{serialNumber}")
    public ResponseEntity<DeviceOwnershipRecord> getDeviceBySerial(@PathVariable String serialNumber) {
        Optional<DeviceOwnershipRecord> device = deviceOwnershipService.getBySerial(serialNumber);
        return device.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DeviceOwnershipRecord> updateDeviceStatus(@PathVariable Long id, @RequestParam boolean active) {
        DeviceOwnershipRecord updatedDevice = deviceOwnershipService.updateDeviceStatus(id, active);
        return ResponseEntity.ok(updatedDevice);
    }
}