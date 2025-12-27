package com.example.demo.repository;

import com.example.demo.model.StolenDeviceReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StolenDeviceReportRepository extends JpaRepository<StolenDeviceReport, Long> {
    Optional<StolenDeviceReport> findById(Long id);
    List<StolenDeviceReport> findAll();
    List<StolenDeviceReport> findBySerialNumber(String serialNumber);
    boolean existsBySerialNumber(String serialNumber);
}