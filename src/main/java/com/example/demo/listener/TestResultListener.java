package com.example.demo.listener;

public class TestResultListener {
    
    public TestResultListener() {
        // Default constructor
    }
    
    public void onTestStart(Object result) {
        System.out.println("Test started");
    }
    
    public void onTestSuccess(Object result) {
        System.out.println("Test passed");
    }
    
    public void onTestFailure(Object result) {
        System.out.println("Test failed");
    }
    
    public void onTestSkipped(Object result) {
        System.out.println("Test skipped");
    }
}