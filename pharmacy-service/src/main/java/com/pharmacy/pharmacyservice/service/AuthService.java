package com.pharmacy.pharmacyservice.service;

import com.pharmacy.pharmacyservice.dto.response.AuthenticationResponse;
import com.pharmacy.pharmacyservice.entity.Token;
import com.pharmacy.pharmacyservice.entity.User;
import com.pharmacy.pharmacyservice.jwt.JwtService;
import com.pharmacy.pharmacyservice.repository.TokenRepository;
import com.pharmacy.pharmacyservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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


    public AuthenticationResponse registration(User user, MultipartFile file) {
        // check 1st,
        checkUser(user);

        if (file == null || file.isEmpty()) {
            try {
                throw new IOException("File is empty");
            } catch (IOException e) {
                throw new RuntimeException(e);

            }
        }
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.setCell(user.getCell());
        newUser.setDob(user.getDob());
        newUser.setGender(user.getGender());
        newUser.setRole(user.getRole());
        newUser.setAddress(user.getAddress());


        newUser.setLock(true);
        newUser.setActive(false);
        newUser.setImage(savedImae(file, user));
        userRepository.save(newUser);

        String jwt = jwtService.generateToken(user);
        savedToken(jwt, user);
        return new AuthenticationResponse(jwt, "User successfully registered!");

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

    private String savedImae(MultipartFile file, User user) {
        Path uploadPath = Paths.get(uploadDir, "users");
        // check folder is exists or not
        if (!Files.exists(uploadPath)) {
            // if not then create the folder
            try {
                Files.createDirectories(uploadPath);
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
        }

        // this name will saved and we catch the image bu this name from frontend
        String filename = user.getName() + "_" + UUID.randomUUID();
        Path filePath = uploadPath.resolve(filename);
        try {
            Files.copy(file.getInputStream(), filePath);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
        return filename;

    }
}
