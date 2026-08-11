package com.connector.gitcon.repository;

import com.connector.gitcon.entity.GithubCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GithubCredentialRepository
        extends JpaRepository<GithubCredential, Integer> {

    Optional<GithubCredential> findByUserId(Integer userId);

    boolean existsByUserId(Integer userId);
}