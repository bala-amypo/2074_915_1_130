package com.example.demo.service;

import com.example.demo.model.StolenDeviceReport;
import java.util.List;
import java.util.Optional;

public interface StolenDeviceService {
    StolenDeviceReport reportStolen(StolenDeviceReport report);
    List<StolenDeviceReport> getReportsBySerial(String serialNumber);
    Optional<StolenDeviceReport> getReportById(Long id);
    List<StolenDeviceReport> getAllReports();
}