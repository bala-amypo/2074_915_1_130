package com.example.demo.service;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.User;

public interface UserService {
    User registerUser(RegisterRequest request);
    AuthResponse register(RegisterRequest request);
    User loginUser(LoginRequest request);
    User getById(Long id);
    User findByEmail(String email);
}