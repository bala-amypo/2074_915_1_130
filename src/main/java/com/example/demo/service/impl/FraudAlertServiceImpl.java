package com.example.demo.service.impl;

import com.example.demo.model.FraudAlertRecord;
import com.example.demo.repository.FraudAlertRecordRepository;
import com.example.demo.service.FraudAlertService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FraudAlertServiceImpl implements FraudAlertService {
    
    private final FraudAlertRecordRepository fraudAlertRecordRepository;
    
    public FraudAlertServiceImpl(FraudAlertRecordRepository fraudAlertRecordRepository) {
        this.fraudAlertRecordRepository = fraudAlertRecordRepository;
    }
    
    @Override
    public FraudAlertRecord createAlert(FraudAlertRecord alert) {
        return fraudAlertRecordRepository.save(alert);
    }
    
    @Override
    public FraudAlertRecord resolveAlert(Long id) {
        FraudAlertRecord alert = fraudAlertRecordRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Category not found"));
        alert.setResolved(true);
        return fraudAlertRecordRepository.save(alert);
    }
    
    @Override
    public List<FraudAlertRecord> getAlertsBySerial(String serialNumber) {
        return fraudAlertRecordRepository.findBySerialNumber(serialNumber);
    }
    
    @Override
    public List<FraudAlertRecord> getAlertsByClaim(Long claimId) {
        return fraudAlertRecordRepository.findByClaimId(claimId);
    }
    
    @Override
    public List<FraudAlertRecord> getAllAlerts() {
        return fraudAlertRecordRepository.findAll();
    }
}