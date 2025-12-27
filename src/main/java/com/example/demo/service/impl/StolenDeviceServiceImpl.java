package com.example.demo.service.impl;

import com.example.demo.model.DeviceOwnershipRecord;
import com.example.demo.model.StolenDeviceReport;
import com.example.demo.repository.DeviceOwnershipRecordRepository;
import com.example.demo.repository.StolenDeviceReportRepository;
import com.example.demo.service.StolenDeviceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StolenDeviceServiceImpl implements StolenDeviceService {
    
    private final StolenDeviceReportRepository stolenDeviceReportRepository;
    private final DeviceOwnershipRecordRepository deviceOwnershipRecordRepository;
    
    public StolenDeviceServiceImpl(StolenDeviceReportRepository stolenDeviceReportRepository,
                                 DeviceOwnershipRecordRepository deviceOwnershipRecordRepository) {
        this.stolenDeviceReportRepository = stolenDeviceReportRepository;
        this.deviceOwnershipRecordRepository = deviceOwnershipRecordRepository;
    }
    
    @Override
    public StolenDeviceReport reportStolen(StolenDeviceReport report) {
        DeviceOwnershipRecord device = deviceOwnershipRecordRepository.findBySerialNumber(report.getSerialNumber())
                .orElseThrow(() -> new NoSuchElementException("Device not found"));
        
        report.setDevice(device);
        return stolenDeviceReportRepository.save(report);
    }
    
    @Override
    public List<StolenDeviceReport> getReportsBySerial(String serialNumber) {
        return stolenDeviceReportRepository.findBySerialNumber(serialNumber);
    }
    
    @Override
    public Optional<StolenDeviceReport> getReportById(Long id) {
        return stolenDeviceReportRepository.findById(id);
    }
    
    @Override
    public List<StolenDeviceReport> getAllReports() {
        return stolenDeviceReportRepository.findAll();
    }
}