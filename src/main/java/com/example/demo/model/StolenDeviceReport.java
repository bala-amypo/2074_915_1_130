package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stolen_device_reports")
public class StolenDeviceReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String serialNumber;

    @Column(nullable = false)
    private String reportedBy;

    @Column(name = "report_date")
    private LocalDateTime reportDate;

    private String details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    private DeviceOwnershipRecord device;

    public StolenDeviceReport() {}

    public StolenDeviceReport(String serialNumber, String reportedBy, String details) {
        this.serialNumber = serialNumber;
        this.reportedBy = reportedBy;
        this.details = details;
    }

    @PrePersist
    protected void onCreate() {
        reportDate = LocalDateTime.now();
    }

    public static StolenDeviceReportBuilder builder() {
        return new StolenDeviceReportBuilder();
    }

    public static class StolenDeviceReportBuilder {
        private Long id;
        private String serialNumber;
        private String reportedBy;
        private String details;
        private DeviceOwnershipRecord device;

        public StolenDeviceReportBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public StolenDeviceReportBuilder serialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
            return this;
        }

        public StolenDeviceReportBuilder reportedBy(String reportedBy) {
            this.reportedBy = reportedBy;
            return this;
        }

        public StolenDeviceReportBuilder details(String details) {
            this.details = details;
            return this;
        }

        public StolenDeviceReportBuilder device(DeviceOwnershipRecord device) {
            this.device = device;
            return this;
        }

        public StolenDeviceReport build() {
            StolenDeviceReport report = new StolenDeviceReport(serialNumber, reportedBy, details);
            report.setId(id);
            report.setDevice(device);
            return report;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDateTime reportDate) {
        this.reportDate = reportDate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public DeviceOwnershipRecord getDevice() {
        return device;
    }

    public void setDevice(DeviceOwnershipRecord device) {
        this.device = device;
    }
}