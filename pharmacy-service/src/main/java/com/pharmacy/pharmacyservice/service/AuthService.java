package com.pharmacy.pharmacyservice.service;

import com.pharmacy.pharmacyservice.jwt.JwtService;
import com.pharmacy.pharmacyservice.repository.TokenRepository;
import com.pharmacy.pharmacyservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    @Value("${image.upload.dir}")
    private String uploadDir;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
                       TokenRepository tokenRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
    }
}
