package com.satwik.splitora.service.implementations;

import com.satwik.splitora.constants.ErrorMessages;
import com.satwik.splitora.exception.DataNotFoundException;
import com.satwik.splitora.persistence.dto.report.ReportDTO;
import com.satwik.splitora.persistence.dto.report.TempReport;
import com.satwik.splitora.persistence.entities.Group;
import com.satwik.splitora.persistence.entities.User;
import com.satwik.splitora.repository.ExpenseShareRepository;
import com.satwik.splitora.repository.GroupRepository;
import com.satwik.splitora.service.interfaces.ReportService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReportServiceImpl implements ReportService {

    private final AuthorizationService authorizationService;

    private final GroupRepository groupRepository;

    private final ExpenseShareRepository expenseShareRepository;

    public ReportServiceImpl(AuthorizationService authorizationService,
                             GroupRepository groupRepository,
                             ExpenseShareRepository expenseShareRepository) {
        this.authorizationService = authorizationService;
        this.groupRepository = groupRepository;
        this.expenseShareRepository = expenseShareRepository;
    }

    @Value("${my.reportFilePath}")
    private String reportFilePath;

    @Override
    @PreAuthorize("@authorizationService.isGroupOwner(#groupId)")
    public List<ReportDTO> generateReport(UUID groupId) {

        Group group = groupRepository.findById(groupId).orElseThrow(() -> new DataNotFoundException(ErrorMessages.GROUP_NOT_FOUND));
        // preparing report
        List<TempReport> tempReportList = groupRepository.generateReportById(group.getId());
        List<ReportDTO> reportDTOS = new ArrayList<>();

        for (TempReport tempReport : tempReportList) {
            ReportDTO reportDTO = new ReportDTO();
            reportDTO.setExpenseOwner(tempReport.getExpenseOwner());
            reportDTO.setExpenseName(tempReport.getExpenseName());
            reportDTO.setGroupName(tempReport.getGroupName());
            reportDTO.setTotalExpenseAmount(tempReport.getTotalExpenseAmount());

//          getting contributors
            List<String> contributors = expenseShareRepository.findPayersById(tempReport.getExpenseId());
            reportDTO.setExpenseContributors(contributors);
            reportDTOS.add(reportDTO);
        }

        return reportDTOS;
    }

    @Override
    @PreAuthorize("@authorizationService.isGroupOwner(#groupId)")
    public String exportReport(UUID groupId, String fileType) {
        User user = authorizationService.getAuthorizedUser();
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new DataNotFoundException(ErrorMessages.GROUP_NOT_FOUND));

        if(user == null || user.getId() != group.getUser().getId()) throw new AccessDeniedException("Access Denied");

        if(fileType.equals("XLSX"))
            exportToXLSX(groupId);
        else if (fileType.equals("CSV"))
            exportToCSV(groupId);
        else
            throw new RuntimeException("Invalid file type");

        return "File successfully created at path - " + reportFilePath;
    }

    private void exportToCSV(UUID groupId) {

        List<TempReport> tempReportList = groupRepository.generateReportById(groupId);
        List<ReportDTO> reportDTOS = new ArrayList<>();

        for (TempReport tempReport : tempReportList) {
            ReportDTO reportDTO = new ReportDTO();
            reportDTO.setExpenseOwner(tempReport.getExpenseOwner());
            reportDTO.setExpenseName(tempReport.getExpenseName());
            reportDTO.setGroupName(tempReport.getGroupName());
            reportDTO.setTotalExpenseAmount(tempReport.getTotalExpenseAmount());

//          getting contributors
            List<String> contributors = expenseShareRepository.findPayersById(tempReport.getExpenseId());
            reportDTO.setExpenseContributors(contributors);
            reportDTOS.add(reportDTO);
        }

        try (FileOutputStream fos = new FileOutputStream(reportFilePath + "data.csv")) {
            StringBuilder csvData = new StringBuilder();
            csvData.append("groupName,expenseName,expenseOwner,expenseContributors,totalExpenseAmount\n");
            for (ReportDTO reportDTO : reportDTOS) {
                csvData.append("\"").append(reportDTO.getGroupName()).append("\",");
                csvData.append("\"").append(reportDTO.getExpenseName()).append("\",");
                csvData.append("\"").append(reportDTO.getExpenseOwner()).append("\",");
                csvData.append("\"").append(String.join(",", reportDTO.getExpenseContributors())).append("\",");
                csvData.append(reportDTO.getTotalExpenseAmount()).append("\n");
            }
            fos.write(csvData.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void exportToXLSX(UUID groupId) {
        List<TempReport> tempReportList = groupRepository.generateReportById(groupId);
        List<ReportDTO> reportDTOS = new ArrayList<>();

        for (TempReport tempReport : tempReportList) {
            ReportDTO reportDTO = new ReportDTO();
            reportDTO.setExpenseOwner(tempReport.getExpenseOwner());
            reportDTO.setExpenseName(tempReport.getExpenseName());
            reportDTO.setGroupName(tempReport.getGroupName());
            reportDTO.setTotalExpenseAmount(tempReport.getTotalExpenseAmount());

//          getting contributors
            List<String> contributors = expenseShareRepository.findPayersById(tempReport.getExpenseId());
            reportDTO.setExpenseContributors(contributors);
            reportDTOS.add(reportDTO);
        }

        try (FileOutputStream outputStream = new FileOutputStream(reportFilePath + "data.xlsx")) {

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Data");
            int rowNum = 0;

            Row headerRow = sheet.createRow(rowNum++);
            headerRow.createCell(0).setCellValue("groupName");
            headerRow.createCell(1).setCellValue("expenseName");
            headerRow.createCell(2).setCellValue("expenseOwner");
            headerRow.createCell(3).setCellValue("expenseContributors");
            headerRow.createCell(4).setCellValue("totalExpenseAmount");

            for (ReportDTO reportDTO : reportDTOS) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(reportDTO.getGroupName());
                row.createCell(1).setCellValue(reportDTO.getExpenseName());
                row.createCell(2).setCellValue(reportDTO.getExpenseOwner());
                row.createCell(3).setCellValue(String.join(",", reportDTO.getExpenseContributors()));
                row.createCell(4).setCellValue(reportDTO.getTotalExpenseAmount());
            }
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
