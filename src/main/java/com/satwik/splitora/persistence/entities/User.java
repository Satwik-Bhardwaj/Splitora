package com.satwik.splitora.persistence.entities;

import com.satwik.splitora.constants.enums.RegistrationMethod;
import com.satwik.splitora.constants.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"phone_country_code", "phone_number"})
})
public class User extends BaseEntity {

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_country_code")
    private String countryCode;

    @Column(name = "phone_number")
    private long phoneNumber;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "registration_method")
    private RegistrationMethod registrationMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false, length = 20)
    private UserRole userRole;

    @OneToMany(mappedBy = "payer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expense> expenseList;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Group> groupList;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<ExpenseShare> userInvolvedInExpenses;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupMembers> groupMembers = new ArrayList<>();

}
