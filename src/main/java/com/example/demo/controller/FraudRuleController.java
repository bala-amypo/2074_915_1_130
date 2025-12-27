package com.example.demo.controller;

import com.example.demo.model.FraudRule;
import com.example.demo.service.FraudRuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/fraud-rules")
@Tag(name = "Rule", description = "Fraud rule endpoints")
public class FraudRuleController {
    
    private final FraudRuleService fraudRuleService;
    
    public FraudRuleController(FraudRuleService fraudRuleService) {
        this.fraudRuleService = fraudRuleService;
    }
    
    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FraudRule> createRule(@RequestBody FraudRule rule) {
        FraudRule savedRule = fraudRuleService.createRule(rule);
        return ResponseEntity.ok(savedRule);
    }
    
    @GetMapping("/")
    public ResponseEntity<List<FraudRule>> getAllRules() {
        List<FraudRule> rules = fraudRuleService.getAllRules();
        return ResponseEntity.ok(rules);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FraudRule> getRuleById(@PathVariable Long id) {
        Optional<FraudRule> rule = fraudRuleService.getAllRules().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();
        return rule.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<FraudRule>> getActiveRules() {
        List<FraudRule> activeRules = fraudRuleService.getActiveRules();
        return ResponseEntity.ok(activeRules);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FraudRule> updateRule(@PathVariable Long id, @RequestBody FraudRule updatedRule) {
        FraudRule rule = fraudRuleService.updateRule(id, updatedRule);
        return ResponseEntity.ok(rule);
    }
}