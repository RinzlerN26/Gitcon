package com.connector.gitcon.controller;

import com.connector.gitcon.dto.request.CreateIssueRequest;
import com.connector.gitcon.dto.response.IssueResponse;
import com.connector.gitcon.dto.response.RepoResponse;
import com.connector.gitcon.dto.response.CommitResponse;
import com.connector.gitcon.dto.response.GithubCommitResponse;
import com.connector.gitcon.dto.request.CreatePrRequest;
import com.connector.gitcon.dto.response.PrResponse;
import com.connector.gitcon.service.GithubService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequiredArgsConstructor
@Tag(name = "GitHub", description = "GitHub Repository Management APIs")
@SecurityRequirement(name = "bearerAuth")
public class GithubController {

        private final GithubService githubService;

        @GetMapping("/api/github/repos")
        @Operation(summary = "Get authenticated user repositories", description = "Returns all GitHub repositories accessible by the authenticated user.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Repositories fetched successfully"),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        public List<RepoResponse> getRepos(Authentication authentication) {
                Integer userId = (Integer) authentication.getPrincipal();

                return githubService.fetchRepositories(userId);
        }

        @PostMapping("/api/github/issues")
        @Operation(summary = "Create GitHub Issue", description = "Creates a new issue in the specified GitHub repository.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Issue created successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Repository not found", content = @Content)
        })
        public IssueResponse createIssue(@RequestBody CreateIssueRequest request, Authentication authentication) {
                Integer userId = (Integer) authentication.getPrincipal();
                return githubService.createIssue(request, userId);
        }

        @GetMapping("/api/github/{owner}/{repo}/issues")
        @Operation(summary = "Get repository issues", description = "Fetches all issues for the specified GitHub repository.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Issues fetched successfully"),
                        @ApiResponse(responseCode = "404", description = "Repository not found", content = @Content)
        })
        public ResponseEntity<List<IssueResponse>> getIssues(
                        @Parameter(description = "Repository owner", example = "octocat") @PathVariable String owner,
                        @Parameter(description = "Repository name", example = "Hello-World") @PathVariable String repo,
                        Authentication authentication) {
                Integer userId = (Integer) authentication.getPrincipal();
                return ResponseEntity.ok(githubService.getRepoIssues(owner, repo, userId));
        }

        @GetMapping("/api/github/{owner}/{repo}/commits")
        @Operation(summary = "Get repository commits", description = "Returns commit history of a GitHub repository.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Commits fetched successfully"),
                        @ApiResponse(responseCode = "404", description = "Repository not found", content = @Content)
        })
        public ResponseEntity<List<CommitResponse>> getCommits(
                        @Parameter(description = "Repository owner", example = "octocat") @PathVariable String owner,
                        @Parameter(description = "Repository name", example = "Hello-World") @PathVariable String repo,
                        Authentication authentication) {
                Integer userId = (Integer) authentication.getPrincipal();
                return ResponseEntity.ok(githubService.getRepoCommits(owner, repo, userId));
        }

        @PostMapping("/api/github/{owner}/{repo}/pulls")
        @Operation(summary = "Create Pull Request", description = "Creates a pull request for the specified GitHub repository.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Pull request created successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Repository not found", content = @Content)
        })
        public ResponseEntity<PrResponse> createPullRequest(
                        @Parameter(description = "Repository owner", example = "octocat") @PathVariable String owner,
                        @Parameter(description = "Repository name", example = "Hello-World") @PathVariable String repo,
                        @RequestBody CreatePrRequest request,
                        Authentication authentication) {
                Integer userId = (Integer) authentication.getPrincipal();
                return ResponseEntity.ok(
                                githubService.createPullRequest(owner, repo, request, userId));
        }

        @GetMapping("/api/github/{owner}/{repo}/commits/{sha}")
        @Operation(summary = "Get commit details", description = "Fetch detailed information about a specific commit including changed files and patches.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Commit details fetched successfully"),
                        @ApiResponse(responseCode = "404", description = "Commit not found", content = @Content),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
        })
        public ResponseEntity<GithubCommitResponse> getCommitDetails(
                        @Parameter(description = "Repository owner", example = "octocat") @PathVariable String owner,
                        @Parameter(description = "Repository name", example = "Hello-World") @PathVariable String repo,
                        @Parameter(description = "Commit SHA") @PathVariable String sha,
                        Authentication authentication) {
                Integer userId = (Integer) authentication.getPrincipal();

                return ResponseEntity.ok(
                                githubService.getCommitDetails(owner, repo, sha, userId));
        }
}