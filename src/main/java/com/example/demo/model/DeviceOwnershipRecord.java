package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "device_ownership_records")
public class DeviceOwnershipRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String serialNumber;

    @Column(nullable = false)
    private String ownerName;

    private String ownerEmail;

    private LocalDate purchaseDate;

    @Column(nullable = false)
    private LocalDate warrantyExpiration;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public DeviceOwnershipRecord() {}

    public DeviceOwnershipRecord(String serialNumber, String ownerName, String ownerEmail, 
                               LocalDate purchaseDate, LocalDate warrantyExpiration) {
        this.serialNumber = serialNumber;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
        this.purchaseDate = purchaseDate;
        this.warrantyExpiration = warrantyExpiration;
        this.active = true;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static DeviceOwnershipRecordBuilder builder() {
        return new DeviceOwnershipRecordBuilder();
    }

    public static class DeviceOwnershipRecordBuilder {
        private Long id;
        private String serialNumber;
        private String ownerName;
        private String ownerEmail;
        private LocalDate purchaseDate;
        private LocalDate warrantyExpiration;
        private Boolean active = true;

        public DeviceOwnershipRecordBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public DeviceOwnershipRecordBuilder serialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
            return this;
        }

        public DeviceOwnershipRecordBuilder ownerName(String ownerName) {
            this.ownerName = ownerName;
            return this;
        }

        public DeviceOwnershipRecordBuilder ownerEmail(String ownerEmail) {
            this.ownerEmail = ownerEmail;
            return this;
        }

        public DeviceOwnershipRecordBuilder purchaseDate(LocalDate purchaseDate) {
            this.purchaseDate = purchaseDate;
            return this;
        }

        public DeviceOwnershipRecordBuilder warrantyExpiration(LocalDate warrantyExpiration) {
            this.warrantyExpiration = warrantyExpiration;
            return this;
        }

        public DeviceOwnershipRecordBuilder active(Boolean active) {
            this.active = active;
            return this;
        }

        public DeviceOwnershipRecord build() {
            DeviceOwnershipRecord record = new DeviceOwnershipRecord(serialNumber, ownerName, ownerEmail, purchaseDate, warrantyExpiration);
            record.setId(id);
            record.setActive(active);
            return record;
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public LocalDate getWarrantyExpiration() {
        return warrantyExpiration;
    }

    public void setWarrantyExpiration(LocalDate warrantyExpiration) {
        this.warrantyExpiration = warrantyExpiration;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}