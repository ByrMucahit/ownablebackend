package com.example.ownablebackend.repositories;

import com.example.ownablebackend.domain.UserRole;
import com.example.ownablebackend.domain.enumeration.UserRoles;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface RoleRepository extends PagingAndSortingRepository<UserRole, Long> {
    Optional<UserRole> findByName(UserRoles userRoles);

}
