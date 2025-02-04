package com.pharmacy.pharmacyservice.service;

import com.pharmacy.pharmacyservice.dto.request.UserLoginRequest;
import com.pharmacy.pharmacyservice.dto.response.AuthenticationResponse;
import com.pharmacy.pharmacyservice.entity.Role;
import com.pharmacy.pharmacyservice.entity.Token;
import com.pharmacy.pharmacyservice.entity.User;
import com.pharmacy.pharmacyservice.jwt.JwtService;
import com.pharmacy.pharmacyservice.repository.TokenRepository;
import com.pharmacy.pharmacyservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

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

    public AuthenticationResponse registration(User user, MultipartFile file) throws IOException {
        checkUser(user);

        String imageFileName = "";

        if (file != null && !file.isEmpty()) {
            imageFileName = savedImage(file, user);

        }
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.setCell(user.getCell());
        newUser.setDob(user.getDob());
        newUser.setGender(user.getGender());
        newUser.setRole(Role.valueOf("USER"));
        newUser.setAddress(user.getAddress());

        newUser.setLock(true);
        newUser.setActive(false);
        newUser.setImage(imageFileName);
        userRepository.save(newUser);

        String jwt = jwtService.generateToken(user);
        savedToken(jwt, user);
        return new AuthenticationResponse(jwt, "User successfully registered!");

    }

    public AuthenticationResponse authenticate(UserLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found by this email!.."));
        String jwt = jwtService.generateToken(user);

        removeAllTokenByUser(user);
        savedToken(jwt, user);
        return new AuthenticationResponse(jwt, "Logged in Successfully");

    }

    private void removeAllTokenByUser(User user) {
        List<Token> valiedTokens = tokenRepository.findTokenByUserId(user.getId());

        if (valiedTokens.isEmpty()) {
            return;
        }

        valiedTokens.forEach(t -> t.setLogout(true));
        tokenRepository.saveAll(valiedTokens);
    }

    private void savedToken(String jwt, User user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLogout(false);
        token.setUser(user);
        tokenRepository.save(token);
    }

    // check the user are already existor not
    private void checkUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
    }
    private String savedImage(MultipartFile file, User user) throws IOException {
        Path uploadPath = Paths.get(uploadDir, "users");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = user.getName() + "_" + UUID.randomUUID();
        Path filePath = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath);
        return fileName;

    }
}
