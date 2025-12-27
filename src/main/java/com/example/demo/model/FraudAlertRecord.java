package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fraud_alert_records")
public class FraudAlertRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String serialNumber;

    @Column(nullable = false)
    private String alertType;

    @Column(nullable = false)
    private String severity;

    private String message;

    @Column(name = "alert_date")
    private LocalDateTime alertDate;

    @Column(nullable = false)
    private Boolean resolved = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id")
    private WarrantyClaimRecord claim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public FraudAlertRecord() {}

    public FraudAlertRecord(WarrantyClaimRecord claim, String serialNumber, String alertType, String severity, String message) {
        this.claim = claim;
        this.serialNumber = serialNumber;
        this.alertType = alertType;
        this.severity = severity;
        this.message = message;
        this.resolved = false;
    }

    @PrePersist
    protected void onCreate() {
        alertDate = LocalDateTime.now();
    }

    public static FraudAlertRecordBuilder builder() {
        return new FraudAlertRecordBuilder();
    }

    public static class FraudAlertRecordBuilder {
        private Long id;

        private String serialNumber;
        private String alertType;
        private String severity;
        private String message;
        private Boolean resolved = false;
        private WarrantyClaimRecord claim;
        private User user;

        public FraudAlertRecordBuilder id(Long id) {
            this.id = id;
            return this;
        }



        public FraudAlertRecordBuilder serialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
            return this;
        }

        public FraudAlertRecordBuilder alertType(String alertType) {
            this.alertType = alertType;
            return this;
        }

        public FraudAlertRecordBuilder severity(String severity) {
            this.severity = severity;
            return this;
        }

        public FraudAlertRecordBuilder message(String message) {
            this.message = message;
            return this;
        }

        public FraudAlertRecordBuilder resolved(Boolean resolved) {
            this.resolved = resolved;
            return this;
        }

        public FraudAlertRecordBuilder claimId(Long claimId) {
            this.claim = WarrantyClaimRecord.builder().id(claimId).build();
            return this;
        }

        public FraudAlertRecordBuilder claim(WarrantyClaimRecord claim) {
            this.claim = claim;
            return this;
        }

        public FraudAlertRecordBuilder user(User user) {
            this.user = user;
            return this;
        }

        public FraudAlertRecord build() {
            FraudAlertRecord alert = new FraudAlertRecord(claim, serialNumber, alertType, severity, message);
            alert.setId(id);
            alert.setResolved(resolved);
            alert.setUser(user);
            return alert;
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

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(LocalDateTime alertDate) {
        this.alertDate = alertDate;
    }

    public Boolean getResolved() {
        return resolved;
    }

    public void setResolved(Boolean resolved) {
        this.resolved = resolved;
    }

    public WarrantyClaimRecord getClaim() {
        return claim;
    }

    public void setClaim(WarrantyClaimRecord claim) {
        this.claim = claim;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}