package com.satwik.splitwiseclone.service.interfaces;

import com.satwik.splitwiseclone.persistence.dto.expense.ExpenseDTO;

import java.util.List;
import java.util.UUID;

public interface ExpenseService {
    ExpenseDTO createNonGroupedExpense(ExpenseDTO expenseDTO);

    ExpenseDTO createGroupedExpense(UUID groupId, ExpenseDTO expenseDTO);

    String deleteExpenseById(UUID expenseId);

    String addUserToExpense(UUID expenseId, UUID owerId);

    String removeUserFromExpense(UUID expenseId, UUID owerId);

    ExpenseDTO findExpenseById(UUID expenseId);

    List<ExpenseDTO> findAllExpense(UUID groupId);

}
