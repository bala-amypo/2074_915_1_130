 package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "stolen_device_reports",
       uniqueConstraints = @UniqueConstraint(columnNames = "serialNumber"))
public class StolenDeviceReport {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Serial number is required")
    private String serialNumber;
    
    @NotBlank(message = "Reported by is required")
    private String reportedBy;
    private LocalDateTime reportDate;
    private String details;

    @PrePersist
    protected void onCreate() {
        reportDate = LocalDateTime.now();
    }

    public StolenDeviceReport() {}

    public StolenDeviceReport(String serialNumber, String reportedBy) {
        this.serialNumber = serialNumber;
        this.reportedBy = reportedBy;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    public String getReportedBy() { return reportedBy; }
    public void setReportedBy(String reportedBy) { this.reportedBy = reportedBy; }
    public LocalDateTime getReportDate() { return reportDate; }
    public void setReportDate(LocalDateTime reportDate) { this.reportDate = reportDate; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}