package com.satwik.splitora.service.interfaces;

import com.satwik.splitora.persistence.dto.group.GroupDTO;
import com.satwik.splitora.persistence.dto.group.GroupListDTO;
import com.satwik.splitora.persistence.dto.group.GroupUpdateRequest;
import com.satwik.splitora.persistence.dto.user.UserDTO;

import java.util.List;
import java.util.UUID;

public interface GroupService {
    String createGroup(GroupDTO groupDTO);

    String deleteGroupByGroupId(UUID groupId);

    GroupDTO findGroupByGroupId(UUID groupId);

    String updateGroup(GroupUpdateRequest groupUpdateRequest, UUID groupId);

    GroupListDTO findAllGroup();

    String addGroupMembers(UUID groupId, UUID memberId);

    List<UserDTO> findMembers(UUID groupId);

    String deleteMembers(UUID groupId, UUID groupMemberId);
}
