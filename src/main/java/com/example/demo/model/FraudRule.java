package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fraud_rules")
public class FraudRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ruleCode;

    @Column(nullable = false)
    private String ruleType;

    private String description;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public FraudRule() {}

    public FraudRule(String ruleCode, String ruleType, String description) {
        this.ruleCode = ruleCode;
        this.ruleType = ruleType;
        this.description = description;
        this.active = true;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public static FraudRuleBuilder builder() {
        return new FraudRuleBuilder();
    }

    public static class FraudRuleBuilder {
        private Long id;
        private String ruleCode;
        private String ruleType;
        private String description;
        private Boolean active = true;

        public FraudRuleBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public FraudRuleBuilder ruleCode(String ruleCode) {
            this.ruleCode = ruleCode;
            return this;
        }

        public FraudRuleBuilder ruleType(String ruleType) {
            this.ruleType = ruleType;
            return this;
        }

        public FraudRuleBuilder description(String description) {
            this.description = description;
            return this;
        }

        public FraudRuleBuilder active(Boolean active) {
            this.active = active;
            return this;
        }

        public FraudRule build() {
            FraudRule rule = new FraudRule(ruleCode, ruleType, description);
            rule.setId(id);
            rule.setActive(active);
            return rule;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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