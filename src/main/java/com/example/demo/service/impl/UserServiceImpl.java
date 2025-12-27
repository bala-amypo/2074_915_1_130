package com.example.demo.service.impl;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    
    @Override
    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Email already exists"
            );
        }
        
        User user = new User(request.getName(), request.getEmail(), 
                           passwordEncoder.encode(request.getPassword()), request.getRoles());
        return userRepository.save(user);
    }
    
    @Override
    public AuthResponse register(RegisterRequest request) {
        User user = registerUser(request);
        String token = jwtTokenProvider.createToken(user.getId(), user.getEmail(), user.getRoles());
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getRoles());
    }
    
    @Override
    public User loginUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid input");
        }
        
        return user;
    }
    
    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }
    
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }
}