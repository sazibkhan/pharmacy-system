package com.pharmacy.pharmacyservice.repository;

import com.pharmacy.pharmacyservice.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    // @Query("select t from Token t inner join User u on t.user.id=u,id where
    // t.user.id=:userId and t.isLogout=false")
    // List<Token> findTokenByUser(String userId);

    @Query("select t from Token t inner join User u on t.user.id = u.id where t.user.id = :userId and t.isLogout = false")
    List<Token> findTokenByUser(String userId);

}
