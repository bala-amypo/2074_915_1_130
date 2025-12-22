package com.example.demo.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TestResultListener {
    
    @EventListener
    public void handleTestResult(Object event) {
        // Handle test result events if needed
    }
}
