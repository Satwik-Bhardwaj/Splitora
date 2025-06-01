package com.satwik.splitora.controller;

import com.satwik.splitora.configuration.security.LoggedInUser;
import com.satwik.splitora.persistence.dto.ResponseModel;
import com.satwik.splitora.persistence.dto.group.GroupDTO;
import com.satwik.splitora.persistence.dto.group.GroupListDTO;
import com.satwik.splitora.persistence.dto.group.GroupUpdateRequest;
import com.satwik.splitora.persistence.dto.user.UserDTO;
import com.satwik.splitora.service.interfaces.GroupService;
import com.satwik.splitora.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/group")
public class GroupController {

    @Autowired
    GroupService groupService;

    @Autowired
    LoggedInUser loggedInUser;

    /**
     * Creates a group for a user.
     *
     * This endpoint processes the request to create a new group with the given group data.
     * It logs the incoming request and the resulting response.
     *
     * @param groupDTO the data transfer object containing the details of the group to be created.
     * @return a ResponseEntity containing a string response message indicating the
     *         result of the group creation process.
     */
    @PostMapping("/create")
    public ResponseEntity<ResponseModel<String>> createGroup(@Valid @RequestBody GroupDTO groupDTO) {
        log.info("Post Endpoint: create group request: {}", groupDTO);
        String response = groupService.createGroup(groupDTO);
        ResponseModel<String> responseModel = ResponseUtil.success(response, HttpStatus.OK, "Group created successfully");
        log.info("Post Endpoint: create group response: {}", responseModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    /**
     * Deletes a group in a user account.
     *
     * This endpoint processes the request to delete a group identified by the given group ID.
     * It logs the incoming request and the resulting response.
     *
     * @param groupId the UUID of the group to be deleted.
     * @return a ResponseEntity containing a string response message indicating the
     *         result of the group deletion process.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseModel<String>> deleteGroup(@RequestParam UUID groupId) {
        log.info("Delete Endpoint: delete group with id: {}", groupId);
        String response = groupService.deleteGroupByGroupId(groupId);
        ResponseModel<String> responseModel = ResponseUtil.success(response, HttpStatus.OK, "Group deleted successfully");
        log.info("Delete Endpoint: delete group response: {}", responseModel);
        return ResponseEntity.ok(responseModel);
    }

    /**
     * Retrieves a group in a user account.
     *
     * This endpoint processes the request to find a group identified by the given group ID.
     * It logs the incoming request and the resulting response.
     *
     * @param groupId the UUID of the group to be retrieved.
     * @return a ResponseEntity containing the GroupDTO of the requested group.
     */
    @GetMapping("/{groupId}")
    public ResponseEntity<ResponseModel<GroupDTO>> findGroup(@PathVariable UUID groupId) {
        log.info("Get Endpoint: find group with id: {}", groupId);
        GroupDTO groupDTO = groupService.findGroupByGroupId(groupId);
        ResponseModel<GroupDTO> responseModel = ResponseUtil.success(groupDTO, HttpStatus.OK, "Group retrieved successfully");
        log.info("Get Endpoint: find group response: {}", responseModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    /**
     * Updates a group in a user account.
     *
     * This endpoint processes the request to update a group identified by the given group ID
     * with the provided update data. It logs the incoming request and the resulting response.
     *
     * @param groupUpdateRequest the data transfer object containing the details of the group update.
     * @param groupId the UUID of the group to be updated.
     * @return a ResponseEntity containing a string response message indicating the
     *         result of the group update process.
     */
    @PutMapping("/update/{groupId}")
    public ResponseEntity<ResponseModel<String>> updateGroup(@RequestBody GroupUpdateRequest groupUpdateRequest, @PathVariable UUID groupId) {
        log.info("Put Endpoint: update group with request: {} and request: {}", groupUpdateRequest, groupId);
        String response = groupService.updateGroup(groupUpdateRequest, groupId);
        ResponseModel<String> responseModel = ResponseUtil.success(response, HttpStatus.OK, "Group updated successfully");
        log.info("Put Endpoint: update group with response: {}", responseModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    /**
     * Retrieves all groups of the user.
     *
     * This endpoint processes the request to find all groups associated with the user.
     * It logs the incoming request and the resulting response.
     *
     * @return a ResponseEntity containing a GroupListDTO with all the groups of the user.
     */
    @GetMapping("")
    public ResponseEntity<ResponseModel<GroupListDTO>> findAllGroup() {
        log.info("Get Endpoint: find all group of the user");
        GroupListDTO response = groupService.findAllGroup();
        ResponseModel<GroupListDTO> responseModel = ResponseUtil.success(response, HttpStatus.OK, "Groups retrieved successfully");
        log.info("Get Endpoint: find all group of the user with response: {}", responseModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    /**
     * Adds members to a group.
     *
     * This endpoint processes the request to add a member identified by the given member ID
     * to a group identified by the given group ID. It logs the incoming request and
     * the resulting response.
     *
     * @param groupId the UUID of the group to which the member will be added.
     * @param memberId the UUID of the member to be added to the group.
     * @return a ResponseEntity containing a string response message indicating the
     *         result of the member addition process.
     */
    @PostMapping("/add-member/{groupId}")
    public ResponseEntity<ResponseModel<String>> addGroupMembers(@PathVariable UUID groupId, @RequestParam UUID memberId) {
        log.info("Post Endpoint: add member with memberId: {} to the group with groupId: {}", memberId, groupId);
        String response = groupService.addGroupMembers(groupId, memberId);
        ResponseModel<String> responseModel = ResponseUtil.success(response, HttpStatus.OK, "Member added to group successfully");
        log.info("Post Endpoint: add member with response: {}", responseModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    /**
     * Retrieves all members of a group.
     *
     * This endpoint processes the request to find all members of a group identified
     * by the given group ID. It logs the incoming request and the resulting response.
     *
     * @param groupId the UUID of the group whose members are to be retrieved.
     * @return a ResponseEntity containing a list of UserDTOs for all members of the group.
     */
    @GetMapping("/{groupId}/members")
    public ResponseEntity<ResponseModel<List<UserDTO>>> findMembers(@PathVariable UUID groupId) {
        log.info("Get Endpoint: find members of group with groupId: {}", groupId);
        List<UserDTO> response = groupService.findMembers(groupId);
        ResponseModel<List<UserDTO>> responseModel = ResponseUtil.success(response, HttpStatus.OK, "Group members retrieved successfully");
        log.info("Get Endpoint: find members of group with response: {}", responseModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    /**
     * Removes a member from a group.
     *
     * This endpoint processes the request to remove a member identified by the given group member ID
     * from a group identified by the given group ID. It logs the incoming request and
     * the resulting response.
     *
     * @param groupMemberId the UUID of the member to be removed from the group.
     * @param groupId the UUID of the group from which the member will be removed.
     * @return a ResponseEntity containing a string response message indicating the
     *         result of the member removal process.
     */
    @DeleteMapping("/remove-member/{groupMemberId}")
    public ResponseEntity<ResponseModel<String>> deleteMembers(@PathVariable UUID groupMemberId, @RequestParam UUID groupId) {
        log.info("Delete Endpoint: delete a member with memberId: {} and groupId: {}", groupMemberId, groupId);
        String response = groupService.deleteMembers(groupId, groupMemberId);
        ResponseModel<String> responseModel = ResponseUtil.success(response, HttpStatus.OK, "Member removed from group successfully");
        log.info("Delete Endpoint: delete a member with response: {}", responseModel);
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }
}