 package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "warranty_claim_records")
public class WarrantyClaimRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Serial number is required")
    private String serialNumber;
    
    @NotBlank(message = "Claimant name is required")
    private String claimantName;
    
    @Email(message = "Valid email is required")
    private String claimantEmail;
    
    @NotBlank(message = "Claim reason is required")
    private String claimReason;
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED, FLAGGED
    private LocalDateTime submittedAt;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        submittedAt = LocalDateTime.now();
    }

    public WarrantyClaimRecord() {}

    public WarrantyClaimRecord(String serialNumber, String claimantName, String claimReason) {
        this.serialNumber = serialNumber;
        this.claimantName = claimantName;
        this.claimReason = claimReason;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    public String getClaimantName() { return claimantName; }
    public void setClaimantName(String claimantName) { this.claimantName = claimantName; }
    public String getClaimantEmail() { return claimantEmail; }
    public void setClaimantEmail(String claimantEmail) { this.claimantEmail = claimantEmail; }
    public String getClaimReason() { return claimReason; }
    public void setClaimReason(String claimReason) { this.claimReason = claimReason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}