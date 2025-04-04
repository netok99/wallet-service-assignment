package com.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TransferModel(
    @NotNull(message = "Please provide a start_user_id")
    @JsonProperty("start_user_id")
    Long startUserId,
    @NotNull(message = "Please provide a destination_user_id")
    @JsonProperty("destination_user_id")
    Long destinationUserId,
    @NotNull(message = "Please provide the amount from transaction")
    @JsonProperty("amount")
    BigDecimal amount
) {
}
