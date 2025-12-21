package com.example.demo.service;

import com.example.demo.model.FraudAlertRecord;
import java.util.List;
import java.util.Optional;

public interface FraudAlertService {
    FraudAlertRecord createAlert(FraudAlertRecord alert);
    FraudAlertRecord resolveAlert(Long id);
    List<FraudAlertRecord> getAlertsBySerial(String serialNumber);
    List<FraudAlertRecord> getAlertsByClaim(Long claimId);
    List<FraudAlertRecord> getAllAlerts();
    Optional<FraudAlertRecord> getAlertById(Long id);
}