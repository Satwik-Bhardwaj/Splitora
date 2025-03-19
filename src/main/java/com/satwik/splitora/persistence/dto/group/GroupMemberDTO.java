package com.satwik.splitora.persistence.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberDTO {

    UUID groupMemberId;

    UUID memberId;

    String username;

    String email;

}
