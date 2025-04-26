package com.satwik.splitora.controller;

import com.satwik.splitora.persistence.dto.ResponseModel;
import com.satwik.splitora.persistence.dto.expense.ExpenseDTO;
import com.satwik.splitora.service.interfaces.ExpenseService;
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
@RequestMapping("/api/v1/expense")
public class ExpenseController {

    @Autowired
    ExpenseService expenseService;

    /**
     * Creates a new expense which is not grouped.
     *
     * This endpoint processes the request to create a non-grouped expense. It logs the
     * incoming request and the resulting response.
     *
     * @param expenseDTO the request body containing the expense details.
     * @return a ResponseEntity containing a string response message indicating the
     *         result of the expense creation process.
     */
    @PostMapping("/create")
    public ResponseEntity<ResponseModel<ExpenseDTO>> createExpense(@RequestBody ExpenseDTO expenseDTO) {
        log.info("Post Endpoint: create expense with request: {}", expenseDTO);
        ExpenseDTO response = expenseService.createNonGroupedExpense(expenseDTO);
        ResponseModel<ExpenseDTO> responseModel = ResponseUtil.success(response, HttpStatus.OK, "Expense created successfully");
        log.info("Post Endpoint: create expense with response: {}", responseModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    /**
     * Creates a new expense which is grouped.
     *
     * This endpoint processes the request to create a grouped expense. It logs the
     * incoming request and the resulting response.
     *
     * @param groupId the UUID of the group to which the expense belongs.
     * @param expenseDTO the request body containing the expense details.
     * @return a ResponseEntity containing a string response message indicating the
     *         result of the grouped expense creation process.
     */
    @PostMapping("/create/{groupId}")
    public ResponseEntity<ResponseModel<ExpenseDTO>> createExpense(@PathVariable UUID groupId, @RequestBody ExpenseDTO expenseDTO) {
        log.info("Post Endpoint: create grouped expense with request: {} and groupId: {}", expenseDTO, groupId);
        ExpenseDTO response = expenseService.createGroupedExpense(groupId, expenseDTO);
        ResponseModel<ExpenseDTO> responseModel = ResponseUtil.success(response, HttpStatus.OK, "Grouped expense created successfully");
        log.info("Post Endpoint: create grouped expense with response: {}", responseModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    /**
     * Deletes an expense by its ID.
     *
     * This endpoint processes the request to delete an expense identified by the given ID.
     * It logs the incoming request and the resulting response.
     *
     * @param expenseId the UUID of the expense to be deleted.
     * @return a ResponseEntity containing a string response message indicating the
     *         result of the expense deletion process.
     */
    @DeleteMapping("/delete/{expenseId}")
    public ResponseEntity<ResponseModel<String>> deleteExpense(@PathVariable UUID expenseId) {
        log.info("Delete Endpoint: delete an expense with id: {}", expenseId);
        String response = expenseService.deleteExpenseById(expenseId);
        ResponseModel<String> responseModel = ResponseUtil.success(response, HttpStatus.OK, "Expense deleted successfully");
        log.info("Delete Endpoint: delete an expense with response: {}", responseModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    /**
     * Adds an ower to an existing expense.
     *
     * This endpoint processes the request to add an ower identified by the given ower ID
     * to an expense identified by the given expense ID. It logs the incoming request and
     * the resulting response.
     *
     * @param owerId the UUID of the ower to be added to the expense.
     * @param expenseId the UUID of the expense to which the ower will be added.
     * @return a ResponseEntity containing a string response message indicating the
     *         result of the operation.
     */
    @PostMapping("/add-ower/{expenseId}")
    public ResponseEntity<ResponseModel<String>> addOwerToExpense(@RequestParam UUID owerId, @PathVariable UUID expenseId) {
        log.info("Post Endpoint: add ower with owerId: {}, to an expense with expenseId: {}", owerId, expenseId);
        String response = expenseService.addUserToExpense(expenseId, owerId);
        ResponseModel<String> responseModel = ResponseUtil.success(response, HttpStatus.OK, "Ower added to expense successfully");
        log.info("Post Endpoint: add ower to expense with response: {}", responseModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    /**
     * Removes a ower from an existing expense.
     *
     * This endpoint processes the request to remove an ower identified by the given ower ID
     * from an expense identified by the given expense ID. It logs the incoming request and
     * the resulting response.
     *
     * @param owerId the UUID of the ower to be removed from the expense.
     * @param expenseId the UUID of the expense from which the ower will be removed.
     * @return a ResponseEntity containing a string response message indicating the
     *         result of the operation.
     */
    @DeleteMapping("/remove-ower/{expenseId}")
    public ResponseEntity<ResponseModel<String>> removeOwerFromExpense(@RequestParam UUID owerId, @PathVariable UUID expenseId) {
        log.info("Delete Endpoint: remove ower with owerId: {} from an expense with expenseId: {}", owerId, expenseId);
        String response = expenseService.removeUserFromExpense(expenseId, owerId);
        ResponseModel<String> responseModel = ResponseUtil.success(response, HttpStatus.OK, "Ower removed from expense successfully");
        log.info("Delete Endpoint: remove ower from an expense with response: {}", responseModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    /**
     * Retrieves an expense by its ID.
     *
     * This endpoint processes the request to find an expense identified by the given expense ID.
     * It logs the incoming request and the resulting response.
     *
     * @param expenseId the UUID of the expense to be retrieved.
     * @return a ResponseEntity containing the ExpenseDTO of the requested expense.
     */
    @GetMapping("/{expenseId}")
    public ResponseEntity<ResponseModel<ExpenseDTO>> findExpense(@PathVariable UUID expenseId) {
        log.info("Get Endpoint: find an expense with expenseId: {}", expenseId);
        ExpenseDTO response = expenseService.findExpenseById(expenseId);
        ResponseModel<ExpenseDTO> responseModel = ResponseUtil.success(response, HttpStatus.OK, "Expense retrieved successfully");
        log.info("Get Endpoint: find an expense with response: {}", responseModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    /**
     * Retrieves all expenses within a specific group.
     *
     * This endpoint processes the request to find all expenses within a group identified
     * by the given group ID. It logs the incoming request and the resulting response.
     *
     * @param groupId the UUID of the group whose expenses are to be retrieved.
     * @return a ResponseEntity containing a list of ExpenseDTOs for all expenses within the group.
     */
    @GetMapping("/group/{groupId}")
    public ResponseEntity<ResponseModel<List<ExpenseDTO>>> findAllExpense(@PathVariable UUID groupId) {
        log.info("Get Endpoint: find all expense within a group with groupId: {}", groupId);
        List<ExpenseDTO> response = expenseService.findAllExpense(groupId);
        ResponseModel<List<ExpenseDTO>> responseModel = ResponseUtil.success(response, HttpStatus.OK, "Expenses retrieved successfully");
        log.info("Get Endpoint: find all expense within a group with response: {}", responseModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }
}