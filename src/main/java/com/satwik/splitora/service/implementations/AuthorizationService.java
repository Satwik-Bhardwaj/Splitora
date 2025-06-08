package com.satwik.splitora.service.implementations;

import com.satwik.splitora.configuration.security.LoggedInUser;
import com.satwik.splitora.constants.ErrorMessages;
import com.satwik.splitora.constants.enums.UserRole;
import com.satwik.splitora.exception.DataNotFoundException;
import com.satwik.splitora.persistence.entities.Expense;
import com.satwik.splitora.persistence.entities.Group;
import com.satwik.splitora.persistence.entities.User;
import com.satwik.splitora.repository.ExpenseRepository;
import com.satwik.splitora.repository.GroupRepository;
import com.satwik.splitora.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("authorizationService")
public class AuthorizationService {

    private final LoggedInUser loggedInUser;

    private final UserRepository userRepository;

    private final GroupRepository groupRepository;

    private final ExpenseRepository expenseRepository;

    public AuthorizationService(LoggedInUser loggedInUser,
                                UserRepository userRepository,
                                GroupRepository groupRepository,
                                ExpenseRepository expenseRepository) {
        this.loggedInUser = loggedInUser;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.expenseRepository = expenseRepository;
    }

    public User getAuthorizedUser() {
        return userRepository.findByEmail(loggedInUser.getUserEmail()).orElseThrow(() -> new DataNotFoundException("User not found"));
    }

    public boolean isGroupOwner(UUID groupId) {
        if (loggedInUser.hasRole(UserRole.ADMIN))
            return true;

        Group group = groupRepository.findById(groupId).orElseThrow(() -> new DataNotFoundException(ErrorMessages.GROUP_NOT_FOUND));
        UUID ownerId = group.getUser().getId();
        return loggedInUser.getUserId().equals(ownerId);
    }

    public boolean isExpenseOwner(UUID expenseId) {
        if (loggedInUser.hasRole(UserRole.ADMIN))
            return true;

        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new DataNotFoundException(ErrorMessages.EXPENSE_NOT_FOUND));
        UUID ownerId = expense.getPayer().getId();
        return loggedInUser.getUserId().equals(ownerId);
    }

}
