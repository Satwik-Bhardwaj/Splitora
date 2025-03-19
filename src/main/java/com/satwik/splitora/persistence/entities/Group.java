package com.satwik.splitora.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "group_table")
public class Group extends BaseEntity {

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "default_group")
    private boolean defaultGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenseList;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<GroupMembers> groupMembers = new ArrayList<>();

}
