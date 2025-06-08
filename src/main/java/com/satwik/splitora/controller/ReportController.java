package com.satwik.splitora.controller;

import com.satwik.splitora.configuration.security.LoggedInUser;
import com.satwik.splitora.persistence.dto.ResponseModel;
import com.satwik.splitora.persistence.dto.report.ReportDTO;
import com.satwik.splitora.service.interfaces.ReportService;
import com.satwik.splitora.util.ResponseUtil;
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

    ReportService reportService;

    public ReportController (ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Generates a report for a specific group.
     *
     * @param groupId the UUID of the group for which the report is to be generated.
     * @return a ResponseEntity containing a ResponseModel with a list of ReportDTOs for the specified group.
     */
    @GetMapping("/{groupId}")
    public ResponseEntity<ResponseModel<List<ReportDTO>>> generateReport(@PathVariable UUID groupId) {
        log.info("Get Endpoint: generate a report for a group with groupId: {}", groupId);
        List<ReportDTO> response = reportService.generateReport(groupId);
        ResponseModel<List<ReportDTO>> responseModel = ResponseUtil.success(response, HttpStatus.OK, "Report generated successfully");
        log.info("Get Endpoint: generate a report for a group with response: {}", responseModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    /**
     * Exports a report for a specific group in a specified file format.
     *
     * @param groupId the UUID of the group for which the report is to be exported.
     * @param fileType the type of file in which the report is to be exported (e.g., PDF, CSV).
     * @return a ResponseEntity containing a ResponseModel with a string response message indicating the result.
     */
    @GetMapping("/{groupId}/export")
    public ResponseEntity<ResponseModel<String>> exportReport(@PathVariable UUID groupId, @RequestParam String fileType) {
        log.info("Get Endpoint: export the report triggered for groupId: {} with fileType: {}", groupId, fileType);
        String response = reportService.exportReport(groupId, fileType);
        ResponseModel<String> responseModel = ResponseUtil.success(response, HttpStatus.OK, "Report exported successfully");
        log.info("Get Endpoint: export the report ended with response: {}", responseModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }
}