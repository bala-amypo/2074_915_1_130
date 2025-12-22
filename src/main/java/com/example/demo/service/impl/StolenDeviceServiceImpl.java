 package com.example.demo.service.impl;

import com.example.demo.model.StolenDeviceReport;
import com.example.demo.repository.DeviceOwnershipRecordRepository;
import com.example.demo.repository.StolenDeviceReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StolenDeviceServiceImpl implements com.example.demo.service.StolenDeviceService {
    private final StolenDeviceReportRepository reportRepo;
    private final DeviceOwnershipRecordRepository deviceRepo;

    public StolenDeviceServiceImpl(StolenDeviceReportRepository reportRepo, DeviceOwnershipRecordRepository deviceRepo) {
        this.reportRepo = reportRepo;
        this.deviceRepo = deviceRepo;
    }

    @Override
    public StolenDeviceReport reportStolen(StolenDeviceReport report) {
        if (!deviceRepo.existsBySerialNumber(report.getSerialNumber())) {
            throw new NoSuchElementException("Device not found");
        }
        if (reportRepo.existsBySerialNumber(report.getSerialNumber())) {
            // Already reported? Update or ignore. But spec doesn't forbid re-reporting.
            // We'll allow it but ensure uniqueness via DB constraint (already in entity)
        }
        return reportRepo.save(report);
    }

    @Override
    public List<StolenDeviceReport> getReportsBySerial(String serialNumber) {
        return reportRepo.findBySerialNumber(serialNumber);
    }

    @Override
    public Optional<StolenDeviceReport> getReportById(Long id) {
        return reportRepo.findById(id);
    }

    @Override
    public List<StolenDeviceReport> getAllReports() {
        return reportRepo.findAll();
    }
}