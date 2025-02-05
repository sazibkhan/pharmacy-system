package com.pharmacy.pharmacyservice.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pharmacy.pharmacyservice.entity.ForgetPassowrdCode;
import com.pharmacy.pharmacyservice.entity.User;
import com.pharmacy.pharmacyservice.repository.ForgetPasswordRepository;
import com.pharmacy.pharmacyservice.repository.UserRepository;

import jakarta.mail.MessagingException;

@Service
public class ForgetPassword {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ForgetPasswordRepository forgetPasswordRepository;



    public void sendEmailForForgetPassword(String email) {
        String activationLink = "http://localhost:8080/auth/newpassword/";
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found by this email"));

        Random random = new Random();
        long randomNumber = 10000000L + random.nextInt(90000000);
        String mailtext = "<h2> Dear " + user.getName() + "</h2>"
                + "<p>Your Code is: " + randomNumber + "</p>"
                +"<p> click where to give password and varification"+activationLink+"</p>";
                ;

        ForgetPassowrdCode code = new ForgetPassowrdCode();
        code.setActive(true);
        code.setEmail(user.getEmail());
        code.setCode(randomNumber);

        forgetPasswordRepository.save(code);

        String subject = "Varfication code";
        try {
            emailService.sendSimpleEmail(user.getEmail(), subject, mailtext);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    public void confirmedPassword(String email, long code, String password) {

        ForgetPassowrdCode existCode = forgetPasswordRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Not found user by this email"));

        if (existCode == null || existCode.getCode() != code || existCode.isActive() == false) {
            return;
        }

        if (existCode.getCode() == code && existCode.isActive() == true) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found by this email"));
            user.setPassword(password);
            userRepository.save(user);

            existCode.setActive(false);
            forgetPasswordRepository.save(existCode);
        }

    }

}