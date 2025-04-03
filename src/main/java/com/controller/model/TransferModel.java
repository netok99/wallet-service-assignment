package com.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record TransferModel(
    @JsonProperty("start_user_id") Long startUserId,
    @JsonProperty("destination_user_id") Long destinationUserId,
    @JsonProperty("amount") BigDecimal amount
) {
}
