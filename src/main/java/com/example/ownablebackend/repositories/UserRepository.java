package com.example.ownablebackend.repositories;

import com.example.ownablebackend.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    Optional<User> findByUserName(@Param(value = "userName") String userName);
    Boolean existsByUserName(@Param(value = "userName") String userName);
    Boolean existsByEmail(@Param(value = "email") String email);
}
