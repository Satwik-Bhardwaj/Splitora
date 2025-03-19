package com.satwik.splitora.persistence.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

    String groupName;

    String expenseName;

    String expenseOwner;

    List<String> expenseContributors;

    double totalExpenseAmount;

}
