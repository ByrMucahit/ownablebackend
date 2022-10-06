package com.example.ownablebackend.repositories;

import com.example.ownablebackend.domain.PasswordResetToken;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PasswordTokenRepository extends PagingAndSortingRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(@Param("token") String token);
}
