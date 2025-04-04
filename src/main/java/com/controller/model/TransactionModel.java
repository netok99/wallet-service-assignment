package com.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TransactionModel(
    @NotNull(message = "Please provide a user_id")
    @JsonProperty("user_id")
    Long userId,
    @NotNull(message = "Please provide the amount from transaction")
    @JsonProperty("amount")
    BigDecimal amount
) {
}
