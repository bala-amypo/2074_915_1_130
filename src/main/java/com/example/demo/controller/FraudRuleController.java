package com.example.demo.controller;

import com.example.demo.model.FraudRule;
import com.example.demo.service.FraudRuleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/fraud-rules")
public class FraudRuleController {
    private final FraudRuleService fraudRuleService;

    public FraudRuleController(FraudRuleService fraudRuleService) {
        this.fraudRuleService = fraudRuleService;
    }

    @PostMapping
    public ResponseEntity<FraudRule> createRule(@Valid @RequestBody FraudRule rule) {
        FraudRule saved = fraudRuleService.createRule(rule);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<FraudRule>> getAllRules() {
        return ResponseEntity.ok(fraudRuleService.getAllRules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FraudRule> getRule(@PathVariable Long id) {
        Optional<FraudRule> ruleOpt = fraudRuleService.getRuleById(id);
        return ruleOpt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    public ResponseEntity<List<FraudRule>> getActiveRules() {
        return ResponseEntity.ok(fraudRuleService.getActiveRules());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FraudRule> updateRule(@PathVariable Long id, @Valid @RequestBody FraudRule updatedRule) {
        FraudRule updated = fraudRuleService.updateRule(id, updatedRule);
        return ResponseEntity.ok(updated);
    }
}