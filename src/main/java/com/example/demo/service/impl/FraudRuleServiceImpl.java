package com.example.demo.service.impl;

import com.example.demo.model.FraudRule;
import com.example.demo.repository.FraudRuleRepository;
import com.example.demo.service.FraudRuleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class FraudRuleServiceImpl implements FraudRuleService {
    
    private final FraudRuleRepository fraudRuleRepository;
    
    public FraudRuleServiceImpl(FraudRuleRepository fraudRuleRepository) {
        this.fraudRuleRepository = fraudRuleRepository;
    }
    
    @Override
    public FraudRule createRule(FraudRule rule) {
        if (fraudRuleRepository.findByRuleCode(rule.getRuleCode()).isPresent()) {
            throw new IllegalArgumentException("Rule already exists");
        }
        return fraudRuleRepository.save(rule);
    }
    
    @Override
    public FraudRule updateRule(Long id, FraudRule updatedRule) {
        FraudRule existingRule = fraudRuleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Match not found"));
        
        existingRule.setRuleType(updatedRule.getRuleType());
        existingRule.setDescription(updatedRule.getDescription());
        existingRule.setActive(updatedRule.getActive());
        
        return fraudRuleRepository.save(existingRule);
    }
    
    @Override
    public List<FraudRule> getActiveRules() {
        return fraudRuleRepository.findByActiveTrue();
    }
    
    @Override
    public Optional<FraudRule> getRuleByCode(String ruleCode) {
        return fraudRuleRepository.findByRuleCode(ruleCode);
    }
    
    @Override
    public List<FraudRule> getAllRules() {
        return fraudRuleRepository.findAll();
    }
}