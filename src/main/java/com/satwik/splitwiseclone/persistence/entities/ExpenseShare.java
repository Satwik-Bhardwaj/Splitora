package com.satwik.splitwiseclone.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expense_share", uniqueConstraints = { @UniqueConstraint(columnNames = { "expense_id", "user_id" }) })
public class ExpenseShare extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id")
    private Expense expense;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "shared_amount")
    private double sharedAmount;

}
