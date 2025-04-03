package com.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record TransactionModel(
    @JsonProperty("user_id") Long userId,
    @JsonProperty("amount") BigDecimal amount) {
}
