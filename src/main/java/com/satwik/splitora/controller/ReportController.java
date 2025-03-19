package com.satwik.splitora.controller;

import com.satwik.splitora.configuration.security.LoggedInUser;
import com.satwik.splitora.persistence.dto.report.ReportDTO;
import com.satwik.splitora.service.interfaces.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/report")
public class ReportController {

    @Autowired
    ReportService reportService;

    @Autowired
    LoggedInUser loggedInUser;

    /**
     * Generates a report for a specific group.
     *
     * This endpoint processes the request to generate a report for a group identified by the given group ID.
     * It logs the incoming request and the resulting response.
     *
     * @param groupId the UUID of the group for which the report is to be generated.
     * @return a ResponseEntity containing a list of ReportDTOs for the specified group.
     */
    @GetMapping("/{groupId}")
    public ResponseEntity<List<ReportDTO>> generateReport(@PathVariable UUID groupId) {
        log.info("Get Endpoint: generate a report for a group with groupId: {}", groupId);
        List<ReportDTO> response = reportService.generateReport(groupId);
        log.info("Get Endpoint: generate a report for a group with response: {}", response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Exports a report for a specific group in a specified file format.
     *
     * This endpoint processes the request to export a report for a group identified by the given group ID
     * in the specified file format. It logs the incoming request and the resulting response.
     *
     * @param groupId the UUID of the group for which the report is to be exported.
     * @param fileType the type of file in which the report is to be exported (e.g., PDF, CSV).
     * @return a ResponseEntity containing a string response message indicating the
     *         result of the report export process.
     */
    @GetMapping("/{groupId}/export")
    public ResponseEntity<String> exportReport(@PathVariable UUID groupId, @RequestParam String fileType) {
        log.info("Get Endpoint: export the report triggered");
        String response = reportService.exportReport(groupId, fileType);
        log.info("Get Endpoint: export the report ended");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
