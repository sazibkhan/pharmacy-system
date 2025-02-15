package com.pharmacy.pharmacyservice.repository;


import com.pharmacy.pharmacyservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface IUserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String username);

}
