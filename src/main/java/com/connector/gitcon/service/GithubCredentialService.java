package com.connector.gitcon.service;

import com.connector.gitcon.dto.request.GithubCredentialRequest;
import com.connector.gitcon.dto.response.GithubCredentialResponse;
import com.connector.gitcon.entity.GithubCredential;
import com.connector.gitcon.entity.User;
import com.connector.gitcon.exception.CustomServiceException;
import com.connector.gitcon.repository.GithubCredentialRepository;
import com.connector.gitcon.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GithubCredentialService {

        private final GithubCredentialRepository githubCredentialRepository;
        private final UserRepository userRepository;
        private final EncryptionService encryptionService;

        public GithubCredentialResponse saveCredential(
                        Integer userId,
                        GithubCredentialRequest request) {

                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new CustomServiceException(HttpStatus.NOT_FOUND, "User not found"));

                if (githubCredentialRepository.existsByUserId(userId)) {
                        throw new CustomServiceException(HttpStatus.CONFLICT, "GitHub credential already exists");
                }

                String encryptedToken = encryptionService.encrypt(
                                request.getAccessToken());

                GithubCredential credential = GithubCredential.builder()
                                .user(user)
                                .githubUsername(
                                                request.getGithubUsername())
                                .accessToken(encryptedToken)
                                .build();

                githubCredentialRepository.save(credential);

                return new GithubCredentialResponse(
                                credential.getGithubUsername());
        }

        public GithubCredentialResponse getCredential(
                        Integer userId) {

                GithubCredential credential = githubCredentialRepository
                                .findByUserId(userId)
                                .orElseThrow(() -> new CustomServiceException(HttpStatus.NOT_FOUND,
                                                "GitHub credential not found"));

                return new GithubCredentialResponse(
                                credential.getGithubUsername());
        }

        public void deleteCredential(Integer userId) {

                GithubCredential credential = githubCredentialRepository
                                .findByUserId(userId)
                                .orElseThrow(() -> new CustomServiceException(HttpStatus.NOT_FOUND,
                                                "GitHub credential not found"));

                githubCredentialRepository.delete(credential);
        }

        public String getDecryptedToken(Integer userId) {

                GithubCredential credential = githubCredentialRepository
                                .findByUserId(userId)
                                .orElseThrow(() -> new CustomServiceException(HttpStatus.NOT_FOUND,
                                                "GitHub credential not found"));

                return encryptionService.decrypt(
                                credential.getAccessToken());
        }
}
