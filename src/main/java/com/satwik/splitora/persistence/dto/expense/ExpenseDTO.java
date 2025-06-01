package com.satwik.splitora.persistence.dto.expense;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.satwik.splitora.persistence.dto.user.OwerDTO;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTO {
    private UUID expenseId;

    private String payerName;

    private UUID payerId;

    @NotNull
    private double amount;

    @NotNull
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    private List<OwerDTO> owers;

}
