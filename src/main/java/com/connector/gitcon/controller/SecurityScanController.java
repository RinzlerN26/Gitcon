package com.connector.gitcon.controller;

import com.connector.gitcon.dto.response.ScanCountResponse;
import com.connector.gitcon.dto.response.ScanHistoryResponse;
import com.connector.gitcon.service.SecurityScanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/security/scans")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Scan History", description = "APIs for viewing security scan history")
@RequiredArgsConstructor
public class SecurityScanController {

        private final SecurityScanService securityScanService;

        @GetMapping("/history")
        @Operation(summary = "Get scan history", description = "Returns the security scan history for the currently authenticated user.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Scan history retrieved successfully"),
        })
        public List<ScanHistoryResponse> getScanHistory(
                        Authentication authentication) {

                Integer userId = (Integer) authentication.getPrincipal();

                return securityScanService.getUserScanHistory(userId);
        }

        @GetMapping("/count")
        @Operation(summary = "Get security scan count", description = "Returns the total number of security scans performed by the currently authenticated user.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Scan count retrieved successfully"),
        })
        public ScanCountResponse getScanCount(
                        Authentication authentication) {

                Integer userId = (Integer) authentication.getPrincipal();

                return new ScanCountResponse(
                                securityScanService.getUserScanCount(userId));
        }
}