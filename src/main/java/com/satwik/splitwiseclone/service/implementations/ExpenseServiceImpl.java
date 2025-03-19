package com.satwik.splitwiseclone.service.implementations;

import com.satwik.splitwiseclone.exception.BadRequestException;
import com.satwik.splitwiseclone.exception.DataNotFoundException;
import com.satwik.splitwiseclone.persistence.dto.expense.*;
import com.satwik.splitwiseclone.persistence.dto.user.OwerDTO;
import com.satwik.splitwiseclone.persistence.entities.*;
import com.satwik.splitwiseclone.repository.*;
import com.satwik.splitwiseclone.service.interfaces.ExpenseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupMembersRepository groupMembersRepository;

    @Autowired
    private ExpenseShareRepository expenseShareRepository;


    @Override
    @Transactional
    public ExpenseDTO createGroupedExpense(UUID groupId, ExpenseDTO expenseDTO) {

        User payer = expenseDTO.getPayerId() != null ? userRepository.findById(expenseDTO.getPayerId()).orElseThrow(() -> new DataNotFoundException("Payer not found!")) : authorizationService.getAuthorizedUser();
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new DataNotFoundException("Group not found"));

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
        Group group = groupRepository.findDefaultGroup(user.getId()).orElseThrow(() -> new DataNotFoundException("Group not found"));

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

        // TODO: add owner itself for
        return response;
    }

    @Override
    @Transactional
    public String deleteExpenseById(UUID expenseId) {
        expenseRepository.deleteById(expenseId);
        return "Expense is deleted successfully!";
    }

    @Override
    @Transactional
    public String addUserToExpense(UUID expenseId, UUID owerId) {

        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new RuntimeException("Expense not found"));
        User ower = userRepository.findById(owerId).orElseThrow(() -> new RuntimeException("Ower not found"));
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
    public String removeUserFromExpense(UUID expenseId, UUID owerId) {
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new DataNotFoundException("Expense not found"));
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
    public ExpenseDTO findExpenseById(UUID expenseId) {
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new DataNotFoundException("Expense not found"));
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
    public List<ExpenseDTO> findAllExpense(UUID groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new DataNotFoundException("Group not found"));
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