package com.pharmacy.pharmacyservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmacy.pharmacyservice.entity.ForgetPassowrdCode;

@Repository
public interface ForgetPasswordRepository extends JpaRepository<ForgetPassowrdCode,Long> {
 
    Optional<ForgetPassowrdCode> findByEmail(String email);
}
