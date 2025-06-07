package com.satwik.splitora.service.implementations;

import com.satwik.splitora.constants.ErrorMessages;
import com.satwik.splitora.exception.BadRequestException;
import com.satwik.splitora.exception.DataNotFoundException;
import com.satwik.splitora.persistence.dto.expense.*;
import com.satwik.splitora.persistence.dto.user.OwerDTO;
import com.satwik.splitora.persistence.entities.*;
import com.satwik.splitora.repository.*;
import com.satwik.splitora.service.interfaces.ExpenseService;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final AuthorizationService authorizationService;

    private final ExpenseRepository expenseRepository;

    private final GroupRepository groupRepository;

    private final UserRepository userRepository;

    private final GroupMembersRepository groupMembersRepository;

    private final ExpenseShareRepository expenseShareRepository;

    public ExpenseServiceImpl (AuthorizationService authorizationService,
                                  ExpenseRepository expenseRepository,
                                  GroupRepository groupRepository,
                                  UserRepository userRepository,
                                  GroupMembersRepository groupMembersRepository,
                                  ExpenseShareRepository expenseShareRepository) {
        this.authorizationService = authorizationService;
        this.expenseRepository = expenseRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.groupMembersRepository = groupMembersRepository;
        this.expenseShareRepository = expenseShareRepository;
    }

    @Override
    @Transactional
    @PreAuthorize("@authorizationService.isGroupOwner(#groupId)")
    public ExpenseDTO createGroupedExpense(UUID groupId, ExpenseDTO expenseDTO) {

        User payer = expenseDTO.getPayerId() != null ? userRepository.findById(expenseDTO.getPayerId()).orElseThrow(() -> new DataNotFoundException("Payer not found!")) : authorizationService.getAuthorizedUser();
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new DataNotFoundException(ErrorMessages.GROUP_NOT_FOUND));

        // checking if payer is a member of the group
        if(!groupMembersRepository.existsByGroupIdAndMemberId(group.getId(), payer.getId()))
            throw new BadRequestException("Payer is not a member of this group!");

        Expense expense = new Expense();
        expense.setAmount(expenseDTO.getAmount());
        expense.setDescription(expenseDTO.getDescription());
        expense.setGroup(group);
        expense.setPayer(payer);
        expenseRepository.save(expense);

        ExpenseDTO response = new ExpenseDTO();
        response.setExpenseId(expense.getId());
        response.setAmount(expense.getAmount());
        response.setPayerId(expense.getPayer().getId());
        response.setDescription(expense.getDescription());
        response.setPayerName(expense.getPayer().getUsername());
        return response;
    }

    @Override
    @Transactional
    public ExpenseDTO createNonGroupedExpense(ExpenseDTO expenseDTO) {

        User user = authorizationService.getAuthorizedUser();
        User payer = expenseDTO.getPayerId() != null ? userRepository.findById(expenseDTO.getPayerId()).orElseThrow(() -> new DataNotFoundException("Payer not found!")) : authorizationService.getAuthorizedUser();
        Group group = groupRepository.findDefaultGroup(user.getId()).orElseThrow(() -> new DataNotFoundException(ErrorMessages.GROUP_NOT_FOUND));

        Expense expense = new Expense();
        expense.setAmount(expenseDTO.getAmount());
        expense.setDescription(expenseDTO.getDescription());
        expense.setGroup(group);
        expense.setPayer(payer);
        expense = expenseRepository.save(expense);

        ExpenseDTO response = new ExpenseDTO();
        response.setExpenseId(expense.getId());
        response.setAmount(expense.getAmount());
        response.setDescription(expense.getDescription());
        response.setPayerName(expense.getPayer().getUsername());
        response.setPayerId(expense.getPayer().getId());

        return response;
    }

    @Override
    @Transactional
    @PreAuthorize("@authorizationService.isExpenseOwner(#expenseId)")
    public String deleteExpenseById(UUID expenseId) {
        expenseRepository.deleteById(expenseId);
        return "Expense is deleted successfully!";
    }

    @Override
    @Transactional
    @PreAuthorize("@authorizationService.isExpenseOwner(#expenseId)")
    public String addUserToExpense(UUID expenseId, UUID owerId) {

        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new DataNotFoundException(ErrorMessages.EXPENSE_NOT_FOUND));
        User ower = userRepository.findById(owerId).orElseThrow(() -> new DataNotFoundException("Ower not found"));
        if(expense.getPayer().getId().equals(ower.getId()))
            throw new BadRequestException("Payer cannot be added as ower to the expense!");
        ExpenseShare expenseShare = new ExpenseShare();
        expenseShare.setExpense(expense);
        expenseShare.setUser(ower);
        double sharedAmount = expense.getAmount() /
                (expenseShareRepository.findCountOfOwer(expenseId) + 2); // old ower plus one for payer and one for new ower
        expenseShare.setSharedAmount(sharedAmount);
        List<ExpenseShare> shareList = expenseShareRepository.findExpenseShareById(expenseId);
        for (ExpenseShare share : shareList) {
            share.setSharedAmount(sharedAmount);
            expenseShareRepository.save(share);
        }
        expenseShareRepository.save(expenseShare);
        return "Success! Ower is added to the expense.";
    }

    @Override
    @Transactional
    @PreAuthorize("@authorizationService.isExpenseOwner(#expenseId)")
    public String removeUserFromExpense(UUID expenseId, UUID owerId) {
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new DataNotFoundException(ErrorMessages.EXPENSE_NOT_FOUND));
        User ower = userRepository.findById(owerId).orElseThrow(() -> new DataNotFoundException("Ower not found"));
        if(!expenseShareRepository.existsByExpenseIdAndUserId(expense.getId(), ower.getId()))
            throw new BadRequestException("Ower is not part of the expense. Hence, cannot be removed!");
        expenseShareRepository.deleteByExpenseIdAndUserId(expense.getId(), ower.getId());
        double sharedAmount = expense.getAmount() / (expenseShareRepository.findCountOfOwer(expense.getId()) + 1);  // old ower plus one for payer
        List<ExpenseShare> shareList = expenseShareRepository.findExpenseShareById(expense.getId());
        for (ExpenseShare share : shareList) {
            share.setSharedAmount(sharedAmount);
            expenseShareRepository.save(share);
        }
        return "Success! Ower is removed from the expense";
    }

    @Override
    @PreAuthorize("@authorizationService.isExpenseOwner(#expenseId)")
    public ExpenseDTO findExpenseById(UUID expenseId) {
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new DataNotFoundException(ErrorMessages.EXPENSE_NOT_FOUND));
        ExpenseDTO expenseDTO = new ExpenseDTO();
        List<OwerDTO> owerDTOS = expenseShareRepository.findOwersWithAmountByExpenseId(expense.getId());
        expenseDTO.setExpenseId(expense.getId());
        expenseDTO.setOwers(owerDTOS);
        expenseDTO.setDescription(expense.getDescription());
        expenseDTO.setAmount(expense.getAmount());
        expenseDTO.setPayerName(expense.getPayer().getUsername());
        expenseDTO.setPayerId(expense.getPayer().getId());
        expenseDTO.setDate(expense.getCreatedOn());
        return expenseDTO;
    }

    @Override
    @PreAuthorize("@authorizationService.isGroupOwner(#groupId)")
    public List<ExpenseDTO> findAllExpense(UUID groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new DataNotFoundException(ErrorMessages.GROUP_NOT_FOUND));
        List<Expense> expenses = expenseRepository.findByGroupId(group.getId());
        List<ExpenseDTO> expenseDTOS = new ArrayList<>();
        for (Expense expense : expenses) {
            ExpenseDTO expenseDTO = new ExpenseDTO();
            List<OwerDTO> owerDTOS = expenseShareRepository.findOwersWithAmountByExpenseId(expense.getId());
            expenseDTO.setExpenseId(expense.getId());
            expenseDTO.setOwers(owerDTOS);
            expenseDTO.setDescription(expense.getDescription());
            expenseDTO.setDate(expense.getCreatedOn());
            expenseDTO.setAmount(expense.getAmount());
            expenseDTO.setPayerName(expense.getPayer().getUsername());
            expenseDTO.setPayerId(expense.getPayer().getId());
            expenseDTOS.add(expenseDTO);
        }
        return expenseDTOS;
    }
}