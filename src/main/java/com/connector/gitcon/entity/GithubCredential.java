package com.connector.gitcon.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "github_credentials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GithubCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String githubUsername;

    @Column(nullable = false, length = 500)
    private String accessToken;
}