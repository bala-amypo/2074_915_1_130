 package com.example.demo.service.impl;

import com.example.demo.model.FraudRule;
import com.example.demo.repository.FraudRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class FraudRuleServiceImpl implements com.example.demo.service.FraudRuleService {
    private final FraudRuleRepository ruleRepo;

    public FraudRuleServiceImpl(FraudRuleRepository ruleRepo) {
        this.ruleRepo = ruleRepo;
    }

    @Override
    public FraudRule createRule(FraudRule rule) {
        if (ruleRepo.findByRuleCode(rule.getRuleCode()).isPresent()) {
            throw new IllegalArgumentException("Rule already exists");
        }
        return ruleRepo.save(rule);
    }

    @Override
    public FraudRule updateRule(Long id, FraudRule updatedRule) {
        FraudRule existing = ruleRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Match not found"));
        existing.setRuleType(updatedRule.getRuleType());
        existing.setDescription(updatedRule.getDescription());
        existing.setActive(updatedRule.getActive());
        return ruleRepo.save(existing);
    }

    @Override
    public List<FraudRule> getActiveRules() {
        return ruleRepo.findByActiveTrue();
    }

    @Override
    public Optional<FraudRule> getRuleByCode(String ruleCode) {
        return ruleRepo.findByRuleCode(ruleCode);
    }

    @Override
    public List<FraudRule> getAllRules() {
        return ruleRepo.findAll();
    }

    @Override
    public Optional<FraudRule> getRuleById(Long id) {
        return ruleRepo.findById(id);
    }
}