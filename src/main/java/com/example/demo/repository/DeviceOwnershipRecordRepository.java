 package com.example.demo.repository;

import com.example.demo.model.DeviceOwnershipRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DeviceOwnershipRecordRepository extends JpaRepository<DeviceOwnershipRecord, Long> {
    Optional<DeviceOwnershipRecord> findBySerialNumber(String serialNumber);
    boolean existsBySerialNumber(String serialNumber);
    List<DeviceOwnershipRecord> findAll();
}