package com.example.demo.repository;

import com.example.demo.model.FraudRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FraudRuleRepository extends JpaRepository<FraudRule, Long> {
    Optional<FraudRule> findById(Long id);
    List<FraudRule> findAll();
    List<FraudRule> findByActiveTrue();
    Optional<FraudRule> findByRuleCode(String ruleCode);
}