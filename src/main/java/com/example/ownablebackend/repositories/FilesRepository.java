package com.example.ownablebackend.repositories;

import com.example.ownablebackend.domain.Files;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface FilesRepository extends PagingAndSortingRepository<Files, Long> {

    boolean existsByHash(@Param("hash") String hash);
}
